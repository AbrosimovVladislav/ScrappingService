package ru.vakoom.scrappingservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vakoom.scrappingservice.model.ScrappingDateLog;

@Repository
public interface ScrappingDateLogRepository extends CrudRepository<ScrappingDateLog, Long> {
}
