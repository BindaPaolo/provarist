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
import it.unimib.bdf.greenbook.services.CustomerService;
import it.unimib.bdf.greenbook.services.ReservationService;
import lombok.extern.slf4j.Slf4j;

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
		
		log.info("First name: " + customer.getFirstName() + " Last name: " + customer.getLastName());
		
		//Get all customers with given firstName and lastName.
		//Note: there can be multiple rows in customers with same pair (firstName, lastName)
		List<Customer> customers = customerService.findAllCustomersByFirstNameAndLastNameAllIgnoringCase(customer.getFirstName(), customer.getLastName());
		
		for(Customer c : customers) {
			log.info(c.toString());
		}
			
			
		//List<Reservation> reservations = reservationService.
		
		
		return "/reservation/search/search-results";
	}
	
	@PostMapping("/cancelSearchReservation")
	public String cancelSearchReservation(Model model) {
		log.info("Aborting reservation search");
		return "/reservation/reservations";
	}
}
	


