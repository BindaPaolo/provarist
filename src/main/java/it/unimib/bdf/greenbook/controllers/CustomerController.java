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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import it.unimib.bdf.greenbook.controllers.ReservationController;


import javax.validation.Valid;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private AllergenService allergenService;

    @GetMapping("/customers")
    public String showAllCustomers(Model model) {
        model.addAttribute("customers", service.findAll());
        return "customer/customers";
    }
    
    @GetMapping("/customer/new-customer")
    public String showNewCustomerForm(Model model) {
    	log.info("Entro in showNewCustomerForm");
    	
        model.addAttribute("customer", new Customer());

        // Retrieve persisted allergens list
        model.addAttribute("allergensList", allergenService.findAll());

        return "customer/new-customer";
    }
    
    @GetMapping("/customer/edit-customer")
    public String showEditCustomerForm(Model model) {
    	log.info("Entro in CustomerController.showEditCustomerForm");
        //model.addAttribute("allergensList", allergenService.findAll());
    	return "/customer/edit-customer";
    }
    
    @GetMapping("/reservation-customers/{reservation_id}")
    public String showReservationCustomers(@PathVariable Long reservation_id, Model model) {
    	model.addAttribute("customers", service.findAllCustomersByReservationId(reservation_id));
    	return "/customers";
    }

    @PostMapping("/addCustomer")
    public String addCustomer(@Valid @ModelAttribute Customer customer,
    						BindingResult result,
    			    		RedirectAttributes redirectAttributes,
    						Model model) {
    	
        if (result.hasErrors()) {
            model.addAttribute("allergensList", allergenService.findAll());
            return "customer/new-customer";
        }
        
    	redirectAttributes.addFlashAttribute("customer", customer);
    	log.info("About to redirect to new-reservation");
    	
    	
        return "redirect:/new-reservation";
    } 

    @PostMapping("/cancelCustomerInsertion")
    public String cancelCustomerInsertion(Model model) {
    	
    	return "redirect:/new-reservation"; 
    }
    
    @GetMapping("/showCustomer/{id}")
    public String showCustomerById(@PathVariable Long id, Model model) {
        Customer customer = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        model.addAttribute("customer", customer);
        model.addAttribute("allergensList", allergenService.findAll());

        return "customer/edit-customer";
    }

    @PostMapping("/updateCustomer/{id}")
    public String updateCustomer(@PathVariable Long id, @Valid @ModelAttribute Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allergensList", allergenService.findAll());
            return "customer/edit-customer";
        }
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        service.save(customer);
        model.addAttribute("customers", service.findAll());
        return "customer/customers";
    }

    @PostMapping("/deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable Long id, Model model) {
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        service.deleteById(id);
        model.addAttribute("customers", service.findAll());
        return "customer/customers";
    }

}
