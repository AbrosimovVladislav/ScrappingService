package ru.vakoom.scrappingservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vakoom.scrappingservice.model.Product;

import java.util.Optional;

@Repository
public interface HockeyRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByNameAndBrand(String name, String brand);

    default void saveOrUpdate(Product product) {
        this.findByNameAndBrand(product.name(), product.brand())
            .ifPresentOrElse(
                p -> this.save(product.id(p.id())),
                () -> this.save(product)
            );
    }
}
