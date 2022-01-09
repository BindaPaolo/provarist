package it.unimib.bdf.greenbook.controllers;


import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;

import it.unimib.bdf.greenbook.containers.ReservationListContainer;
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
								@RequestParam("searchType") String searchType,
								@RequestParam(value = "firstName", required = false) String firstName,
								@RequestParam(value = "lastName", required = false) String lastName,
								@RequestParam(value = "date", required = false) String date,								
								Model model) {
		
		
		Reservation reservation = reservationService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));

		model.addAttribute("waitersList", getPersistedWaiters());		
		model.addAttribute("reservation", reservation);
		
		httpSession.setAttribute("searchType", searchType);
		if(searchType.equals("byDate")) {
			httpSession.setAttribute("date", LocalDate.parse(date));
		}
		else {
			httpSession.setAttribute("firstName", firstName);
			httpSession.setAttribute("lastName", lastName);
		}

		return "/reservation/edit/edit-reservation";
	}
	
	
	@PostMapping("/cancel-edit-reservation")
	public String cancelEditReservation(Model model) {
		ReservationListContainer reservationListContainer = new ReservationListContainer();
		// Cleanup and close the session and set up model attribute 
		// needed when going back to search-results.
		String searchType = (String) httpSession.getAttribute("searchType");
		model.addAttribute("searchType", searchType);

		if(searchType.equals("byDate")) {
			LocalDate date = (LocalDate) httpSession.getAttribute("date");
			httpSession.removeAttribute("date");
			model.addAttribute("date", date);
			reservationListContainer.setReservations(reservationService.findAllReservationsByDate(date));
		}
		else {
			model.addAttribute("firstName", httpSession.getAttribute("firstName"));
			model.addAttribute("lastName", httpSession.getAttribute("lastName"));
			reservationListContainer.setReservations(reservationService.findAllReservationByCustomerFirstNameAndLastName("firstName", "lastName"));

			httpSession.removeAttribute("firstName");
			httpSession.removeAttribute("lastName");
		}
		httpSession.removeAttribute("searchType");
		httpSession.removeAttribute("reservation");
		httpSession.invalidate();
		
		model.addAttribute("reservationListContainer", reservationListContainer);

		return "/reservation/search/search-results";
	}

	private List<Employee> getPersistedWaiters(){
		List<Employee> persistedEmployees = employeeService.findAll();
		persistedEmployees.removeIf(
			obj -> obj.getRole() != Employee.roleEnumType.Cameriere && obj.getRole() != Employee.roleEnumType.CapoSala
		);
		
		return persistedEmployees;
	}

}
