package it.unimib.bdf.assignment3.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unimib.bdf.assignment3.models.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
	List<Persona> findByEmail(String email);
}
