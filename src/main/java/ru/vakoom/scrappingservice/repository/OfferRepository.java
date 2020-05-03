package ru.vakoom.scrappingservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vakoom.scrappingservice.model.Offer;

@Repository
public interface OfferRepository extends CrudRepository<Offer, Long> {
}
