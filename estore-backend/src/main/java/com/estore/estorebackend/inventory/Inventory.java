package com.estore.estorebackend.inventory;

import com.estore.estorebackend.catalog.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", unique = true)
    @JsonIgnore
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public Inventory() {}

    public Inventory(Long id, Product product, Integer quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }

    public void setId(Long id) { this.id = id; }
    public void setProduct(Product product) { this.product = product; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public static InventoryBuilder builder() { return new InventoryBuilder(); }

    public static class InventoryBuilder {
        private Long id;
        private Product product;
        private Integer quantity;

        public InventoryBuilder id(Long id) { this.id = id; return this; }
        public InventoryBuilder product(Product product) { this.product = product; return this; }
        public InventoryBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }

        public Inventory build() { return new Inventory(id, product, quantity); }
    }
}
