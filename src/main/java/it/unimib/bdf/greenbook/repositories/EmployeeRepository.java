package it.unimib.bdf.greenbook.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.unimib.bdf.greenbook.models.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}