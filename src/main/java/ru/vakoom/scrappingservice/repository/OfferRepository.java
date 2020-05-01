package ru.vakoom.scrappingservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vakoom.scrappingservice.model.Offer;

import java.util.Optional;

@Repository
public interface OfferRepository extends CrudRepository<Offer, Long> {

    Optional<Offer> findByNameAndBrandAndShopName(String name, String brand, String shopName);

    default void saveOrUpdate(Offer offer) {
        this.findByNameAndBrandAndShopName(offer.name(), offer.brand(), offer.shopName())
                .ifPresentOrElse(
                        p -> this.save(offer.id(p.id())),
                        () -> this.save(offer)
                );
    }
}
