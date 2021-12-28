package it.unimib.bdf.greenbook.controllers;


import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.ReservationService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@SessionAttributes("reservation")
public class ReservationController {
	
	@Autowired
	private ReservationService service;
	
    @GetMapping("/reservations")
    public String showAllReservations(Model model) {
        return "reservation/reservations";
    }
    
    @GetMapping("/search-reservation-by-customer")
    public String searchReservationByCustomer(Model model) {
    	return "reservation/search-reservation-by-customer";
    }
    
    @GetMapping("/search-reservation-by-date")
    public String serachReservationByDate(Model model) {
    	return "reservation/search-reservation-by-date";
    }

    @GetMapping("/new-reservation")
    public String showNewReservationForm(Model model) {
    	log.info("Entro in new-reservation");
    	
    	if (model.getAttribute("reservation") == null) {
            model.addAttribute("reservation", new Reservation());
    	}
    	else if (model.getAttribute("customer") != null){
    		Customer newCustomer = (Customer) model.getAttribute("customer");
    		Reservation reservation = (Reservation) model.getAttribute("reservation");
    		reservation.addReservationCustomer(newCustomer);
    	}
    	
        return "reservation/new-reservation";
    }
    
    
    @PostMapping("/addCustomerToReservation")
    public String addCustomerToReservation(Model model,
    		@ModelAttribute("reservation") Reservation reservation
    		) {
    	
    	log.info("Entro in addCustomerToReservation");
    	
    	return "redirect:/customer/new-customer";
    }
    

    @PostMapping("/saveReservation")
    public String saveReservation(@Valid @ModelAttribute Reservation reservation,
    							BindingResult result, 
    							Model model,
    							WebRequest request,
    							SessionStatus status) {
        log.info("Entro in saveReservation");

    	if (result.hasErrors()) {
            return "reservation/new-reservation";
        }
    	log.info("Saving Reservation and Customer objects...");
    	service.save(reservation);
    	log.info("Reservation and Customer objects saved");
    	
    	log.info("Ending Session...");
    	status.setComplete();
    	
    	log.info("Removing Reservation object from session");
    	request.removeAttribute("reservation", WebRequest.SCOPE_SESSION);
    	
        
        return "reservation/reservations";
    }
    
    @PostMapping("/cancelReservation")
    public String cancelReservation(@ModelAttribute Reservation reservation,
									Model model,
									WebRequest request,
									SessionStatus status) {
        log.info("Entro in cancelReservation\n\n\n\n");
    	log.info("Ending Session...");
    	status.setComplete();
    	
    	log.info("Removing Reservation object from session");
    	request.removeAttribute("reservation", WebRequest.SCOPE_SESSION);
        return "reservation/reservations";
    }    

    @GetMapping("/deleteReservationCustomer/{firstName}&{lastName}&{mobileNumber}")
    public String deleteReservationCustomer(@PathVariable("firstName") String firstName,
    						    				@PathVariable("lastName") String lastName,
    						    				@PathVariable("mobileNumber") String mobileNumber,
    						    				Model model){
    	Reservation reservation = (Reservation) model.getAttribute("reservation");
    	removeCustomer(firstName, lastName, mobileNumber, reservation);
    	
    	return "reservation/new-reservation";
    }
    
    
    //edit = remove + add new
    @GetMapping("/editReservationCustomer/{firstName}&{lastName}&{mobileNumber}")
    public String editReservationCustomer(@PathVariable("firstName") String firstName,
    						    		@PathVariable("lastName") String lastName,
    						    		@PathVariable("mobileNumber") String mobileNumber,
    						    		Model model){
    	Reservation reservation = (Reservation) model.getAttribute("reservation");
    	removeCustomer(firstName, lastName, mobileNumber, reservation);

    	
    	return "redirect:/customer/new-customer";
    }


    @GetMapping("/showReservation/{id}")
    public String showReservationById(@PathVariable Long id, Model model) {
        Reservation reservation = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        model.addAttribute("reservation", reservation);
        return "reservation/edit-reservation";
    }
   
    @PostMapping("/updateReservation/{id}")
    public String updateReservation(@PathVariable Long id, @Valid @ModelAttribute Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "reservation/edit-reservation";
        }
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        service.save(reservation);
        model.addAttribute("reservations", service.findAll());
        return "reservation/reservations";
    }

    @PostMapping("/deleteReservation/{id}")
    public String deleteReservation(@PathVariable Long id, Model model) {
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        service.deleteById(id);
        model.addAttribute("reservations", service.findAll());
        return "reservation/reservations";
    }
    
    private void removeCustomer(String firstName, String lastName, String mobileNumber, Reservation reservation) {
    	Customer found = new Customer();	
    	for(Customer c : reservation.getReservation_customers()) {
    		if (c.getFirstName().equalsIgnoreCase(firstName) &&
    			c.getLastName().equalsIgnoreCase(lastName) &&
    			c.getMobileNumber().equals(mobileNumber)) {
    			found = c;
    		}
    	}
    	reservation.getReservation_customers().remove(found);
    	
    	
    	return;
    }

}
