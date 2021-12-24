package it.unimib.bdf.greenbook.controllers;


import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.models.Reservation.shiftEnumType;
import it.unimib.bdf.greenbook.services.ReservationService;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
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
        model.addAttribute("reservation", new Reservation());
        return "reservation/new-reservation";
    }
    
    /*
    @PostMapping("/addCustomerToReservation")
    public String addCustomerToReservation(Model model,
    		@Valid @ModelAttribute("reservation") Reservation reservation,
    		BindingResult result,
    		RedirectAttributes redirectAttributes) {
    	
    	if(result.hasErrors()) {
    		return "reservation/new-reservation";
    	}
    	log.info("Entro in addCustomerToReservation");

    	redirectAttributes.addFlashAttribute("reservation", reservation);
    	
    	return "redirect:/new-customer";
    }*/
    
    @PostMapping("/addCustomerToReservation")
    public String addCustomerToReservation(Model model,
    		@Valid @ModelAttribute("reservation") Reservation reservation,
    		BindingResult result) {
    	
    	if(result.hasErrors()) {
    		return "reservation/new-reservation";
    	}
    	log.info("Entro in addCustomerToReservation");

    	model.addAttribute("reservation", reservation);
    	
    	return "/new-customer";
    }
    
    

    @PostMapping(value="/saveReservation", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveReservation(@Valid @ModelAttribute Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/reservations";
        }
        log.info("\n\n\n Entro in ReservationController.saveReservation\n\n\n\n");
        
        return "reservation/reservations";
    }
    
    @PostMapping(value="/cancelReservation", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String cancelReservation(@Valid @ModelAttribute Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "reservation/reservations";
        }
        log.info("\n\n\n Entro in ReservationController.cancelReservation\n\n\n\n");

        
        return "reservation/reservations";
    }    
    
    
    @PostMapping(value = "/addReservation", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addNewReservation(@Valid @ModelAttribute Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "reservation/reservations";
        }
        service.save(reservation);
        model.addAttribute("reservations", service.findAll());
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
