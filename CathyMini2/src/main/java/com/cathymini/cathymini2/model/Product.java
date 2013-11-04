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
@NamedQueries({
    @NamedQuery(name="deleteById", query="DELETE FROM Product p WHERE p.id=:id")
})
@Table(name="Product")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="productType", discriminatorType=DiscriminatorType.STRING)
public abstract class Product implements Serializable {

    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Column(name="name")
    private String name;
    
    @Column(name="price")
    private Float price;
    
    public Long getId() {
        return id;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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