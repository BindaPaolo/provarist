package it.unimib.bdf.greenbook.controllers;


import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import it.unimib.bdf.greenbook.containers.ReservationListContainer;
import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Employee;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.ReservationService;
import it.unimib.bdf.greenbook.services.AllergenService;
import it.unimib.bdf.greenbook.services.CustomerService;
import it.unimib.bdf.greenbook.services.EmployeeService;


@Controller
@SessionAttributes({"reservation", "searchType", "date", "firstName", "lastName", "originalCustomer"})
@RequestMapping("/reservation/edit*")
public class EditReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private AllergenService allergenService;
	
	@PostMapping("/editReservation/{id}")
	public String editReservation(@PathVariable Long id,
								@RequestParam("searchType") String searchType,
								@RequestParam(value = "firstName", required = false) String firstName,
								@RequestParam(value = "lastName", required = false) String lastName,
								@RequestParam(value = "date", required = false) String date,
								HttpSession httpSession,
								Model model) {
		//Make sure we are in a new session
		if(!httpSession.isNew()) {
			httpSession.invalidate();
		}
		//Retrieve the selected reservation object
		Reservation reservation = reservationService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));



		model.addAttribute("waitersList", getPersistedWaiters());		
		model.addAttribute("reservation", reservation);
		model.addAttribute("searchType", searchType);
		
		//Differentiate model attributes based on 
		//the search parameters.
		if(searchType.equals("byDate")) {
			model.addAttribute("date", LocalDate.parse(date));
		}
		else {
			model.addAttribute("firstName", firstName);
			model.addAttribute("lastName", lastName);
		}

		return "/reservation/edit/edit-reservation";
	}
	
	@PostMapping("/editReservationSave")
	public String editReservationSave(Model model,
									@Valid @ModelAttribute("reservation") Reservation reservation,
	    							BindingResult result,
									HttpSession httpSession) {

    	if (result.hasErrors()) {
			model.addAttribute("waitersList", getPersistedWaiters());
            return "reservation/edit/edit-reservation";
        }
    	//Update the reservation
    	reservationService.save(reservation);
    	//Go back to the previous page, updating search results.
		getBackToSearchResults(model, httpSession);
		
		return "/reservation/search/search-results";
	}
	
	//Once finished editing (either through a save or cancel)
	//we go back to the previous jsp, search-results.jsp, with the 
	//updated reservation list (possibly modified through
	//the editing actions).
	private void getBackToSearchResults(Model model, HttpSession httpSession) {
		
		String searchType = (String) httpSession.getAttribute("searchType");
		ReservationListContainer reservationListContainer = new ReservationListContainer();
		model.addAttribute("searchType", searchType);
		
		if(searchType.equals("byDate")) {
			LocalDate date = (LocalDate) httpSession.getAttribute("date");
			model.addAttribute("date", date);
			reservationListContainer.setReservations(reservationService.findAllReservationsByDate(date));
		}
		else {
			String firstName =  (String) httpSession.getAttribute("firstName");
			String lastName = (String) httpSession.getAttribute("lastName");
			model.addAttribute("firstName", firstName);
			model.addAttribute("lastName", lastName);
			reservationListContainer.setReservations(reservationService.findAllReservationByCustomerFirstNameAndLastName(firstName, lastName));
		}
		
		model.addAttribute("reservationListContainer", reservationListContainer);
	}
	
	//Cancel the reservation edit process
	@PostMapping("/editReservationCancel")
	public String editReservationCancel(Model model,
										HttpSession httpSession) {
		
		getBackToSearchResults(model, httpSession);
		
		return "/reservation/search/search-results";
	}
	
	@PostMapping("/editReservationNewCustomer")
	public String editReservationNewCustomer(Model model,
											@ModelAttribute("reservation") Reservation reservation,
											@Valid @ModelAttribute("customer") Customer customer,
											BindingResult result,
											@RequestParam("action") String action) {
		
		switch (action) {
			case "show":
					//Show form for adding a new customer to the reservation.
					model.addAttribute("customer", new Customer());
					model.addAttribute("allergensList", allergenService.findAll());
					return "/reservation/edit/edit-reservation-new-customer";
			case "add":
					//Adding new customer to the reservation.
					model.addAttribute("reservation", reservation);
					//Check for errors in new customer form.
					if(editReservationCustomerCheckForErrors(result, model, customer)){
						return "/reservation/edit/edit-reservation-new-customer";
					}
					//If there's no errors, add it to the reservation's list.
					reservation.addReservationCustomer(customer);
					model.addAttribute("waitersList", getPersistedWaiters());
					return "/reservation/edit/edit-reservation";
			case "cancel":
					//Cancel the addition of a new customer to the reservation.
					model.addAttribute("reservation", reservation);
					model.addAttribute("waitersList", getPersistedWaiters());
					return "/reservation/edit/edit-reservation";
		}
		
		return "error";
	}
	
	//Method that handles the editing process of
	//a customer selected from the reservation's customer list.
	//It's similar to what happens in NewReservationController
	//but the big difference is that now the reservation 
	//and all its customers are already persisted in the database.
    @PostMapping("/editReservationEditCustomer")
    public String editReservationEditCustomer(Model model,
    									@ModelAttribute("originalCustomer") Customer originalCustomer,
    									@ModelAttribute("reservation") Reservation reservation,
    									@Valid @ModelAttribute Customer customer,
    									BindingResult result,
										@RequestParam("action") String action) {
    	
    	if (action.equals("cancel")) {
    		//We had previously removed the customer 
    		//selected for editing from the reservation's customer list.
    		//Since we are undoing this process, we must add it back again.
    		reservation.getReservation_customers().add(originalCustomer);
    		model.addAttribute("waitersList", getPersistedWaiters());
    		model.addAttribute("reservation", reservation);
    		return "/reservation/edit/edit-reservation";
    	}else if(action.equals("save")) {
    		if (editReservationCustomerCheckForErrors(result, model, customer)) {
    			model.addAttribute("customer", customer);
    			return "/reservation/edit/edit-reservation-edit-customer";
    		}
    		// The original customer might have had already been inserted into the db
    		customer.setId(originalCustomer.getId());
    		reservation.addReservationCustomer(customer);
    		
    		model.addAttribute("reservation", reservation);
			model.addAttribute("waitersList", getPersistedWaiters());
    		return "/reservation/edit/edit-reservation";
    	}
    	return "error";
    }   
	
    //Method that finds the customer selected from
    //the reservation's customer list and either handles 
    //It's deletion or forwards the editing process to
    //a certain view which will be handled by another
    //method of this controller's (for better code
    //readability and separation of concerns).
    @PostMapping("/editReservationModifyCustomer/{firstName}&{lastName}&{mobileNumber}")
    public String editReservationModifyCustomer(Model model,
    										@PathVariable("firstName") String firstName,
    										@PathVariable("lastName") String lastName,
    										@PathVariable("mobileNumber") String mobileNumber,
    										@ModelAttribute("reservation") Reservation reservation,
    										@RequestParam("action") String action) {
    	//Trovo il customer
    	Customer originalCustomer = findCustomer(firstName, lastName, mobileNumber, reservation);
    	model.addAttribute("originalCustomer", originalCustomer);
    	reservation.getReservation_customers().remove(originalCustomer);
    	
		if (action.equals("edit")) {
        	model.addAttribute("customer", originalCustomer);
    		model.addAttribute("allergensList", allergenService.findAll());
    		return "/reservation/edit/edit-reservation-edit-customer";
    	}
    	else if(action.equals("delete")) {
			model.addAttribute("waitersList", getPersistedWaiters());
    		
    		return "/reservation/edit/edit-reservation";
    	}
    	
    	return "error";
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
    private boolean editReservationCustomerCheckForErrors(BindingResult result, Model model, Customer customer) {
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
        	if (c.getMobileNumber().equals(customer.getMobileNumber()) ){
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

}
