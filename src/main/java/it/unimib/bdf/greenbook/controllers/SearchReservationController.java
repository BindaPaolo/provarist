package it.unimib.bdf.greenbook.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.unimib.bdf.greenbook.models.Customer;

import it.unimib.bdf.greenbook.models.Reservation;

import it.unimib.bdf.greenbook.services.ReservationService;
import it.unimib.bdf.greenbook.models.ReservationListContainer;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/reservation/search*")
public class SearchReservationController {
	@Autowired
	private ReservationService reservationService;

	
    @GetMapping("/search-reservation-by-customer")
    public String searchReservationByCustomer(Model model) {
    	model.addAttribute("customer", new Customer());
    	log.info("Going to search-reservation-by-customer");
    	
    	return "reservation/search/search-reservation-by-customer";
    }
    
    @GetMapping("/search-reservation-by-date")
    public String serachReservationByDate(Model model) {
    	model.addAttribute("reservation", new Reservation());
    	log.info("Going to search-reservatoin-by-date");
    	
    	return "/reservation/search/search-reservation-by-date";
    }
	
	@PostMapping("/executeSearchReservationByCustomer")
	public String executeSearchReservationByCustomer(Model model, 
									@Valid @ModelAttribute Customer customer,
									BindingResult result) {
		log.info("Entro in executeSearchReservationByCustomer");
		if(result.getFieldErrors("firstName").size() != 0 ||
				result.getFieldErrors("lastName").size() != 0) {
            return "/reservation/search/search-reservation-by-customer";
        }
		
		
		ReservationListContainer reservationListContainer = new ReservationListContainer();
		reservationListContainer.setReservations(reservationService.findAllReservationByCustomerFirstNameAndLastName(customer.getFirstName(), customer.getLastName()));


		model.addAttribute("reservationListContainer", reservationListContainer);
		
		return "/reservation/search/search-results";
	}

	@PostMapping("/executeSearchReservationByDate")
	public String executeSearchReservationByDate(Model model, 
									@Valid @ModelAttribute Reservation reservation,
									BindingResult result) {
		
		log.info("Entro in executeSearchReservationByDate");
		
		if(result.getFieldError("date") != null) {
            return "/reservation/search/search-reservation-by-date";
        }
		
		LocalDate date = reservation.getDate();
		
		ReservationListContainer reservationListContainer = new ReservationListContainer();
		reservationListContainer.setReservations(reservationService.findAllReservationsByDate(date));
		model.addAttribute("reservationListContainer", reservationListContainer);
		return "/reservation/search/search-results";
	}
	
	@PostMapping("/cancelSearchReservation")
	public String cancelSearchReservation(Model model) {
		log.info("Aborting reservation search");
		return "/reservation/reservations";
	}
	
	
    //Al momento questa funzione elimina la reservation solo dalla lista
    //delle reservations che vengono mostrare in search-result.jsp 
    //e non dal dbms, in quanto l'eliminazione di una reservation (al momento)
    //triggera un Referential Constraint violation.
    @PostMapping("/deleteReservation/{id}")
    public String deleteReservation(@PathVariable Long id, 
    								@ModelAttribute("reservations") ReservationListContainer reservationListContainer,
    								Model model) {
    	log.info("Entro in deleteReservation");
    	log.info("Lista delle prenotazioni:  "+reservationListContainer.getReservations());   	
    	//Elimino la prenotazione selezionata dalla lista delle prenotazioni
    	//che verrano mostrate in search-results.jsp
    	reservationListContainer.getReservations().removeIf(r -> (r.getReservation_id() == id));

    	
    	Reservation reservationToDelete = (Reservation) reservationService.findById(id)
                							.orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
    	log.info("Reservation da eliminare: "+reservationToDelete.toString());
    	
    	List<Reservation> newReservationList = new ArrayList<>();
    	for(Reservation r : reservationListContainer.getReservations()) {
    		newReservationList.add(reservationService.findById(r.getReservation_id())
    							.orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id)));
    	}
    	reservationListContainer.setReservations(newReservationList);
    	
    	//Ancora non posso farlo. Triggera un Referential Constrain error.
    	reservationService.deleteById(id); //da errore!!
    	
		model.addAttribute("reservationListContainer", reservationListContainer);
        return "/reservation/search/search-results";
    }
	
}
	


