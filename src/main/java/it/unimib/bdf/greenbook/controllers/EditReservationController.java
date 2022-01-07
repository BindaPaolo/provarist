package it.unimib.bdf.greenbook.controllers;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.unimib.bdf.greenbook.models.Employee;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.ReservationService;
import it.unimib.bdf.greenbook.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/reservation/edit*")
public class EditReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private HttpSession httpSession;
	
	@PostMapping("/editReservation/{id}")
	public String editReservation(@PathVariable Long id,
									Model model) {
		log.info("\n\n\n entro in edit-reservation\n\n\n");
		Reservation reservation = reservationService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
		model.addAttribute("waitersList", getPersistedWaiters());
		
		model.addAttribute("reservation", reservation);
		
		httpSession.setAttribute("reservation", reservation);
		
		
		log.info(reservation.toString());
		
		return "/reservation/edit/edit-reservation";
	}
	
	//TODO
	@PostMapping("/cancel-edit-reservation")
	public String cancelEditReservation(Model model) {
		httpSession.removeAttribute("reservation");
		httpSession.invalidate();
		/*
		model.addAttribute("reservation", httpSession.getAttribute("reservation"));
		model.addAttribute("waitersList", getPersistedWaiters());
		*/
		log.info("\n\n" + httpSession.toString());
		
		
		return "/reservation/search/search-reservation-by-date";
	}

	//METODO DUPLICATO (NewReservationController)!!
	private List<Employee> getPersistedWaiters(){
		List<Employee> persistedEmployees = employeeService.findAll();
		persistedEmployees.removeIf(
			obj -> obj.getRole() != Employee.roleEnumType.Cameriere && obj.getRole() != Employee.roleEnumType.CapoSala
		);
		
		return persistedEmployees;
	}

}
