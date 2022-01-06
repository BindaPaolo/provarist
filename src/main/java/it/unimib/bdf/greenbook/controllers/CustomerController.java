package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.services.AllergenService;
import it.unimib.bdf.greenbook.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping(value = "/customer*")
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

        // Load persisted allergens list
        model.addAttribute("allergensList", allergenService.findAll());

        return "/customer/new-customer";
    }

    @GetMapping("/edit-reservation-customer")
    public String showEditCustomerForm(Model model) {
        log.info("Entro in CustomerController.showEditCustomerForm");

        // Load persisted allergens list
        model.addAttribute("allergensList", allergenService.findAll());

        return "/customer/edit-reservation-customer";
    }

    @GetMapping("/reservation-customers/{reservation_id}")
    public String showReservationCustomers(@PathVariable Long reservation_id, Model model) {
        model.addAttribute("customers", service.findAllCustomersByReservationId(reservation_id));
        return "/customer/customers";
    }

    @PostMapping("/addCustomer")
    public String addNewCustomer(@Valid @ModelAttribute Customer customer, BindingResult result, Model model) {

        // Check for validation errors or data lacks in the database persistence
        if (checkForErrors(result, model, customer, true))
            return "/customer/new-customer";

        // Persist customer's data
        service.save(customer);

        // Show all customers' data in customers list
        model.addAttribute("customers", service.findAll());
        return "/customer/customers";
    }


    @GetMapping("/showCustomer/{id}")
    public String showCustomerById(@PathVariable Long id, Model model) {

        // Check if the customer's id exists, otherwise throw an exception and show error page
        Customer customer = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        model.addAttribute("customer", customer);

        // Load persisted allergens list
        model.addAttribute("allergensList", allergenService.findAll());

        // Show the page meant to edit the customer's data
        return "/customer/edit-customer";
    }

    @PostMapping("/updateCustomer/{id}")
    public String updateCustomer(@PathVariable Long id, @Valid @ModelAttribute Customer customer, BindingResult result, Model model) {

        // Check if the customer's id exists, otherwise throw an exception and show error page
        service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

        // Check for validation errors or data lacks in the database persistence
        if (checkForErrors(result, model, customer, false))
            return "/customer/edit-customer";

        // Persist updated customer's data
        service.save(customer);

        // Show all customers' data in customers list
        model.addAttribute("customers", service.findAll());
        return "/customer/customers";
    }

    @PostMapping("/deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable Long id, Model model) {
        log.info("Entro in deleteCustomer");

        // Check if the customer's id exists, otherwise throw an exception and show error page
        Customer customerToBeDeleted = service.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid customer Id:" + id));

        // Set recommended-by field to null before removing the record (so that the recommended-by customer persists)
        customerToBeDeleted.setRecommendedBy(null);

        // Delete the persisted customer by id
        try {
            service.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("dataIntegrityError", "Impossibile eliminare il cliente con ID " +
                    customerToBeDeleted.getId() + ": verifica che non faccia parte di una prenotazione.");
        }

        model.addAttribute("customers", service.findAll());
        return "/customer/customers";
    }

    @PostMapping("/cancelCustomerOp")
    public String cancelOperation(Model model) {
        model.addAttribute("customers", service.findAll());
        return "/customer/customers";
    }

    /**
     * There is an error if:
     * - there is some validation error
     * - the customer related to the mobile number given by the user is not persisted on the database (but only
     * if the mobile number field contains something, otherwise it doesn't get checked)
     * - customer has entered himself as referral
     * - the customer chose a mobile number which is already persisted and related to a different customer
     *
     * @param result       object that eventually contains validation errors
     * @param model        set of attributes of the .jsp page shown to the user
     * @param customer     object of the customer that the user is inserting/editing
     * @param insertAction defines if the current action is an insert (1) or an update (0)
     * @return true if there is an error and some page needs to be shown to the user; false otherwise
     */
    private boolean checkForErrors(BindingResult result, Model model, Customer customer, boolean insertAction) {

        // Flag errorPresence = presence of errors
        boolean errorPresence = false;

        // Check if the mobile number of the customer is already persisted in the database
        boolean mobileNumberAlreadyPersisted = service.isMobileNumberPersisted(customer.getMobileNumber());

        // Check if the customer related to the mobile number given by the user is actually persisted on the database
        String recommendedByMobileNumber = customer.getRecommendedBy().getMobileNumber();
        boolean recommendedByIsPersisted = service.isMobileNumberPersisted(recommendedByMobileNumber);

        // IF -> there are some validation errors
        if (result.hasErrors())
            errorPresence = true;

        // IF -> a customer with the same mobile number is already present in the database
        if (mobileNumberAlreadyPersisted) {
            String duplicatedError = "Il numero di telefono è già registrato per un altro cliente.";

            if (insertAction) {
                model.addAttribute("mobileNumberError", duplicatedError);
                errorPresence = true;
            } else {
                if (service.checkForMobileNumberDuplicates(customer.getId(), customer.getMobileNumber())) {
                    model.addAttribute("mobileNumberError", duplicatedError);
                    errorPresence = true;
                }
            }
        }

        // IF -> user has entered some recommended-by mobile phone AND it is not persisted in the database
        if (!recommendedByMobileNumber.isEmpty() && !recommendedByIsPersisted) {
            model.addAttribute("recommendedByError",
                    "Il cliente scelto come referreal non esiste! Verifica il numero di telefono.");
            errorPresence = true;
        }

        // IF -> user has entered himself as referral
        if (!recommendedByMobileNumber.isEmpty() && recommendedByMobileNumber.equals(customer.getMobileNumber())) {
            model.addAttribute("recommendedByError",
                    "Il cliente scelto come referreal non può coincidere con il cliente stesso!");
            errorPresence = true;
        }

        // If any of the above errors is present, show again page new/edit-customer
        if (errorPresence) {
            // Load persisted allergens list
            model.addAttribute("allergensList", allergenService.findAll());

            // Show again the page meant to insert/edit the customer's data
            return true;
        }

        return false;
    }


}
