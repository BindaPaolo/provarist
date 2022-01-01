package it.unimib.bdf.greenbook.controllers;


import it.unimib.bdf.greenbook.models.Customer;
import it.unimib.bdf.greenbook.models.Employee;
import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.services.AllergenService;
import it.unimib.bdf.greenbook.services.CustomerService;
import it.unimib.bdf.greenbook.services.EmployeeService;
import it.unimib.bdf.greenbook.services.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
@SessionAttributes({"reservation", "customerOriginal"})
@RequestMapping(value = "/reservation*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AllergenService allergenService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/reservations")
    public String showAllReservations(Model model) {
        return "/reservation/reservations";
    }

    @GetMapping("/new-reservation")
    public String showNewReservationForm(Model model) {
        log.info("Entro in new-reservation");

        if (model.getAttribute("reservation") == null) {
            log.info("Aggiungo nuova reservation al modello.");
            model.addAttribute("reservation", new Reservation());
        }

        // Show persisted waiters
        model.addAttribute("waitersList", getPersistedWaiters());

        return "/reservation/new-reservation";
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
                return "/reservation/new-reservation-customer";
            case "add":
                log.info("action = add");
                if (result.hasErrors()) {
                    model.addAttribute("allergensList", allergenService.findAll());
                    return "/reservation/new-reservation-customer";
                }
                reservation.addReservationCustomer(customer);
                model.addAttribute("waitersList", getPersistedWaiters());
                return "/reservation/new-reservation";
            case "cancel":
                log.info("action = cancel");
                model.addAttribute("waitersList", getPersistedWaiters());
                return "/reservation/new-reservation";

        }

        return "error";
    }

    @PostMapping("/saveReservation")
    public String saveReservation(@Valid @ModelAttribute Reservation reservation,
                                  BindingResult result,
                                  Model model,
                                  WebRequest request,
                                  SessionStatus status) {
        log.info("Entro in saveReservation");
        if (result.hasErrors()) {
            model.addAttribute("waitersList", getPersistedWaiters());
            return "reservation/new-reservation";
        }
        log.info("Saving Reservation and Customer objects...");
        AddCustomerRefferralByMobileNumber(reservation);
        reservationService.save(reservation);
        log.info("Reservation and Customer objects saved");
        endSessionAndRemoveReservationSessionAttribute(status, request, reservation);
        return "/reservation/reservations";
    }

    @PostMapping("/cancelReservation")
    public String cancelReservation(@ModelAttribute Reservation reservation,
                                    Model model,
                                    WebRequest request,
                                    SessionStatus status) {
        log.info("Entro in cancelReservation");

        endSessionAndRemoveReservationSessionAttribute(status, request, reservation);

        return "reservation/reservations";
    }

    @PostMapping("/editReservationCustomer")
    public String editReservationCustomer(Model model,
                                          @ModelAttribute("reservation") Reservation reservation,
                                          @ModelAttribute("customerOriginal") Customer customerOriginal,
                                          @Valid @ModelAttribute("customerMod") Customer customerMod,
                                          BindingResult result,
                                          @RequestParam("action") String action) {

        log.info("Entro in editReservationCustomer");

        if (action.equals("cancel")) {
            log.info("action = cancel");
            reservation.addReservationCustomer(customerOriginal);
            model.addAttribute("waitersList", getPersistedWaiters());
            return "/reservation/new-reservation";
        } else if (action.equals("save")) {
            log.info("action = save");
            if (result.hasErrors()) {
                model.addAttribute("allergensList", allergenService.findAll());
                return "/reservation/edit-reservation-customer";
            }
            model.addAttribute("waitersList", getPersistedWaiters());
            reservation.addReservationCustomer(customerMod);
            return "/reservation/new-reservation";
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
        Customer customerOriginal = findCustomer(firstName, lastName, mobileNumber, reservation);
        reservation.getReservation_customers().remove(customerOriginal);
        if (action.equals("edit")) {
            log.info("action = edit");
            Customer customerMod = (Customer) customerOriginal.clone();

            model.addAttribute("customerMod", customerMod);
            model.addAttribute("customerOriginal", customerOriginal);
            model.addAttribute("allergensList", allergenService.findAll());
            return "/reservation/edit-reservation-customer";
        } else if (action.equals("delete")) {
            log.info("action = delete");
            return "/reservation/new-reservation";
        }

        return "error";
    }

    @GetMapping("/showReservation/{id}")
    public String showReservationById(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        model.addAttribute("reservation", reservation);
        return "/reservation/edit-reservation";
    }

    @PostMapping("/updateReservation/{id}")
    public String updateReservation(@PathVariable Long id, @Valid @ModelAttribute Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/reservation/edit-reservation";
        }
        reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        reservationService.save(reservation);
        model.addAttribute("reservations", reservationService.findAll());
        return "/reservation/reservations";
    }

    @PostMapping("/deleteReservation/{id}")
    public String deleteReservation(@PathVariable Long id, Model model) {
        reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        reservationService.deleteById(id);
        model.addAttribute("reservations", reservationService.findAll());
        return "/reservation/reservations";
    }

    //Helper function
    private Customer findCustomer(String firstName, String lastName, String mobileNumber, Reservation reservation) {
        Customer found = null;
        for (Customer c : reservation.getReservation_customers()) {
            if (c.getFirstName().equalsIgnoreCase(firstName) &&
                    c.getLastName().equalsIgnoreCase(lastName) &&
                    c.getMobileNumber().equals(mobileNumber)) {
                found = c;
            }
        }

        return found;
    }

    //Helper function
    private void endSessionAndRemoveReservationSessionAttribute(SessionStatus status,
                                                                WebRequest request,
                                                                Reservation reservation) {
        log.info("Ending Session...");
        status.setComplete();
        log.info("Removing Reservation object from session");
        request.removeAttribute("reservation", WebRequest.SCOPE_SESSION);
    }

    private List<Employee> getPersistedWaiters() {
        List<Employee> persistedEmployees = employeeService.findAll();
        persistedEmployees.removeIf(
                obj -> obj.getRole() != Employee.roleEnumType.Cameriere && obj.getRole() != Employee.roleEnumType.CapoSala
        );

        return persistedEmployees;
    }

    private void AddCustomerRefferralByMobileNumber(Reservation reservation) {

        List<Customer> customer_all = customerService.findAll();
        for (Customer customer : reservation.getReservation_customers()) {

            boolean numberExist = false;
        /*
        Se il numero di telefono inserito dall'utente non è nullo viene ricercato, tra tutti i Customer, l'id associato
        al numero inserito e successivamente viene modificato il suo refferral
        Nel caso il numero inserito dall'utente fosse nullo viene aggiornata la variabile "recommendedBy" con il valore "null"
        */
            if (customer.getRecommendedBy() != null && !customer.getRecommendedBy().getMobileNumber().equalsIgnoreCase("")) {

                //Controller se il Customer è presente tra quelli inseriti nella prenotazione
                for (Customer c : reservation.getReservation_customers()) {
                    if (c.getMobileNumber().equalsIgnoreCase(customer.getRecommendedBy().getMobileNumber())) {

                        customer.setRecommendedBy(c);
                        numberExist = true;
                        break;
                    }
                }

                //Se non è ancora stato trovato continuo la ricerca, se no vado avanti
                if (!numberExist) {
                    //Controllo se il Customer è presente tra quelli già inseriti
                    for (Customer c : customer_all) {
                        if (c.getMobileNumber().equalsIgnoreCase(customer.getRecommendedBy().getMobileNumber())) {

                            customer.setRecommendedBy(c);
                            numberExist = true;
                            break;
                        }
                    }
                }

            } else {

                customer.setRecommendedBy(null);
            }

            //Se il numero di telefono inserito dall'utente non è stato trovato la raccomandazione non esiste e di conseguenza viene salvata come nulla
            if (!numberExist)
                customer.setRecommendedBy(null);
        }

        //log.info("\n\n\n\n\n DENTRO FOR: " + reservation.getReservation_customers().toString() + "\n\n\n\n\n");
    }

}
