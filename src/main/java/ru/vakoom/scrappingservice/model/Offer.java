package ru.vakoom.scrappingservice.model;

import lombok.Data;

import javax.persistence.*;

import static ru.vakoom.scrappingservice.model.Type.TYPE_ID;

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
    @ManyToOne
    @JoinColumn(name = TYPE_ID, nullable = false)
    private Type type;
    private String shopName;
    @Column(length = 1023)
    private String link;
    private String age;
}
