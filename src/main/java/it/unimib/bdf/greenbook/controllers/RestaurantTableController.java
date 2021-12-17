package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.RestaurantTable;
import it.unimib.bdf.greenbook.services.RestaurantTableService;
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

@Controller
public class RestaurantTableController {

    @Autowired
    private RestaurantTableService service;

    @GetMapping("/tables")
    public String showAllRestaurantTable(Model model) {
        model.addAttribute("tables", service.findAll());
        return "table/tables";
    }

    @GetMapping("/new-table")
    public String showNewRestaurantTableForm(Model model) {
        model.addAttribute("table", new RestaurantTable());
        return "table/new-table";
    }

    @PostMapping(value = "/addTable", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addNewRestaurantTable(@ModelAttribute RestaurantTable table, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "table/new-table";
        }
        service.save(table);
        model.addAttribute("tables", service.findAll());
        return "table/tables";
    }

    @GetMapping("/showTable/{id}")
    public String showRestaurantTableById(@PathVariable Long id, Model model) {
        RestaurantTable table = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid table Id:" + id));
        model.addAttribute("table", table);
        return "table/edit-table";
    }

    @PostMapping("/updateTable/{id}")
    public String updateRestaurantTable(@PathVariable Long id, @ModelAttribute RestaurantTable table, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "table/edit-table";
        }
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid table Id:" + id));
        service.save(table);
        model.addAttribute("tables", service.findAll());
        return "table/tables";
    }

    @PostMapping("/deleteTable/{id}")
    public String deleteRestaurantTable(@PathVariable Long id, Model model) {
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid table Id:" + id));
        service.deleteById(id);
        model.addAttribute("tables", service.findAll());
        return "table/tables";
    }
}
