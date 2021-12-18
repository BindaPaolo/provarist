package it.unimib.bdf.greenbook.repositories;

import it.unimib.bdf.greenbook.models.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long>{

}
