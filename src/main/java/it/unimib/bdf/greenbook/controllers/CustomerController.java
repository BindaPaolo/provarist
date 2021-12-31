package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.Allergen;
import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.services.AllergenService;
import it.unimib.bdf.greenbook.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/customer*")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private AllergenService allergenService;

    @GetMapping("/customers")
    public String showAllCustomers(Model model) {
        model.addAttribute("customers", service.findAll());
        return "/customer/customers";
    }
    
    @GetMapping("/new-customer")
    public String showNewCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());

        // Retrieve persisted allergens list
        List<Allergen> persistedAllergens = allergenService.findAll();
        model.addAttribute("allergensList", persistedAllergens);

        return "/customer/new-customer";
    }
    
    @GetMapping("/edit-reservation-customer")
    public String showEditCustomerForm(Model model) {
    	log.info("Entro in CustomerController.showEditCustomerForm");
    	//Get the customer object
    	//that needs editing.
    	Customer customer = (Customer) model.getAttribute("customer");
    	log.info(customer.toString());
    	model.addAttribute("allergensList", allergenService.findAll());
        //model.addAttribute("allergensChecked", customer.getAllergies());
    	return "/customer/edit-reservation-customer";
    }
    
    @GetMapping("/reservation-customers/{reservation_id}")
    public String showReservationCustomers(@PathVariable Long reservation_id, Model model) {
    	model.addAttribute("customers", service.findAllCustomersByReservationId(reservation_id));
    	return "/customer/customers";
    }
    
    @PostMapping("/addCustomer")
    public String addNewCustomer(@Valid @ModelAttribute Customer customer, BindingResult result, Model model) {
        boolean recommendedByIsPersisted = isMobileNumberPersisted(customer.getRecommendedById().getMobileNumber());

        if (result.hasErrors() ||
                (!customer.getRecommendedById().getMobileNumber().isEmpty() && !recommendedByIsPersisted)) {

            if(!customer.getRecommendedById().getMobileNumber().isEmpty() && !recommendedByIsPersisted){
                model.addAttribute("recommendedByError",
                        "L'utente scelto come referreal non esiste! Verifica il numero di telefono.");
            }

            model.addAttribute("allergensList", allergenService.findAll());
            return "/customer/new-customer";
        }

        // If the recommended by field is left empty by the user, make the RecommendedBy object null
        if(customer.getRecommendedById().getMobileNumber().isEmpty()) {
            customer.setRecommendedById(null);
        } else {
            customer.setRecommendedById(service.findAllCustomersByMobileNumber(customer.getRecommendedById().getMobileNumber()).get(0));
        }

        service.save(customer);
        model.addAttribute("customers", service.findAll());

        return "/customer/customers";
    }


    @GetMapping("/showCustomer/{id}")
    public String showCustomerById(@PathVariable Long id, Model model) {
        Customer customer = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        model.addAttribute("customer", customer);
        model.addAttribute("allergensList", allergenService.findAll());

        return "/customer/edit-customer";
    }

    @PostMapping("/updateCustomer/{id}")
    public String updateCustomer(@PathVariable Long id, @Valid @ModelAttribute Customer customer, BindingResult result, Model model) {
        boolean recommendedByIsPersisted = isMobileNumberPersisted(customer.getRecommendedById().getMobileNumber());

        if (result.hasErrors() ||
                (!customer.getRecommendedById().getMobileNumber().isEmpty() && !recommendedByIsPersisted)) {
            model.addAttribute("allergensList", allergenService.findAll());

            if(!customer.getRecommendedById().getMobileNumber().isEmpty() && !recommendedByIsPersisted){
                model.addAttribute("recommendedByError",
                        "L'utente scelto come referreal non esiste! Verifica il numero di telefono.");
            }

            model.addAttribute("allergensList", allergenService.findAll());
            return "/customer/edit-customer";
        }

        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        // If the recommended by field is left empty by the user, make the RecommendedBy object null
        if(customer.getRecommendedById().getMobileNumber().isEmpty()) {
            customer.setRecommendedById(null);
        } else {
            customer.setRecommendedById(service.findAllCustomersByMobileNumber(customer.getRecommendedById().getMobileNumber()).get(0));
        }

        service.save(customer);
        model.addAttribute("customers", service.findAll());
        return "/customer/customers";
    }

    @PostMapping("/deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable Long id, Model model) {
    	log.info("Entro in deleteCustomer");
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        //changeRefferal(service.findById(id).get());
        service.deleteById(id);
        model.addAttribute("customers", service.findAll());
        return "/customer/customers";
    }

    // Checks if the user is already present, via mobile number
    private boolean isMobileNumberPersisted(String mobileNumber){
        return !service.findAllCustomersByMobileNumber(mobileNumber).isEmpty();
    }

}
