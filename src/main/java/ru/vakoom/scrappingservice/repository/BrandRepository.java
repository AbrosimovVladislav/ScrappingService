package ru.vakoom.scrappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vakoom.scrappingservice.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
