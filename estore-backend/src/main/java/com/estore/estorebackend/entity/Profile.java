package com.estore.estorebackend.entity;

import com.estore.estorebackend.customer.User;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"user"})
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String address;
    private String city;
    private String country;

    @JsonIgnoreProperties({"password", "profile", "hibernateLazyInitializer", "handler"})
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Profile() {}

    public Profile(Long id, String phone, String address, String city, String country, User user) {
        this.id = id;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.user = user;
    }

    // Getters
    public Long getId() { return id; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public User getUser() { return user; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
    public void setUser(User user) { this.user = user; }

    // Builder
    public static ProfileBuilder builder() { return new ProfileBuilder(); }

    public static class ProfileBuilder {
        private Long id;
        private String phone;
        private String address;
        private String city;
        private String country;
        private User user;

        public ProfileBuilder id(Long id) { this.id = id; return this; }
        public ProfileBuilder phone(String phone) { this.phone = phone; return this; }
        public ProfileBuilder address(String address) { this.address = address; return this; }
        public ProfileBuilder city(String city) { this.city = city; return this; }
        public ProfileBuilder country(String country) { this.country = country; return this; }
        public ProfileBuilder user(User user) { this.user = user; return this; }

        public Profile build() {
            return new Profile(id, phone, address, city, country, user);
        }
    }
}