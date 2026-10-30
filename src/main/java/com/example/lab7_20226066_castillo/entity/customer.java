package com.example.lab7_20226066_castillo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "customer")
public class customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "document", nullable = false, length = 150)
    private String document;
    @Column(name = "document_type", nullable = false, length = 150)
    private String document_type;
}