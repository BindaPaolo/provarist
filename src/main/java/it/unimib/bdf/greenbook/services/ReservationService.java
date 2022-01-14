package it.unimib.bdf.greenbook.services;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class ReservationService{
	@Autowired
    private ReservationRepository reservationRepository;

	@Autowired
	private CustomerService customerService;
	   
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
    	List<Customer> customersToRemove = new ArrayList<>();
    	List<Customer> customersToAdd = new ArrayList<>();
    	
    	for(Customer c : reservation.getReservation_customers()) {
    		//Trim the customer's fields of type String
        	c.setFirstName(c.getFirstName().trim());
        	c.setLastName(c.getLastName().trim());
        	c.setMobileNumber(c.getMobileNumber().trim());
    		//Check if recommendedBy field was left empty, in
    		//that case is set to null, otherwise find the 
    		//customer in the database that recommended Customer c
    		//and save its reference.
    		customerService.fixRecommendedByForeignKey(c);
    		//Can't insert the same entity (same mobile number) twice.
    		String mobileNumber = c.getMobileNumber();
    		//Get all the customers with the same mobile number.
    		//Size must be 0 or 1.
    		List<Customer> customers = customerService.findAllCustomersByMobileNumber(mobileNumber);
    		if (!customers.isEmpty()) {
    			//Then it contains only one customer.
    			Customer returningCustomer = customers.get(0);
    			//Check if there's new information this time.
    			//On whether he was recommended
    			if(returningCustomer.getRecommendedBy() == null && c.getRecommendedBy() != null) {
    				returningCustomer.setRecommendedBy(customerService.findAllCustomersByMobileNumber(c.getRecommendedBy().getMobileNumber()).get(0));
    			}
    			//And always update it's allergies to the most
    			//recent ones.
    			returningCustomer.setAllergies(c.getAllergies());
    			customerService.save(returningCustomer);
    			customersToRemove.add(c);
    			customersToAdd.add(returningCustomer);
    		}
    		else{
    			//New customer.
        		customerService.save(c);
    		}

    	}
    	reservation.getReservation_customers().removeAll(customersToRemove);
    	reservation.getReservation_customers().addAll(customersToAdd);
    	//Save the reservation object.
    	reservationRepository.save(reservation);
    	
        return reservation;
    }


    public void deleteById(Long id) {
    	this.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
    	//Delete reservation and clean join table with customer and waiters
    	reservationRepository.deleteById(id);
    }
    
    public List<Reservation> findAllReservationByCustomerFirstNameAndLastName(String firstName, String lastName){
    	firstName = firstName.trim();
    	lastName = lastName.trim();
    	return reservationRepository.findAllReservationByCustomerFirstNameAndLastName(firstName, lastName);
    }

}
