package com.example.warehouse.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private int quantity;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private String status;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public Product() {}

    public Product(String name, Category category, int quantity, BigDecimal costPrice, BigDecimal salePrice, Supplier supplier) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.supplier = supplier;
        updateStatus();
    }

    @PrePersist
    @PreUpdate
    public void updateStatus() {
        if (quantity <= 0) {
            status = "Agotado";
        } else if (quantity < 5) {
            status = "Stock bajo";
        } else {
            status = "En stock";
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateStatus();
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getStatus() {
        return status;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
