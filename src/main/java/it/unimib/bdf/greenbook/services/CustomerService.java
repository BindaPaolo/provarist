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
    
    
    public List<Customer> findAllCustomersByFirstNameAndLastNameAllIgnoringCase(String firstName, String lastName){
    	return repository.findAllCustomersByFirstNameAndLastNameAllIgnoringCase(firstName, lastName);
    }
    
    
    
    public List<Customer> findAllCustomersByReservationId(Long reservation_id){
    	List<Long> customer_id_list = this.repository.findAllCustomersByReservationId(reservation_id); 
    	List<Customer> customer_obj_list = new ArrayList<Customer>();
    	
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
        return repository.save(customer);
    }

    public void deleteById(Long id) {
    	log.info("\n\nENTRO in deleteById\n\n");
    	updateRecommendedBy(id);
        repository.deleteById(id);
    }
    
    public void updateRecommendedBy(Long id) {
    	for(Customer c: repository.findAll()) {
    		
    		if(c.getRecommendedBy() != null) {
    			if(c.getRecommendedBy().getId() == id) {
    				c.setRecommendedBy(null);
    				repository.updateRecommendedBy(c.getId(), null);
    			}
    		}
    	}
    }

}