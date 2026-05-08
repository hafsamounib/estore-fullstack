package com.estore.estorebackend.dto;

public class ProfileDTO {

    private String phone;
    private String address;
    private String city;
    private String country;

    public ProfileDTO() {}

    public ProfileDTO(String phone, String address, String city, String country) {
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    // Getters
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getCountry() { return country; }

    // Setters
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
}