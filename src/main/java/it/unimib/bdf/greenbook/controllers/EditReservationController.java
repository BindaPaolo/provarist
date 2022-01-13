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

		if(!httpSession.isNew()) {
			httpSession.invalidate();
		}
		Reservation reservation = reservationService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));



		model.addAttribute("waitersList", getPersistedWaiters());		
		model.addAttribute("reservation", reservation);
		model.addAttribute("searchType", searchType);
		
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
    	
    	reservationService.save(reservation);

		getBackToSearchResults(model, httpSession);
		
		return "/reservation/search/search-results";
	}
	
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
					model.addAttribute("customer", new Customer());
					model.addAttribute("allergensList", allergenService.findAll());
					return "/reservation/edit/edit-reservation-new-customer";
			case "add":
					model.addAttribute("reservation", reservation);
					if(editReservationCustomerCheckForErrors(result, model, customer)){
						return "/reservation/edit/edit-reservation-new-customer";
					}
					reservation.addReservationCustomer(customer);
					model.addAttribute("waitersList", getPersistedWaiters());
					return "/reservation/edit/edit-reservation";
			case "cancel":
					model.addAttribute("reservation", reservation);
					model.addAttribute("waitersList", getPersistedWaiters());
					return "/reservation/edit/edit-reservation";
		}
		
		return "error";
	}
	
    @PostMapping("/editReservationEditCustomer")
    public String editReservationEditCustomer(Model model,
    									@ModelAttribute("originalCustomer") Customer originalCustomer,
    									@ModelAttribute("reservation") Reservation reservation,
    									@Valid @ModelAttribute Customer customer,
    									BindingResult result,
										@RequestParam("action") String action) {

    	if (action.equals("cancel")) {
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
    
	private List<Employee> getPersistedWaiters(){
		List<Employee> persistedEmployees = employeeService.findAll();
		persistedEmployees.removeIf(
			obj -> obj.getRole() != Employee.roleEnumType.Cameriere && obj.getRole() != Employee.roleEnumType.CapoSala
		);
		
		return persistedEmployees;
	}

}
