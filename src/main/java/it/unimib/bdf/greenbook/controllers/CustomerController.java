package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.AllergenService;
import it.unimib.bdf.greenbook.services.CustomerService;
import it.unimib.bdf.greenbook.services.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/customer*")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private AllergenService allergenService;

    @Autowired
    private ReservationService reservationService;

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

        // Get the mobile number of the referral user
        String recommendedByMobileNumber = customer.getRecommendedBy().getMobileNumber();

        // Check for validation errors or data lacks in the database persistence
        if (checkForErrors(result, model, recommendedByMobileNumber))
            return "/customer/new-customer";

        // Check if the recommended-by field is left empty
        fixRecommendedByForeignKey(customer, recommendedByMobileNumber);

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

        // Get the mobile number of the referral user
        String recommendedByMobileNumber = customer.getRecommendedBy().getMobileNumber();

        // Check for validation errors or data lacks in the database persistence
        if (checkForErrors(result, model, recommendedByMobileNumber))
            return "/customer/edit-customer";

        // Check if the recommended-by field is left empty
        fixRecommendedByForeignKey(customer, recommendedByMobileNumber);

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

        // Set customer's ID to null in related reservations (so that they persist)
        cleanCustomerReservations(customerToBeDeleted);

        // Delete the persisted customer by id
        service.deleteById(id);

        model.addAttribute("customers", service.findAll());
        return "/customer/customers";
    }

    /**
     * There is an error if:
     * - there is some validation error
     * - the customer related to the mobile number given by the user is not persisted on the database (but only
     * if the mobile number field contains something, otherwise it doesn't get checked)
     *
     * @param result                    object that eventually contains validation errors
     * @param model                     set of attributes of the .jsp page shown to the user
     * @param recommendedByMobileNumber mobile number of the recommended-by customer
     * @return true if there is an error and some page needs to be shown to the user; false otherwise
     */
    private boolean checkForErrors(BindingResult result, Model model, String recommendedByMobileNumber) {

        // Check if the customer related to the mobile number given by the user is actually persisted on the database
        boolean recommendedByIsPersisted =
                !service.findAllCustomersByMobileNumber((recommendedByMobileNumber)).isEmpty();

        // IF -> presence of validation errors
        // OR IF -> user has entered some recommended-by mobile phone AND it is not persisted in the database
        if (result.hasErrors() || (!recommendedByMobileNumber.isEmpty() && !recommendedByIsPersisted)) {

            // If the user has given a referral mobile number but it's not persisted, show an error message
            if (!recommendedByMobileNumber.isEmpty() && !recommendedByIsPersisted) {
                model.addAttribute("recommendedByError",
                        "L'utente scelto come referreal non esiste! Verifica il numero di telefono.");
            }

            // Load persisted allergens list
            model.addAttribute("allergensList", allergenService.findAll());

            // Show again the page meant to insert/edit the customer's data
            return true;
        }

        return false;
    }

    /**
     * If the recommended by field is left empty by the user, make the RecommendedBy object null so that the foreign
     * key in the database is set to null
     *
     * @param customer                  the customer object
     * @param recommendedByMobileNumber mobile number of the recommended-by customer
     */
    private void fixRecommendedByForeignKey(Customer customer, String recommendedByMobileNumber) {
        if (recommendedByMobileNumber.isEmpty()) {
            // If the recommended by field is left empty by the user, make the RecommendedBy object null
            customer.setRecommendedBy(null);
        } else {
            // Fetch the customer in the database which has the mobile number given by the user
            customer.setRecommendedBy(service.findAllCustomersByMobileNumber(recommendedByMobileNumber).get(0));
        }
    }

    /**
     * Before deleting the customer, remove his reference from all the reservations that include him
     *
     * @param customerToBeDeleted the customer being removed from the database
     *
     */
    private void cleanCustomerReservations(Customer customerToBeDeleted) {
        List<Reservation> customerToBeDeletedReservations =
                reservationService.findAllReservationsByCustomerId(customerToBeDeleted.getId());

        for(Reservation reservation: customerToBeDeletedReservations){
            List<Customer> reservationCustomers = reservation.getReservation_customers();

            reservationCustomers.removeIf(
                    reservationCustomer -> reservationCustomer.getId() == customerToBeDeleted.getId());

            reservationService.save(reservation);
        }
    }

}
