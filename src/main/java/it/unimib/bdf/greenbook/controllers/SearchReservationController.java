package it.unimib.bdf.greenbook.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.CustomerService;
import it.unimib.bdf.greenbook.services.ReservationService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value= "/reservation/search*")
public class SearchReservationController {
	@Autowired
	private ReservationService reservationService;
	@Autowired
	private CustomerService customerService;
	
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
				
		//Get all customers with given firstName and lastName.
		//Note: there can be multiple rows in customers with same pair (firstName, lastName)
		//		and different IDs.
		List<Customer> customers = customerService.findAllCustomersByFirstNameAndLastNameAllIgnoringCase(customer.getFirstName(), customer.getLastName());
		
		List<Reservation> reservations = new ArrayList<>();
		for(Customer c : customers) {
			Long id = c.getId();
			reservations.addAll(reservationService.findAllReservationsByCustomerId(id));
		}
		
		for(Reservation r : reservations) {
			log.info(r.toString());
		}


		model.addAttribute("reservations", reservations);
		
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
		
		List<Reservation> reservations = new ArrayList<>();
		
		reservations.addAll(reservationService.findAllReservationsByDate(date));
		
		model.addAttribute("reservations", reservations);
		
		return "/reservation/search/search-results";
	}
	
	@PostMapping("/cancelSearchReservation")
	public String cancelSearchReservation(Model model) {
		log.info("Aborting reservation search");
		return "/reservation/reservations";
	}
	
	
}
	


