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
    	//Save the reservation object.
    	//Save each customer object.

        log.info("\n\n\n" + reservation.toString() + "\n\n\n\n");
    	reservationRepository.save(reservation);




    	for (Customer customer : reservation.getReservation_customers()) {
        	customerRepository.save(customer);
    	}
    	
        return reservation;
    }


    public void deleteById(Long id) {
    	reservationRepository.deleteById(id);
    }

}