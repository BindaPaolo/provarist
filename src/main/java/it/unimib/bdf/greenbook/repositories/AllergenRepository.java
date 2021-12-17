package it.unimib.bdf.greenbook.repositories;

import it.unimib.bdf.greenbook.models.Allergen;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergenRepository extends CrudRepository<Allergen, Long> {

}