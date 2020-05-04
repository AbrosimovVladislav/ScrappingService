package ru.vakoom.scrappingservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vakoom.scrappingservice.model.Type;

public interface TypeRepository extends CrudRepository<Type, Long> {
}
