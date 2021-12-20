package it.unimib.bdf.greenbook.repositories;

import it.unimib.bdf.greenbook.models.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	@Query("SELECT c FROM Customer c where c.person_id = 1")
	public List<Long> findAllCustomersByReservationId(Long reservation_id);
}
