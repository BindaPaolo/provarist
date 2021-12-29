package it.unimib.bdf.greenbook.repositories;

import it.unimib.bdf.greenbook.models.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	@Query(value="SELECT customer_id FROM reservation_customers rc where rc.reservation_id = :reservation_id", nativeQuery=true)
	public List<Long> findAllCustomersByReservationId(@Param("reservation_id") Long reservation_id);
	
	public List<Customer> findAllCustomersByFirstNameAndLastNameAllIgnoringCase(String firstName, String lastName);
}
