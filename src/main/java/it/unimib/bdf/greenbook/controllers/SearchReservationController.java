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
import org.springframework.web.bind.annotation.RequestParam;

import it.unimib.bdf.greenbook.models.Customer;

import it.unimib.bdf.greenbook.models.Reservation;

import it.unimib.bdf.greenbook.services.ReservationService;
import it.unimib.bdf.greenbook.models.ReservationListContainer;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;


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
		model.addAttribute("searchType", "byCustomer");
		model.addAttribute("firstName", customer.getFirstName());
		model.addAttribute("lastName", customer.getLastName());
		
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
		model.addAttribute("searchType", "byDate");
		model.addAttribute("date", date);

		return "/reservation/search/search-results";
	}
	
	@PostMapping("/cancelSearchReservation")
	public String cancelSearchReservation(Model model) {
		log.info("Aborting reservation search");
		return "/reservation/reservations";
	}
	
    @PostMapping("/deleteReservation/{id}")
    public String deleteReservation(@PathVariable Long id, 
    								@ModelAttribute("reservations") ReservationListContainer reservationListContainer,
    								@RequestParam(value="searchType", required=false) String searchType,
    								@RequestParam(value="firstName", required=false) String firstName,
    								@RequestParam(value="lastName", required=false) String lastName,
    								@RequestParam(value="date", required=false) String date,
    								Model model) {
    	
		reservationService.deleteById(id);

    	if (searchType.equals("byCustomer")) {
    		reservationListContainer.setReservations(reservationService.findAllReservationByCustomerFirstNameAndLastName(firstName, lastName));
    		model.addAttribute("firstName", firstName);
    		model.addAttribute("lastName", lastName);
    	}  	
    	else{
    		reservationListContainer.setReservations(reservationService.findAllReservationsByDate(LocalDate.parse(date)));    		
    		model.addAttribute("date", date);
    	}
    	

		model.addAttribute("reservationListContainer", reservationListContainer);
		model.addAttribute("searchType", searchType);

		
        return "/reservation/search/search-results";
    }
	
}
	


