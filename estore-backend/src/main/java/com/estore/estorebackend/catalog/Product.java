package com.estore.estorebackend.catalog;

import com.estore.estorebackend.inventory.Inventory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"products", "hibernateLazyInitializer"})
    private Category category;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"product", "hibernateLazyInitializer"})
    private Inventory inventory;

    public Product() {}

    public Product(Long id, String name, String description, Double price,
                   String imageUrl, Category category, Inventory inventory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.inventory = inventory;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public Category getCategory() { return category; }
    public Inventory getInventory() { return inventory; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategory(Category category) { this.category = category; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    public static ProductBuilder builder() { return new ProductBuilder(); }

    public static class ProductBuilder {
        private Long id;
        private String name;
        private String description;
        private Double price;
        private String imageUrl;
        private Category category;
        private Inventory inventory;

        public ProductBuilder id(Long id) { this.id = id; return this; }
        public ProductBuilder name(String name) { this.name = name; return this; }
        public ProductBuilder description(String description) { this.description = description; return this; }
        public ProductBuilder price(Double price) { this.price = price; return this; }
        public ProductBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public ProductBuilder category(Category category) { this.category = category; return this; }
        public ProductBuilder inventory(Inventory inventory) { this.inventory = inventory; return this; }

        public Product build() {
            return new Product(id, name, description, price, imageUrl, category, inventory);
        }
    }
}
