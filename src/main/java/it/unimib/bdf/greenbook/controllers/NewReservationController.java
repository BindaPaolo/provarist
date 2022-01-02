package it.unimib.bdf.greenbook.controllers;


import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Employee;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.EmployeeService;
import it.unimib.bdf.greenbook.services.ReservationService;
import it.unimib.bdf.greenbook.services.AllergenService;


import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@Controller
@SessionAttributes("reservation")
@RequestMapping("/reservation/new*")
public class NewReservationController {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private AllergenService allergenService;

    
    @GetMapping("/new-reservation")
    public String showNewReservationForm(Model model) {
    	log.info("Entro in new-reservation");
    	

		// Show persisted waiters
		model.addAttribute("waitersList", getPersistedWaiters());

		return "/reservation/new/new-reservation";
    }
    
    @PostMapping("/newReservationCustomer")
    public String newReservationCustomer(Model model, 
    									@ModelAttribute("reservation") Reservation reservation,
    									@Valid @ModelAttribute("customer") Customer customer,
    									BindingResult result,
    									@RequestParam("action") String action) {
    	
    	log.info("Entro in newReservationCustomer");

		switch (action) {
			case "show":
				log.info("action = show");
				model.addAttribute("customer", new Customer());
				model.addAttribute("allergensList", allergenService.findAll());
				return "/reservation/new/new-reservation-customer";
			case "add":
				log.info("action = add");
				if (result.hasErrors()) {
					model.addAttribute("allergensList", allergenService.findAll());
					return "/reservation/new/new-reservation-customer";
				}
				reservation.addReservationCustomer(customer);
				model.addAttribute("waitersList", getPersistedWaiters());
				return "/reservation/new/new-reservation";
			case "cancel":
				log.info("action = cancel");
				model.addAttribute("waitersList", getPersistedWaiters());
				return "/reservation/new/new-reservation";

		}
    	
    	return "error";
    }
    
    @PostMapping("/saveReservation")
    public String saveReservation(@Valid @ModelAttribute Reservation reservation,
    							BindingResult result, 
    							Model model,
    							SessionStatus status) {
        log.info("Entro in saveReservation");
    	if (result.hasErrors()) {
			model.addAttribute("waitersList", getPersistedWaiters());
            return "reservation/new/new-reservation";
        }
    	log.info("Saving Reservation and Customer objects...");
    	reservationService.save(reservation);
    	log.info("Reservation and Customer objects saved");

    	status.setComplete();
    	return "/reservation/reservations";
    }
    
    @PostMapping("/cancelReservation")
    public String cancelReservation(@ModelAttribute Reservation reservation,
									Model model,
									SessionStatus status) {
        log.info("Entro in cancelReservation");
        
        status.setComplete();

        return "reservation/reservations";
    }

    @PostMapping("/editReservationCustomer")
    public String editReservationCustomer(Model model,
    									@ModelAttribute("reservation") Reservation reservation,
    									@Valid @ModelAttribute("customer") Customer customer,
    									BindingResult result,
										@RequestParam("action") String action) {

    	log.info("Entro in editReservationCustomer");
    	Customer originalCustomer = null;
		List<Customer> reservation_customers = reservation.getReservation_customers();
		for(Customer c : reservation_customers) {
			if(c.getId() == -1) {
				originalCustomer = c;
				break;
			}
		}
    	
    	if (action.equals("cancel")) {
    		log.info("action = cancel");
    		originalCustomer.setId(0);
    		model.addAttribute("waitersList", getPersistedWaiters());
    		return "/reservation/new/new-reservation";
    	}else if(action.equals("save")) {
    		log.info("action = save");
    		if (result.hasErrors()) {
    			model.addAttribute("allergensList", allergenService.findAll());
    			model.addAttribute("customer", customer);
    			return "/reservation/new/new-reservation-edit-customer";
    		}
			reservation.getReservation_customers().remove(originalCustomer);
			model.addAttribute("waitersList", getPersistedWaiters());
    		reservation.addReservationCustomer(customer);
    		return "/reservation/new/new-reservation";
    	}
    	return "error";
    }   
    
    @PostMapping("/modifyReservationCustomer/{firstName}&{lastName}&{mobileNumber}")
    public String modifyReservationCustomer(Model model,
    										@PathVariable("firstName") String firstName,
    										@PathVariable("lastName") String lastName,
    										@PathVariable("mobileNumber") String mobileNumber,
    										@ModelAttribute("reservation") Reservation reservation,
    										@RequestParam("action") String action) {
    	log.info("Entro in modifyReservationCustomer");
    	//Trovo il customer
    	Customer originalCustomer = findCustomer(firstName, lastName, mobileNumber, reservation);


		if (action.equals("edit")) {
    		log.info("action = edit");
    		Customer cloneCustomer = (Customer) originalCustomer.clone();
    		originalCustomer.setId(-1);
        	model.addAttribute("customer", cloneCustomer);
    		model.addAttribute("allergensList", allergenService.findAll());
    		return "/reservation/new/new-reservation-edit-customer";
    	}
    	else if(action.equals("delete")) {
    		log.info("action = delete");
        	reservation.getReservation_customers().remove(originalCustomer);
			model.addAttribute("waitersList", getPersistedWaiters());
    		
    		return "/reservation/new/new-reservation";
    	}
    	
    	return "error";
    }
    
    @GetMapping("/showReservation/{id}")
    public String showReservationById(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        model.addAttribute("reservation", reservation);
        return "/reservation/new/edit-reservation";
    }
   
    @PostMapping("/updateReservation/{id}")
    public String updateReservation(@PathVariable Long id, @Valid @ModelAttribute Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/reservation/new/edit-reservation";
        }
        reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        reservationService.save(reservation);
        model.addAttribute("reservations", reservationService.findAll());
        return "/reservation/new/reservations";
    }


    
    //Helper function
    private Customer findCustomer(String firstName, String lastName, String mobileNumber, Reservation reservation) {
    	Customer found = null;	
		for(Customer c : reservation.getReservation_customers()) {
			if (c.getFirstName().equalsIgnoreCase(firstName) &&
				c.getLastName().equalsIgnoreCase(lastName) &&
				c.getMobileNumber().equals(mobileNumber)) {
				found = c;
			}
		}
		
		return found;
	}
    
	private List<Employee> getPersistedWaiters(){
		List<Employee> persistedEmployees = employeeService.findAll();
		persistedEmployees.removeIf(
			obj -> obj.getRole() != Employee.roleEnumType.Cameriere && obj.getRole() != Employee.roleEnumType.CapoSala
		);
		
		return persistedEmployees;
	}

	@ModelAttribute("reservation")
	public Reservation getReservation() {
		log.info("Adding new reservation to the model");
		return new Reservation();
	}

}









