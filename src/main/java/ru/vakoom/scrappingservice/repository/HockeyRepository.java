package ru.vakoom.scrappingservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vakoom.scrappingservice.model.Product;

@Repository
public interface HockeyRepository extends CrudRepository<Product, Long> {}
