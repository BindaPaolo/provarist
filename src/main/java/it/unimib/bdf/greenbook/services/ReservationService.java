package it.unimib.bdf.greenbook.services;

import it.unimib.bdf.greenbook.controllers.NewReservationController;
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

import lombok.extern.slf4j.Slf4j;


@Slf4j
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
    	for(Customer c : reservation.getReservation_customers()) {
    		customerService.save(c);
    	}
    	
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
    	return reservationRepository.findAllReservationByCustomerFirstNameAndLastName(firstName, lastName);
    }

}
