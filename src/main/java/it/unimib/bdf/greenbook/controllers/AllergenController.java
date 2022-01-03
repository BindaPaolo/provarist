package it.unimib.bdf.greenbook.controllers;

import it.unimib.bdf.greenbook.models.Allergen;
import it.unimib.bdf.greenbook.services.AllergenService;
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
        // If there are some validation errors:
        if (result.hasErrors()) {
            return "allergen/new-allergen";
        }

        service.save(allergen);

        model.addAttribute("allergens", service.findAll());
        return "allergen/allergens";
    }

    @GetMapping("/showAllergen/{id}")
    public String showAllergenById(@PathVariable Long id, Model model) {
        // Check if the allergen is actually persisted in the database, otherwise show an error page
        Allergen allergen = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid allergen Id:" + id));

        model.addAttribute("allergen", allergen);
        return "allergen/edit-allergen";
    }

    @PostMapping("/updateAllergen/{id}")
    public String updateAllergen(@PathVariable Long id, @ModelAttribute Allergen allergen, BindingResult result, Model model) {
        // Check if the allergen is actually persisted in the database, otherwise show an error page
        service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid allergen Id:" + id));

        // If there are some validation errors:
        if (result.hasErrors()) {
            return "allergen/edit-allergen";
        }

        service.save(allergen);

        model.addAttribute("allergens", service.findAll());
        return "allergen/allergens";
    }

    @PostMapping("/deleteAllergen/{id}")
    public String deleteAllergen(@PathVariable Long id, Model model) {
        // Check if the allergen is actually persisted in the database, otherwise show an error page
        service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid allergen Id:" + id));

        //service.deleteById(id);

        try {
            service.deleteById(id);
        } catch(DataIntegrityViolationException e) {
            model.addAttribute("dataIntegrityError", "Impossibile eliminare l'allergene \"" +
                   service.findById(id).get().getName() + "\" : verifica che non sia associato a nessun cliente.");
        }

        model.addAttribute("allergens", service.findAll());
        return "allergen/allergens";
    }
}
