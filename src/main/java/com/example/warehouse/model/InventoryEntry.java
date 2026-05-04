package com.example.warehouse.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_entries")
public class InventoryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int quantity;
    private String note;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public InventoryEntry() {
        this.date = LocalDate.now();
    }

    public InventoryEntry(Product product, Supplier supplier, int quantity, String note) {
        this.product = product;
        this.supplier = supplier;
        this.quantity = quantity;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
