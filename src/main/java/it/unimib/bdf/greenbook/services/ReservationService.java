package it.unimib.bdf.greenbook.services;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.repositories.ReservationRepository;
import it.unimib.bdf.greenbook.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ReservationService{

    private ReservationRepository reservationRepository;
    private CustomerRepository customerRepository;

    

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
    		CustomerRepository customerRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
    }

    public List<Reservation> findAll() {
        return StreamSupport.stream(reservationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public Reservation save(Reservation reservation) {
    	//Save the reservation object.
    	//Save each customer object.


        log.info("\n\n\n" + reservation.toString() + "\n\n\n\n");
        //searchIdByPhone(reservation);
        log.info("\n\n\n" + reservation.toString() + "\n\n\n\n");
    	reservationRepository.save(reservation);




    	for (Customer customer : reservation.getReservation_customers()) {
        	customerRepository.save(customer);
    	}
    	
        return reservation;
    }

//    public void searchIdByPhone(Reservation reservation) {
//
//        for(Customer customer : reservation.getReservation_customers()) {
//
//        /*
//        Se il numero di telefono inserito dall'utente non è nullo viene ricercato, tra tutti i Customer inseriti, l'id associato
//        al numero di telefono inserito e successivamente viene modificato il suo refferral
//        Nel caso il numero di telefono inserito dall'utente fosse nullo viene aggiornata la variabile "recommendedById" con il valore "null"
//        */
//            if (!customer.getRecommendedById().getMobileNumber().equalsIgnoreCase("")) {
//                for (Customer c : reservation.getReservation_customers()) {
//                    if (c.getMobileNumber().equalsIgnoreCase(customer.getRecommendedById().getMobileNumber())) {
//
//                        customer.setRecommendedById(c);
//                    }
//                }
//
//            } else {
//
//                customer.setRecommendedById(null);
//            }
//        }
//    }

    public void deleteById(Long id) {
    	reservationRepository.deleteById(id);
    }

}