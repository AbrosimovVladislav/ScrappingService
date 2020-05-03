package ru.vakoom.scrappingservice.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private Boolean inStore;
    private String category;
    private String shopName;
    private String link;
}
