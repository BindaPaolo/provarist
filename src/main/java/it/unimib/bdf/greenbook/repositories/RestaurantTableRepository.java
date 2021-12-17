package it.unimib.bdf.greenbook.repositories;

import it.unimib.bdf.greenbook.models.RestaurantTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTableRepository extends CrudRepository<RestaurantTable, Long> {

}