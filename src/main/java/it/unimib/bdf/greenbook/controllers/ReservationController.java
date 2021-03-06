package it.unimib.bdf.greenbook.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/reservation*")
public class ReservationController {
	
    @GetMapping("/reservations")
    public String showAllReservations(Model model) {
        return "/reservation/reservations";
    }
}
