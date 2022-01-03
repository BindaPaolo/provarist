package it.unimib.bdf.greenbook.services;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.repositories.CustomerRepository;
import it.unimib.bdf.greenbook.repositories.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@Service
public class ReservationService{
	@Autowired
    private ReservationRepository reservationRepository;
	@Autowired
    private CustomerRepository customerRepository;
	@Autowired
	private CustomerService customerService;
	
    

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
    		CustomerRepository customerRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
    }
    
    public List<Reservation> findAllReservationsByCustomerId(Long id){
    	return this.reservationRepository.findAllReservationsByCustomerId(id);
    }

    public List<Reservation> findAll() {
        return StreamSupport.stream(reservationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }
    
    public List<Reservation> findAllReservationsByDate(LocalDate date){
    	return reservationRepository.findAllReservationsByDate(date);
    }

    @Transactional
    public Reservation save(Reservation reservation) {
    	// List for customers that are already inside the database
    	List<Customer> customersToRemove = new ArrayList<>();
    	List<Customer> customersToAdd = new ArrayList<>();
    	
    	for (Customer customer : reservation.getReservation_customers()) {

    		if(customerService.findAllCustomersByMobileNumber(customer.getMobileNumber()).isEmpty()) {
    			// The customer isn't present in the db.
            	customerService.save(customer);
    		}
    		else {
    			// switch the two customer objects
    			// and update the recommendedBy field
    			// if the new customer's is not null
    			// and the old customer's field is null
    			Customer alreadyExistingCustomer  = customerService.findAllCustomersByMobileNumber(customer.getMobileNumber()).get(0);
    			customerService.fixRecommendedByForeignKey(customer);
    			if(alreadyExistingCustomer.getRecommendedBy() == null && customer.getRecommendedBy() != null){
    				alreadyExistingCustomer.setRecommendedBy(customer.getRecommendedBy());
    			}
    			customersToAdd.add(alreadyExistingCustomer);
    			customersToRemove.add(customer);
    		}

    	}
    	
		reservation.getReservation_customers().addAll(customersToAdd);
    	reservation.getReservation_customers().removeAll(customersToRemove);
    	//Save the reservation object.
    	reservationRepository.save(reservation);
    	
        return reservation;
    }


    public void deleteById(Long id) {
    	//Take care of the recommended by stuff
    	Reservation reservation = this.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
    	for(Customer c : reservation.getReservation_customers()) {
    		customerService.cleanRecommendedByFieldOnCustomerDelete(c.getId());
    	}
    	reservationRepository.deleteById(id);
    }
    
    public List<Reservation> findAllReservationByCustomerFirstNameAndLastName(String firstName, String lastName){
    	return reservationRepository.findAllReservationByCustomerFirstNameAndLastName(firstName, lastName);
    }

}



/*
if(!customerService.findAllCustomersByMobileNumber(customer.getMobileNumber()).isEmpty()) {
	Customer existingCustomer = customerService.findAllCustomersByMobileNumber(customer.getMobileNumber()).get(0);
	if (existingCustomer != null) {
		if (customer.getRecommendedBy() != null && existingCustomer.getRecommendedBy() == null) {
			existingCustomer.setRecommendedBy(customer.getRecommendedBy());
			customerService.save(existingCustomer);
		}
		
	}
}*/