package ru.vakoom.scrappingservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Product {

    private String name;
    private String brand;
    private String price;
    private String link;
    private String imgLink;

}
