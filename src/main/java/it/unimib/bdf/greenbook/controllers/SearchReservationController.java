package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.containers.DateContainer;
import it.unimib.bdf.greenbook.containers.ReservationListContainer;
import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.services.ReservationService;
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
import java.time.LocalDate;
import javax.validation.Valid;


@Controller
@RequestMapping("/reservation/search*")
public class SearchReservationController {
	@Autowired
	private ReservationService reservationService;
	
	
    @GetMapping("/search-reservation-by-customer")
    public String searchReservationByCustomer(Model model) {
        model.addAttribute("customer", new Customer());

        return "reservation/search/search-reservation-by-customer";
    }
    
    @GetMapping("/search-reservation-by-date")
    public String serachReservationByDate(Model model) {
    	DateContainer dateContainer = new DateContainer();
    	model.addAttribute("dateContainer", dateContainer);

        return "/reservation/search/search-reservation-by-date";
    }

    @PostMapping("/executeSearchReservationByCustomer")
    public String executeSearchReservationByCustomer(Model model,
                                                     @Valid @ModelAttribute Customer customer,
                                                     BindingResult result) {
        if (result.getFieldErrors("firstName").size() != 0 ||
                result.getFieldErrors("lastName").size() != 0) {
        	//non devo riaggiungere customer al modello?
            return "/reservation/search/search-reservation-by-customer";
        }
		
		
		ReservationListContainer reservationListContainer = new ReservationListContainer();
		//Fill the reservation list container with the reservations
		//in the db where a customer with those first and last name 
		//has took part.
		reservationListContainer.setReservations(reservationService.findAllReservationByCustomerFirstNameAndLastName(customer.getFirstName(), customer.getLastName()));

		// Adding list of reservations with that customer's first and last name
		model.addAttribute("reservationListContainer", reservationListContainer);
		// Adding useful data, needed in other methods
		model.addAttribute("searchType", "byCustomer");
		model.addAttribute("firstName", customer.getFirstName());
		model.addAttribute("lastName", customer.getLastName());
		
		
		return "/reservation/search/search-results";
	}

	@PostMapping("/executeSearchReservationByDate")
	public String executeSearchReservationByDate(Model model, 
												@ModelAttribute DateContainer dateContainer) {		
		// Error Check: empty date field
		if(dateContainer.getDate() == null) {
			model.addAttribute("emptyDateField", "Seleziona una data.");
			model.addAttribute("dateContainer", dateContainer);
            return "/reservation/search/search-reservation-by-date";
        }
		
		ReservationListContainer reservationListContainer = new ReservationListContainer();
		//Find all reservations in the db
		//that have happened/will happened
		//on the specified date.
		reservationListContainer.setReservations(reservationService.findAllReservationsByDate(dateContainer.getDate()));
		model.addAttribute("reservationListContainer", reservationListContainer);
		model.addAttribute("searchType", "byDate");
		model.addAttribute("date", dateContainer.getDate());

		return "/reservation/search/search-results";
	}
	

	
    @PostMapping("/deleteReservation/{id}")
    public String deleteReservation(@PathVariable Long id, 
    								@RequestParam("searchType") String searchType,
    								@RequestParam(value = "firstName", required = false) String firstName,
    								@RequestParam(value = "lastName", required = false) String lastName,
    								@RequestParam(value = "date", required = false) String date,
    								Model model) {
    	
    	ReservationListContainer reservationListContainer = new ReservationListContainer();
    	//delete the selected reservation
		reservationService.deleteById(id);
		
		//update search results page
		//after the deletion of the reservation.
		if (searchType.equals("byDate")) {
			reservationListContainer.setReservations(reservationService.findAllReservationsByDate(LocalDate.parse(date)));
			model.addAttribute("date", date);
		}
		else {
			//searchType = byCustomer
			reservationListContainer.setReservations(reservationService.findAllReservationByCustomerFirstNameAndLastName(firstName, lastName));			
			model.addAttribute("firstName", firstName);
			model.addAttribute("lastName", lastName);
		}


		model.addAttribute("reservationListContainer", reservationListContainer);
		model.addAttribute("searchType", searchType);
		
        return "reservation/search/search-results";
    }
	
    
	@PostMapping("/cancelSearchReservation")
	public String cancelSearchReservation(Model model) {
		return "/reservation/reservations";
	}
}
	


