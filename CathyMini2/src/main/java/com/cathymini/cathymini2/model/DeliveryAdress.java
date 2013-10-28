package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author kraiss
 */
@Entity
public class DeliveryAdress implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="ID")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name="userID")
    private Consumer consumer; // foreign key
    
    @Column(name="firstname")
    private String firstname;
    @Column(name="lastname")
    private String lastname;
    
    @Column(name="address")
    private String address;
    @Column(name="postalcode")
    private String postalCode;
    @Column(name="city")
    private String city;
    
    @Column(name="country")
    private String country;
    
    public DeliveryAdress() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
