package it.unimib.bdf.greenbook.repositories;

import it.unimib.bdf.greenbook.models.Reservation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long>{
	@Query(value="SELECT r.reservation_id, r.shift_enum, r.date"
			+ 	 " FROM  reservation_customers rc join reservation r on rc.reservation_id = r.reservation_id"
			+ 	 " WHERE rc.customer_id = :customer_id", nativeQuery=true)
	public List<Reservation> findAllReservationsByCustomerId(@Param("customer_id") Long customer_id);
	
	@Query(value="SELECT customer_id FROM reservation_customers rc where rc.reservation_id = :reservation_id", nativeQuery=true)
	public List<Long> findAllCustomersByReservationId(@Param("reservation_id") Long reservation_id);

}
