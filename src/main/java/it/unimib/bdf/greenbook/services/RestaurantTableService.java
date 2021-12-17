package it.unimib.bdf.greenbook.services;

import it.unimib.bdf.greenbook.models.RestaurantTable;
import it.unimib.bdf.greenbook.repositories.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RestaurantTableService {

    private RestaurantTableRepository repository;

    @Autowired
    public RestaurantTableService(RestaurantTableRepository repository) {
        this.repository = repository;
    }

    public List<RestaurantTable> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<RestaurantTable> findById(Long id) {
        return repository.findById(id);
    }

    public RestaurantTable save(RestaurantTable table) {
        return repository.save(table);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
