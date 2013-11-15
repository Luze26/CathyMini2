package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The class {@link DeliveryAddress} is an EJB Entity representing a {@link Consumer} {@link DeliveryAddress}.
 * @author kraiss
 */
@Entity(name="DeliveryAddress")
@Table(name="DeliveryAddress")
public class DeliveryAddress implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="deliveryAddresID")
    /** Integer primary key */
    private Long deliveryAddresID;
    
    @ManyToOne
    /** Owner of the {@link DeliveryAddress} */
    private Consumer consumer;
    
    @Column(name="firstname")
    private String firstname;
    @Column(name="lastname")
    private String lastname;
    
    @Column(name="address")
    private String address;
    @Column(name="zipcode")
    private String zipCode;
    @Column(name="city")
    private String city;
    
    @Column(name="country")
    private String country;

    public Long getDeliveryAddresID() {
        return deliveryAddresID;
    }

    public void setDeliveryAddresID(Long deliveryAddresID) {
        this.deliveryAddresID = deliveryAddresID;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String postalCode) {
        this.zipCode = postalCode;
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
