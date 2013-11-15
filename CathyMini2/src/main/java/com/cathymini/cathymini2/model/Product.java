package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Represent a product
 * @author uzely
 */
@Entity(name="Product")
@Table(name="Product")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="productType", discriminatorType=DiscriminatorType.STRING)
@NamedQueries({
    @NamedQuery(name="deleteById", query="DELETE FROM Product p WHERE p.id=:id")
})
public abstract class Product implements Serializable {

    public Product() {
    }

    public Product(String name, String type, Float price, Float flux, String description, String marque) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.flux = flux;
        this.description = description;
        this.marque = marque;
    }

    @Id
    @GeneratedValue
    @Column(name="ID")
    protected Long id;

    @Column(name="name",columnDefinition="LONG VARCHAR")
    protected String name;
    
    @Column(name="type")
    protected String type;
    
    @Column(name="marque")
    protected String marque;
    
    @Column(name="pictureUrl")
    protected String pictureUrl;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Float getFlux() {
        return flux;
    }

    public void setFlux(Float flux) {
        this.flux = flux;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="price")
    protected Float price;
    
    @Column(name="flux")
    protected Float flux;
    
    @Column(name="description",columnDefinition="LONG VARCHAR")
    protected String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Float getPrice() {
        return price;
    }
    
    public void setPrice(Float price) {
        this.price = price;
    }

    public String toString() {
        return "{id: " + id + ", name: " + name + "}";
    }
 
}
