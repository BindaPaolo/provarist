package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.Allergen;
import it.unimib.bdf.greenbook.services.AllergenService;
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
public class AllergenController {

    @Autowired
    private AllergenService service;

    @GetMapping("/allergens")
    public String showAllergen(Model model) {
        model.addAttribute("allergens", service.findAll());
        return "allergen/allergens";
    }

    @GetMapping("/new-allergen")
    public String showNewAllergenForm(Model model) {
        model.addAttribute("allergen", new Allergen());
        return "allergen/new-allergen";
    }

    @PostMapping(value = "/addAllergen", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addNewAllergen(@ModelAttribute Allergen allergen, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "allergen/new-allergen";
        }
        service.save(allergen);
        model.addAttribute("allergens", service.findAll());
        return "allergen/allergens";
    }

    @GetMapping("/showAllergen/{id}")
    public String showAllergenById(@PathVariable Long id, Model model) {
        Allergen allergen = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid allergen Id:" + id));
        model.addAttribute("allergen", allergen);
        return "allergen/edit-allergen";
    }

    @PostMapping("/updateAllergen/{id}")
    public String updateAllergen(@PathVariable Long id, @ModelAttribute Allergen allergen, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "allergen/edit-allergen";
        }
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid allergen Id:" + id));
        service.save(allergen);
        model.addAttribute("allergens", service.findAll());
        return "allergen/allergens";
    }

    @PostMapping("/deleteAllergen/{id}")
    public String deleteAllergen(@PathVariable Long id, Model model) {
        service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid allergen Id:" + id));
        service.deleteById(id);
        model.addAttribute("allergens", service.findAll());
        return "allergen/allergens";
    }
}
