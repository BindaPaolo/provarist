package it.unimib.bdf.greenbook.services;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@Slf4j
public class CustomerService {

    private CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }
    
    public List<Customer> findAllCustomersByReservationId(Long reservation_id){
    	log.info("\n\n\n\n ENTRO SERVICE \n\n\n\n");
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
        repository.deleteById(id);
    }

}