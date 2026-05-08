package com.estore.estorebackend.shopping;

import com.estore.estorebackend.catalog.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public CartItem() {}

    public CartItem(Long id, Cart cart, Product product, Integer quantity) {
        this.id = id;
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public Cart getCart() { return cart; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }

    public void setId(Long id) { this.id = id; }
    public void setCart(Cart cart) { this.cart = cart; }
    public void setProduct(Product product) { this.product = product; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public static CartItemBuilder builder() { return new CartItemBuilder(); }

    public static class CartItemBuilder {
        private Long id;
        private Cart cart;
        private Product product;
        private Integer quantity;

        public CartItemBuilder id(Long id) { this.id = id; return this; }
        public CartItemBuilder cart(Cart cart) { this.cart = cart; return this; }
        public CartItemBuilder product(Product product) { this.product = product; return this; }
        public CartItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }

        public CartItem build() { return new CartItem(id, cart, product, quantity); }
    }
}
