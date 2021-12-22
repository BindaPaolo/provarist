package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Reservation;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping("/customers")
    public String showAllCustomers(Model model) {
        model.addAttribute("customers", service.findAll());
        return "customer/customers";
    }

    /*
    @GetMapping("/customer/new-customer")
    public String showNewCustomerForm(
    		@ModelAttribute("reservation") Reservation reservation,
    		Model model) {
    	Customer c = new Customer();
    	reservation.add_reservation_customer(c);
        model.addAttribute("customer", c);
        return "customer/new-customer";
    }*/
    
    @GetMapping("/customer/new-customer")
    public String showNewCustomerForm(
    		@ModelAttribute("reservation") Reservation reservation,
    		Model model) {
    	//Customer c = new Customer();
    	//reservation.add_reservation_customer(c);
        model.addAttribute("reservation", reservation);
        model.addAttribute("customer", new Customer());
        return "customer/new-customer";
    }
    
    @GetMapping("/customer/reservation-customers/{reservation_id}")
    public String showReservationCustomers(@PathVariable Long reservation_id, Model model) {
    	model.addAttribute("customers", service.findAllCustomersByReservationId(reservation_id));
    	return "customer/customers";
    }

    @PostMapping(value = "/addCustomer", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addCustomer(@Valid @ModelAttribute Customer customer,
    						@Valid @ModelAttribute("reservation") Reservation reservation,
    						BindingResult result,
    						RedirectAttributes redirectAttributes,
    						Model model) {
        if (result.hasErrors()) {
            return "customer/new-customer";
        }
        reservation.add_reservation_customer(customer);
        redirectAttributes.addFlashAttribute("reservation", reservation);
        /*Reservation res = new Reservation();
        List<Customer> c = new ArrayList<>();
        c.add(addedCustomer);
        res.setReservation_customers(c);
        model.addAttribute("reservation", res);*/
        return "redirect:/reservation/new-reservation";
    }

    @GetMapping("/showCustomer/{id}")
    public String showCustomerById(@PathVariable Long id, Model model) {
        Customer customer = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "customer/edit-customer";
    }

    @PostMapping("/updateCustomer/{id}")
    public String updateCustomer(@PathVariable Long id, @Valid @ModelAttribute Customer customer, BindingResult result, Model model) {
        if (result.hasErrors()) {
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
