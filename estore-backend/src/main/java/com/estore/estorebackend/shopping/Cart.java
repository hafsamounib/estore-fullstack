package com.estore.estorebackend.shopping;

import com.estore.estorebackend.customer.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}

    public Cart(Long id, User user, List<CartItem> items) {
        this.id = id;
        this.user = user;
        this.items = items != null ? items : new ArrayList<>();
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public List<CartItem> getItems() { return items; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public Double getTotal() {
        return items.stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();
    }

    public static CartBuilder builder() { return new CartBuilder(); }

    public static class CartBuilder {
        private Long id;
        private User user;
        private List<CartItem> items = new ArrayList<>();

        public CartBuilder id(Long id) { this.id = id; return this; }
        public CartBuilder user(User user) { this.user = user; return this; }
        public CartBuilder items(List<CartItem> items) { this.items = items; return this; }

        public Cart build() { return new Cart(id, user, items); }
    }
}
