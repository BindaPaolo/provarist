package it.unimib.bdf.greenbook.services;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Transactional
public class CustomerService {

	@Autowired
    private CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }
    
    public List<Long> findAllCustomerReservations(Long id){
    	return repository.findAllCustomerReservations(id);
    }
    
    
    public List<Customer> findAllCustomersByFirstNameAndLastNameAllIgnoringCase(String firstName, String lastName){
    	return repository.findAllCustomersByFirstNameAndLastNameAllIgnoringCase(firstName, lastName);
    }
    
    public List<Customer> findAllCustomersByMobileNumber(String mobileNumber){
        return repository.findAllCustomersByMobileNumber(mobileNumber);
    }
    
    public List<Customer> findAllCustomersByReservationId(Long reservation_id){
    	List<Long> customer_id_list = this.repository.findAllCustomersByReservationId(reservation_id); 
    	List<Customer> customer_obj_list = new ArrayList<>();
    	
    	for (Long customer_id : customer_id_list) {
    		customer_obj_list.add(this.findById(customer_id).get());
    	}
    	
    	return customer_obj_list;
    }

    public List<Customer> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Customer> findById(Long id) {
        return repository.findById(id);
    }

    public Customer save(Customer customer) {
        // Check if the recommended-by field is left empty
    	fixRecommendedByForeignKey(customer);
        return repository.save(customer);
    }
    

    public void deleteById(Long id) {
    	log.info("\n\nENTRO in deleteById\n\n");
    	cleanRecommendedByFieldOnCustomerDelete(id);
        repository.deleteById(id);
    }
    
    /**
     * On customer deletion, if it was referenced by other customers as the recommendedBy, we need to set the
     * recommendedBy fields of those that remain to null.
     * 
     * @param id id of the customer being removed
     */
    public void cleanRecommendedByFieldOnCustomerDelete(Long id) {
    	for(Customer c: repository.findAll()) {
    		
    		if(c.getRecommendedBy() != null) {
    			if(c.getRecommendedBy().getId() == id) {
    				c.setRecommendedBy(null);
    				repository.updateRecommendedBy(c.getId(), null);
    			}
    		}
    	}
    }

    
    /**
     * If the recommended by field is left empty by the user, make the RecommendedBy object null so that the foreign
     * key in the database is set to null
     *
     * @param customer the customer object
     */
    public void fixRecommendedByForeignKey(Customer customer) {
        String recommendedByMobileNumber = customer.getRecommendedBy().getMobileNumber();
        if (recommendedByMobileNumber.isEmpty()) {
            // If the recommended by field is left empty by the user, make the RecommendedBy object null
            customer.setRecommendedBy(null);
        } else {
            // Fetch the customer in the database which has the mobile number given by the user
            customer.setRecommendedBy(findAllCustomersByMobileNumber(recommendedByMobileNumber).get(0));
        }
    }


    /**
     * Checks that the mobile number is stored in the database (in this case, the user is inserting a duplicate)
     *
     * @param mobileNumber mobile number of the customer that the user wants to insert
     */
    public boolean isMobileNumberPersisted(String mobileNumber){
        return !findAllCustomersByMobileNumber(mobileNumber).isEmpty();
    }


    /**
     * Checks for mobile number duplicates
     *
     * @param mobileNumber mobile number of the customer that the user wants to update
     * @return alreadyPresent returns true if the mobile number was already persisted for a different customer
     */
    public boolean checkForMobileNumberDuplicates(Long id, String mobileNumber){
        List<Customer> customersList = findAllCustomersByMobileNumber(mobileNumber);
        boolean alreadyPresent = false;

        for(Customer c : customersList){
            if (c.getId() != id) {
                alreadyPresent = true;
                break;
            }
        }

        return alreadyPresent;
    }

}