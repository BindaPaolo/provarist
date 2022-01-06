package it.unimib.bdf.greenbook.services;

import it.unimib.bdf.greenbook.models.Allergen;
import it.unimib.bdf.greenbook.repositories.AllergenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AllergenService {

    private AllergenRepository repository;

    @Autowired
    public AllergenService(AllergenRepository repository) {
        this.repository = repository;
    }

    public List<Allergen> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Allergen> findById(Long id) {
        return repository.findById(id);
    }

    public Allergen save(Allergen allergen) {
        return repository.save(allergen);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /**
     * Checks if the allergen is already present in the database
     *
     * @param allergenName name of the allergen that the user wants to add to allergens list
     */
    public boolean isAllergenPersisted(String allergenName) {
        return !repository.findAllAllergensByNameAllIgnoringCase(allergenName).isEmpty();
    }

}
