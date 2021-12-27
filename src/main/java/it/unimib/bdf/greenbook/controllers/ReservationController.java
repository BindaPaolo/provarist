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
    	log.info("\nEntro in new-reservation");
    	
    	if (model.getAttribute("reservation") == null) {
    		log.info("\n\n\n OCCHIO Reservation object NOT in the model");
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
    		@Valid @ModelAttribute("reservation") Reservation reservation,
    		BindingResult result) {
    	
    	if(result.hasErrors()) {
    		return "reservation/new-reservation";
    	}
    	log.info("Entro in addCustomerToReservation");
    	
    	return "redirect:/customer/new-customer";
    }
    

    @PostMapping("/saveReservation")
    public String saveReservation(@Valid @ModelAttribute Reservation reservation,
    							BindingResult result, 
    							Model model,
    							WebRequest request,
    							SessionStatus status) {
        log.info("\n\n\n Entro in ReservationController.saveReservation");

    	if (result.hasErrors()) {
            return "/reservations";
        }
    	log.info("Ending Session...");
    	status.setComplete();
    	
    	log.info("Saving Reservation and Customer objects...");
    	service.save(reservation);
    	
    	log.info("Reservation and Customer objects saved");
    	request.removeAttribute("reservation", WebRequest.SCOPE_SESSION);
    	
        
        return "reservation/reservations";
    }
    
    @PostMapping("/cancelReservation")
    public String cancelReservation(@Valid @ModelAttribute Reservation reservation, Model model) {
        log.info("\n\n\n Entro in ReservationController.cancelReservation\n\n\n\n");

        
        return "reservation/reservations";
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

}
