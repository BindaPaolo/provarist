package it.unimib.bdf.greenbook.controllers;


import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Employee;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.EmployeeService;
import it.unimib.bdf.greenbook.services.ReservationService;
import it.unimib.bdf.greenbook.services.AllergenService;
import it.unimib.bdf.greenbook.services.CustomerService;

import javax.servlet.http.HttpSession;
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

import java.util.List;


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
    public String showNewReservationForm(Model model, HttpSession httpSession) {
    	//If the session is old, invalidate it
    	//and create a new one.
    	if(!httpSession.isNew()) {
    		httpSession.invalidate();
    	}
		model.addAttribute("reservation", new Reservation());
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

		switch (action) {
			case "show":
				//Show form for adding a new customer to the reservation
				model.addAttribute("customer", new Customer());
				model.addAttribute("allergensList", allergenService.findAll());
				return "/reservation/new/new-reservation-new-customer";
			case "add":
				//Check for errors in the customer form
				if(reservationCustomerCheckForErrors(result, model, customer)){
					return "/reservation/new/new-reservation-new-customer";
				}
				//If the object passes the tests,
				//add it to the reservation list.
				reservation.addReservationCustomer(customer);
				model.addAttribute("waitersList", getPersistedWaiters());
				return "/reservation/new/new-reservation";
			case "cancel":
				//Cancel customer insertion
				model.addAttribute("waitersList", getPersistedWaiters());
				return "/reservation/new/new-reservation";

		}
    	
    	return "error";
    }
    
    @PostMapping("/saveReservation")
    public String saveReservation(@Valid @ModelAttribute Reservation reservation,
    							BindingResult result, 
    							Model model) {
    	if (result.hasErrors()) {
			model.addAttribute("waitersList", getPersistedWaiters());
            return "reservation/new/new-reservation";
        }
    	//Persist the reservation to database.
    	reservationService.save(reservation);
    	
    	return "/reservation/reservations";
    }
    
    @PostMapping("/cancelReservation")
    public String cancelReservation(@ModelAttribute Reservation reservation,
									Model model,
									SessionStatus status) {
        //Reservation is being cancelled,
    	//we make sure to close the session.
        status.setComplete();

        return "reservation/reservations";
    }

    @PostMapping("/editReservationCustomer")
    public String editReservationCustomer(Model model,
    									@ModelAttribute("reservation") Reservation reservation,
    									@Valid @ModelAttribute("customer") Customer customer,
    									BindingResult result,
										@RequestParam("action") String action) {

    	//Set up a way to find the customer after
    	//passing to different view.
    	Customer originalCustomer = null;
		List<Customer> reservation_customers = reservation.getReservation_customers();
		for(Customer c : reservation_customers) {
			if(c.getId() == -1) {
				originalCustomer = c;
				break;
			}
		}
		//Remove the customer that's being
		//modified from the reservation's customer list.
		reservation_customers.remove(originalCustomer);

    	if (action.equals("cancel")) {
    		//Cancel means, we restore things as before
    		originalCustomer.setId(0);
    		//and re-add the original customer to the list
    		reservation_customers.add(originalCustomer);
    		
    		model.addAttribute("waitersList", getPersistedWaiters());
    		return "/reservation/new/new-reservation";
    	}else if(action.equals("save")) {
    		if (reservationCustomerCheckForErrors(result, model, customer)) {
    			reservation_customers.add(originalCustomer);
    			model.addAttribute("customer", customer);
    			return "/reservation/new/new-reservation-edit-customer";
    		}
    		//The original customer is no longer important.
    		//We can substitute it with the "new" one.
    		//This way of updating the customer is needed 
    		//because the reservation's customer list has not 
    		//been persisted yet.
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
    	//Trovo il customer
    	Customer originalCustomer = findCustomer(firstName, lastName, mobileNumber, reservation);

		if (action.equals("edit")) {
    		Customer cloneCustomer = (Customer) originalCustomer.clone();
    		//Setting up customer object recognition
    		//(might be needed).
    		originalCustomer.setId(-1);
        	model.addAttribute("customer", cloneCustomer);
    		model.addAttribute("allergensList", allergenService.findAll());
    		return "/reservation/new/new-reservation-edit-customer";
    	}
    	else if(action.equals("delete")) {
    		//Remove the customer from the reservation's customer list
        	reservation.getReservation_customers().remove(originalCustomer);
			model.addAttribute("waitersList", getPersistedWaiters());
    		
    		return "/reservation/new/new-reservation";
    	}
    	
    	return "error";
    }
    

    //Method to find a customer with given firstName, lastName and mobileNumber
    //in the reservation's customer list.
    public Customer findCustomer(String firstName, String lastName, String mobileNumber, Reservation reservation) {
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
    
    //Get the employees' with a particular role.
	private List<Employee> getPersistedWaiters(){
		List<Employee> persistedEmployees = employeeService.findAll();
		persistedEmployees.removeIf(
			obj -> obj.getRole() != Employee.roleEnumType.Cameriere && obj.getRole() != Employee.roleEnumType.CapoSala
		);
		
		return persistedEmployees;
	}
	
	
    /**
     * List of possible errors check for a customer to 
     * be inserted into the reservation's customer list.
     * 
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
    public boolean reservationCustomerCheckForErrors(BindingResult result, Model model, Customer customer) {

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
