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
@Table(name = "invoice")
public class invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "type", nullable = false, length = 100)
    private String type;
    @Column(name = "date", nullable = false, length = 150)
    private LocalDate date;

    @ManyToMany
    @JoinTable(name = "invoice_detail",
            joinColumns = @JoinColumn(name = "customer_id"))
    private Set<invoice_detail> invoices = new LinkedHashSet<>();
}