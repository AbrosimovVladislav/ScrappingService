package ru.vakoom.scrappingservice.model;

import lombok.Data;

import javax.persistence.*;

import static ru.vakoom.scrappingservice.model.Type.TYPE_TABLE;

@Data
@Entity
@Table(name = TYPE_TABLE)
public class Type {
    public static final String TYPE_TABLE = "type";
    public static final String TYPE_ID = "type_id";
    public static final String TYPE_UPPER = "upper";
    public static final String TYPE_MEDIUM = "medium";
    public static final String TYPE_LOWER = "lower";
    public static final String TYPE_SHOW_NAME = "show_name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TYPE_ID, length = 8, nullable = false)
    private Long typeId;

    @Column(name = TYPE_UPPER)
    private String upper;

    @Column(name = TYPE_MEDIUM)
    private String medium;

    @Column(name = TYPE_LOWER)
    private String lower;

    @Column(name = TYPE_SHOW_NAME)
    private String showName;
}
