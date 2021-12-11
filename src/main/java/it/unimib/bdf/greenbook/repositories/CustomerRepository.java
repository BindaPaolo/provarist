package it.unimib.bdf.greenbook.repositories;

import it.unimib.bdf.greenbook.models.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
