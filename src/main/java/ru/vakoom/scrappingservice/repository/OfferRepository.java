package ru.vakoom.scrappingservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vakoom.scrappingservice.model.Offer;

import java.util.Optional;

@Repository
public interface OfferRepository extends CrudRepository<Offer, Long> {

    Optional<Offer> findByNameAndBrand(String name, String brand);

    default void saveOrUpdate(Offer offer) {
        this.findByNameAndBrand(offer.name(), offer.brand())
            .ifPresentOrElse(
                p -> this.save(offer.id(p.id())),
                () -> this.save(offer)
            );
    }
}
