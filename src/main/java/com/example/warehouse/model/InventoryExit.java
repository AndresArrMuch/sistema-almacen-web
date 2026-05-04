package com.example.warehouse.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_exits")
public class InventoryExit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int quantity;
    private String destination;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public InventoryExit() {
        this.date = LocalDate.now();
    }

    public InventoryExit(Product product, int quantity, String destination) {
        this.product = product;
        this.quantity = quantity;
        this.destination = destination;
        this.date = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
