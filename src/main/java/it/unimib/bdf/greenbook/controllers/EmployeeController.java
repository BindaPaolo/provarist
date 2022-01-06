package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.Employee;
import it.unimib.bdf.greenbook.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping("/employees")
    public String showAllEmployees(Model model) {
        model.addAttribute("employees", service.findAll());
        return "employee/employees";
    }

    @GetMapping("/new-employee")
    public String showNewEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee/new-employee";
    }

    @PostMapping(value = "/addEmployee", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addNewEmployee(@Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
        // Check for validation errors

        if (checkForErrors(result, model, employee, true))
            return "/employee/new-employee";


        service.save(employee);

        model.addAttribute("employees", service.findAll());
        return "employee/employees";
    }

    @GetMapping("/showEmployee/{id}")
    public String showEmployeeById(@PathVariable Long id, Model model) {
        // Check if the employee is actually persisted in the database, otherwise show an error page
        Employee employee =
                service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

        model.addAttribute("employee", employee);
        return "employee/edit-employee";
    }

    @PostMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
        // Check if the employee is actually persisted in the database, otherwise show an error page
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

        if (checkForErrors(result, model, employee, false))
            return "employee/edit-employee";

        service.save(employee);
        model.addAttribute("employees", service.findAll());
        return "employee/employees";
    }

    @PostMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        // Check if the employee is actually persisted in the database, otherwise show an error page
        service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

        try {
            service.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("dataIntegrityError", "Impossibile eliminare il dipendente con ID " +
                    service.findById(id).get().getId() + ": verifica che non faccia parte di una prenotazione.");
        }

        model.addAttribute("employees", service.findAll());
        return "employee/employees";
    }

    /**
     * There is an error if:
     * - there is some validation error
     * - the employee related to the cf given by the user is not persisted on the database (but only
     * if the cf field contains something, otherwise it doesn't get checked)
     *
     * @param result       object that eventually contains validation errors
     * @param model        set of attributes of the .jsp page shown to the user
     * @param employee     object of the employee that the user is inserting/editing
     * @param insertAction defines if the current action is an insert (1) or an update (0)
     * @return true if there is an error and some page needs to be shown to the user; false otherwise
     */
    private boolean checkForErrors(BindingResult result, Model model, Employee employee, boolean insertAction) {

        // Flag = presence of errors
        boolean errorPresence = false;

        // Check if the cf of the employee is already persisted in the database
        boolean CFAlreadyPersisted = isCFPersisted(employee.getCf());

        // IF -> presence of validation errors
        if (result.hasErrors())
            errorPresence = true;

        // IF -> a employee with the same cf is already present in the database
        if (CFAlreadyPersisted) {
            String duplicatedError = "Il codice fiscale è già registrato per un altro utente.";

            if (insertAction) {
                model.addAttribute("CfError", duplicatedError);
                errorPresence = true;
            } else {
                if (checkForCFDuplicates(employee.getId(), employee.getCf())) {
                    model.addAttribute("CfError", duplicatedError);
                    errorPresence = true;
                }
            }
        }

        // Show again the page meant to insert/edit the customer's data
        return errorPresence;
    }

    @PostMapping("/cancelEmployeeOp")
    public String cancelOperation(Model model) {
        model.addAttribute("employees", service.findAll());
        return "employee/employees";
    }

    /**
     * Checks that the cf is stored in the database (in this case, the user is inserting a duplicate)
     *
     * @param cf cf of the customer that the user wants to insert
     */
    private boolean isCFPersisted(String cf) {
        return !service.findAllEmployeeByCF(cf).isEmpty();
    }

    /**
     * Checks for cf duplicates
     *
     * @param cf cf of the customer that the user wants to update
     */
    private boolean checkForCFDuplicates(Long id, String cf) {
        List<Employee> employeeList = service.findAllEmployeeByCF(cf);
        boolean alreadyPresent = false;

        for (Employee e : employeeList) {
            if (e.getId() != id) {
                alreadyPresent = true;
                break;
            }
        }

        return alreadyPresent;
    }

}
