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
@Table(name = "invoice_detail")
public class invoice_detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "quantity", nullable = false, length = 100)
    private String quantity;
    @Column(name = "price", nullable = false, length = 150)
    private Double price;
    @Column(name = "subtotal", nullable = false, length = 150)
    private Double subtotal;

    @ManyToMany
    @JoinTable(name = "invoice_detail",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<invoice_detail> invoices = new LinkedHashSet<>();
}