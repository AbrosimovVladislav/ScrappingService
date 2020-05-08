package ru.vakoom.scrappingservice.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = Brand.BRAND_TABLE)
public class Brand {
    public static final String BRAND_TABLE = "brand";
    public static final String BRAND_ID = "brand_id";
    public static final String BRAND_SHORT_NAME = "shortName";
    public static final String BRAND_FULL_NAME = "fullName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = BRAND_ID, length = 8, nullable = false)
    private Long brandId;

    @Column(name = BRAND_SHORT_NAME, unique = true)
    private String shortName;

    @Column(name = BRAND_FULL_NAME)
    private String fullName;
}
