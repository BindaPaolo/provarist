package it.unimib.bdf.greenbook.controllers;


import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Employee;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.repositories.CustomerRepository;
import it.unimib.bdf.greenbook.services.CustomerService;
import it.unimib.bdf.greenbook.services.EmployeeService;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@Controller
@SessionAttributes("reservation")
public class ReservationController {
	
	@Autowired
	private ReservationService service;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CustomerService customerService;

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

		// Show persisted waiters
		model.addAttribute("waitersList", getPersistedWaiters());

		return "reservation/new-reservation";
    }
    
    
    @PostMapping("/reservation/addCustomerToReservation")
    public String addCustomerToReservation(Model model, @ModelAttribute("reservation") Reservation reservation) {
    	log.info("Entro in addCustomerToReservation");
    	return "redirect:/customer/new-reservation-customer";
    }
    

    @PostMapping("/saveReservation")
    public String saveReservation(@Valid @ModelAttribute Reservation reservation,
    							BindingResult result, 
    							Model model,
    							WebRequest request,
    							SessionStatus status) {
        log.info("Entro in saveReservation");

    	if (result.hasErrors()) {
			model.addAttribute("waitersList", getPersistedWaiters());
            return "reservation/new-reservation";
        }
    	log.info("Saving Reservation and Customer objects...");

		AddCustomerRefferralByMobileNumber(reservation);
		log.info("finito");

		//log.info(reservation.getReservation_customers().toString());
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

    @PostMapping("/deleteReservationCustomer/{firstName}&{lastName}&{mobileNumber}")
    public String deleteReservationCustomer(@PathVariable("firstName") String firstName,
    						    				@PathVariable("lastName") String lastName,
    						    				@PathVariable("mobileNumber") String mobileNumber,
    						    				@ModelAttribute Reservation reservation,
    						    				Model model){
    	//Reservation reservation = (Reservation) model.getAttribute("reservation");
    	removeCustomer(firstName, lastName, mobileNumber, reservation);
    	
    	log.info("\n\n\n\n"+ reservation+"\n\n\n\n");
    	
    	return "reservation/new-reservation";
    }
    
    
    //edit = remove + add new
    @PostMapping("/editReservationCustomer/{firstName}&{lastName}&{mobileNumber}")
    public String editReservationCustomer(@PathVariable("firstName") String firstName,
    						    		@PathVariable("lastName") String lastName,
    						    		@PathVariable("mobileNumber") String mobileNumber,
    						    		@ModelAttribute Reservation reservation,
    						    		RedirectAttributes redirectAttributes,
    						    		Model model){
    	if(model.getAttribute("reservation") == null) {
    		log.info(reservation.toString());
    	}
    	removeCustomer(firstName, lastName, mobileNumber, reservation);

    	Customer customer = new Customer();
    	customer.setFirstName(firstName);
    	customer.setLastName(lastName);
    	customer.setMobileNumber(mobileNumber);
    	
    	redirectAttributes.addFlashAttribute("customer", customer);
    	
    	return "redirect:/customer/edit-reservation-customer";
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
    }

	private List<Employee> getPersistedWaiters(){
		List<Employee> persistedEmployees = employeeService.findAll();
		persistedEmployees.removeIf(
			obj -> obj.getRole() != Employee.roleEnumType.Cameriere && obj.getRole() != Employee.roleEnumType.CapoSala
		);

		return persistedEmployees;
	}

	private void AddCustomerRefferralByMobileNumber(Reservation reservation) {

		List<Customer> customer_all = customerService.findAll();
		for(Customer customer : reservation.getReservation_customers()) {

			boolean numberExist = false;
        /*
        Se il numero di telefono inserito dall'utente non è nullo viene ricercato, tra tutti i Customer, l'id associato
        al numero inserito e successivamente viene modificato il suo refferral
        Nel caso il numero inserito dall'utente fosse nullo viene aggiornata la variabile "recommendedById" con il valore "null"
        */
			if (customer.getRecommendedById() != null && !customer.getRecommendedById().getMobileNumber().equalsIgnoreCase("")) {

				//Controller se il Customer è presente tra quelli inseriti nella prenotazione
				for (Customer c : reservation.getReservation_customers()) {
					if (c.getMobileNumber().equalsIgnoreCase(customer.getRecommendedById().getMobileNumber())) {

						customer.setRecommendedById(c);
						numberExist = true;
						break;
					}
				}

				//Se non è ancora stato trovato continuo la ricerca, se no vado avanti
				if(!numberExist) {
					//Controllo se il Customer è presente tra quelli già inseriti
					for (Customer c : customer_all) {
						if (c.getMobileNumber().equalsIgnoreCase(customer.getRecommendedById().getMobileNumber())) {

							customer.setRecommendedById(c);
							numberExist = true;
							break;
						}
					}
				}

			} else {

				customer.setRecommendedById(null);
			}

			//Se il numero di telefono inserito dall'utente non è stato trovato la raccomandazione non esiste e di conseguenza viene salvata come nulla
			if(!numberExist)
				customer.setRecommendedById(null);
		}

		//log.info("\n\n\n\n\n DENTRO FOR: " + reservation.getReservation_customers().toString() + "\n\n\n\n\n");
	}

}
