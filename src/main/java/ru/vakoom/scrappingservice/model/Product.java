package ru.vakoom.scrappingservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Accessors(fluent = true)
@Entity
public class Product {
    @Id @GeneratedValue private Long id;

    private String name;
    private String brand;
    private String price;
    private String link;
    private String imgLink;

}
