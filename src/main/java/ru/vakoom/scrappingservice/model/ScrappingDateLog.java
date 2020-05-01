package ru.vakoom.scrappingservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ScrappingDateLog {
    @Id
    @GeneratedValue
    private Long statId;
    private String shopName;
    private Date dateOfScrap;
    private Long timeOfScrapping;
}
