package it.unimib.bdf.greenbook.services;

import it.unimib.bdf.greenbook.models.Reservation;
import it.unimib.bdf.greenbook.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReservationService{

    private ReservationRepository repository;

    @Autowired
    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public List<Reservation> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Reservation> findById(Long id) {
        return repository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}