package it.unimib.bdf.greenbook.repositories;

import it.unimib.bdf.greenbook.models.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findAllEmployeeBycf(String cf);

}