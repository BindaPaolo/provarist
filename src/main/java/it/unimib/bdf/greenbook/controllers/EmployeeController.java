package it.unimib.bdf.greenbook.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.unimib.bdf.greenbook.models.Employee;
import it.unimib.bdf.greenbook.services.EmployeeService;

@Controller
public class EmployeeController {
    
	@Autowired
    private EmployeeService service;
	
	@GetMapping("/employees")
    public String showAllEmployees(Model model) {
        model.addAttribute("employees", service.findAll());
        return "employees";
    }

	@GetMapping("/new-employee")
    public String showNewEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "new-employee";
    }
	
	@PostMapping(value = "/addEmployee", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addNewEmployee(@Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "new-employee";
        }
        service.save(employee);
        model.addAttribute("employees", service.findAll());
        return "employees";
    }
	
	@GetMapping("/showEmployee/{id}")
    public String showEmployeeById(@PathVariable Long id, Model model) {
        Employee employee = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        model.addAttribute("employee", employee);
        return "edit-employee";
    }

    @PostMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid @ModelAttribute Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "edit-employee";
        }
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        service.save(employee);
        model.addAttribute("employees", service.findAll());
        return "employees";
    }

    @PostMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        service.deleteById(id);
        model.addAttribute("employees", service.findAll());
        return "employees";
    }
}
