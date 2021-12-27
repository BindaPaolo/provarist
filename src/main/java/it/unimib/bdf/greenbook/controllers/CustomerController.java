package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.Allergen;
import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.services.AllergenService;
import it.unimib.bdf.greenbook.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/new-customer")
    public String showNewCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());

        // Retrieve persisted allergens list
        List<Allergen> persistedAllergens = allergenService.findAll();
        model.addAttribute("allergensList", persistedAllergens);

        return "customer/new-customer";
    }

    @PostMapping(value = "/addCustomer", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addNewCustomer(@Valid @ModelAttribute Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allergensList", getPersistedAllergens());
            return "customer/new-customer";
        }
        service.save(customer);
        model.addAttribute("customers", service.findAll());
        return "customer/customers";
    }

    @GetMapping("/showCustomer/{id}")
    public String showCustomerById(@PathVariable Long id, Model model) {
        Customer customer = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        model.addAttribute("customer", customer);
        model.addAttribute("allergensList", getPersistedAllergens());

        return "customer/edit-customer";
    }

    @PostMapping("/updateCustomer/{id}")
    public String updateCustomer(@PathVariable Long id, @Valid @ModelAttribute Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allergensList", getPersistedAllergens());
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

    private List<Allergen> getPersistedAllergens(){
        // Retrieve persisted allergens list
        return allergenService.findAll();
    }

}
