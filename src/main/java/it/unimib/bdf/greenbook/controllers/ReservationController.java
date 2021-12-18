package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.ReservationService;

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

@Controller
public class ReservationController {
	
	@Autowired
	private ReservationService service;
	
    @GetMapping("/reservations")
    public String showAllReservations(Model model) {
        model.addAttribute("reservations", service.findAll());
        return "reservation/reservations";
    }

    @GetMapping("/new-reservation")
    public String showNewReservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "reservation/new-reservation";
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
    public String showCustomerById(@PathVariable Long id, Model model) {
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
    public String deleteCustomer(@PathVariable Long id, Model model) {
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        service.deleteById(id);
        model.addAttribute("reservations", service.findAll());
        return "reservation/reservations";
    }

}
