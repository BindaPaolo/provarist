package it.unimib.bdf.greenbook.controllers;


import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Employee;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.EmployeeService;
import it.unimib.bdf.greenbook.services.ReservationService;
import it.unimib.bdf.greenbook.services.AllergenService;
import it.unimib.bdf.greenbook.services.CustomerService;
import it.unimib.bdf.greenbook.controllers.CustomerController;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogDelegateFactory;
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
	
	@Autowired
	private CustomerService customerService;
	
	
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
				if(newReservationCustomerCheckForErrors(result, model, customer)){
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
    		log.info("\n\nEntro in result.hasW\n\n");
			model.addAttribute("waitersList", getPersistedWaiters());
            return "reservation/new/new-reservation";
        }
    	log.info(reservation.toString());
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
		reservation_customers.remove(originalCustomer);

    	if (action.equals("cancel")) {
    		log.info("action = cancel");
    		
    		originalCustomer.setId(0);
    		reservation_customers.add(originalCustomer);
    		
    		model.addAttribute("waitersList", getPersistedWaiters());
    		return "/reservation/new/new-reservation";
    	}else if(action.equals("save")) {
    		log.info("action = save");
    		if (newReservationCustomerCheckForErrors(result, model, customer)) {
    			reservation_customers.add(originalCustomer);
    			model.addAttribute("customer", customer);
    			return "/reservation/new/new-reservation-edit-customer";
    		}
    		
    		reservation.addReservationCustomer(customer);
    		
			model.addAttribute("waitersList", getPersistedWaiters());
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
    

    /**
     * Adds new Reservation object to the model.
     * This object then remains in the session
     * until its completion.
     **/
	@ModelAttribute("reservation")
	public Reservation getReservation() {
		log.info("Adding new reservation to the model");
		return new Reservation();
	}
      
	
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
	
	
    /**
     * 1 result errors:
     * 		a) firstName or lastName or mobileNumber not present or the
     * 		   mobileNumber field doesn't contain a non zero number of digits.
     * 
     * 2 mobileNumber errors:
     * 		a) mobileNumber is equal to another customer already 
     * 		   present in db but they differ for either firstName or lastName.
     * 		b) in the reservation_customers list there's already another
     * 		   customer with same mobileNumber.
     * 		c) mobileNumber is equal to recommendedBy.
     * 
     * 3 recommendedBy errors:
     * 		a) recommendedBy.getMobileNumber() is not empty but the referenced 
     *  	   mobileNumber doesn't belong to a customer in the db.
     * 
     * @param result                    object that eventually contains validation errors
     * @param model                     set of attributes of the .jsp page shown to the user
     * @param customer                  object of the customer that the user is inserting/editing
     * @return true if there is an error and some page needs to be shown to the user; false otherwise
     */
    private boolean newReservationCustomerCheckForErrors(BindingResult result, Model model, Customer customer) {

        // Flag = presence of errors
        boolean errorPresence = false;
        // Side (but important) note: customer.getRecommendedBy() 
        // returns a customer object with all fields set to null except (maybe)
        // the mobileNumber field.
        Customer recommendedByCustomer  = customer.getRecommendedBy();
        String recommendedByMobileNumber = recommendedByCustomer.getMobileNumber(); 
        boolean recommendedByIsPersisted = !customerService.findAllCustomersByMobileNumber(recommendedByMobileNumber).isEmpty();

        
        if (result.hasErrors()) {
            // firstName or lastName or mobileNumber are empty
            // 1a)
            errorPresence = true;
        }
    	
        if(!customerService.findAllCustomersByMobileNumber(customer.getMobileNumber()).isEmpty()) {
        	Customer existingCustomer = customerService.findAllCustomersByMobileNumber(customer.getMobileNumber()).get(0);
	        if(existingCustomer != null) {
	        	// There is another customer with 
	        	// the same mobile number saved in the db,
	        	if(!existingCustomer.getFirstName().equalsIgnoreCase(customer.getFirstName()) ||
	        			!existingCustomer.getLastName().equalsIgnoreCase(customer.getLastName())) {
	        		// but with different first and/or last name
	        		// 2a)
	            	String mobileNumberError = "Un cliente con questo numero di telefono esiste già ma ha nome e/o cognome differenti!";
	                model.addAttribute("mobileNumberError", mobileNumberError);
	            	errorPresence = true;        			
	        		
	        	}
	        }
        }
        
        for(Customer c : ((Reservation) model.getAttribute("reservation")).getReservation_customers()) {
        	if (c.getMobileNumber().equals(customer.getMobileNumber()) && !(c == customer)){
        		// Another customer, already in the reservation_customers' list,
        		// has the same mobile number.
        		// 2b)
        		String mobileNumberError = "Il numero di telefono appartiene ad un altro cliente nella prenotazione!";
        		model.addAttribute("mobileNumberError", mobileNumberError);
        		errorPresence = true;
        	}
        }
        
        if(!recommendedByMobileNumber.isEmpty()) {
        	
        	if(!recommendedByIsPersisted) {
            	// RecommendedBy field not empty but 
            	// refererenced customer doesn't exist.
        		// 3a)
	        	String recommendedByError = "Non esiste un cliente con questo numero di telefono nel sistema!";
	            model.addAttribute("recommendedByError", recommendedByError);
	        	errorPresence = true;
        	}
        	
        	if(recommendedByMobileNumber.equals(customer.getMobileNumber())){
        		// Self loop relationship on the same entity
        		// 2c)
        		String mobileNumberError = "Il cliente scelto come referreal non "
        								+ "può coincidere con il cliente stesso!";
        		model.addAttribute("mobileNumberError", mobileNumberError);
        		errorPresence = true;
        	}
        }
        
        if(errorPresence) {
            // Load persisted allergens list
            model.addAttribute("allergensList", allergenService.findAll());
            return true;
        }

        return false;
    }

}
