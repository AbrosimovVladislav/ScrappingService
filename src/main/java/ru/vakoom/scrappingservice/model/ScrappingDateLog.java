package ru.vakoom.scrappingservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ScrappingDateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statId;
    private String shopName;
    private Date dateOfScrap;
    private Long timeOfScrapping;
    private int countOfRecords;
}
