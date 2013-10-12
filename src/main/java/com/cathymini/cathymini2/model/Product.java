package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represent a product
 * @author uzely
 */
@Entity(name="Product")
@Table(name="Product")
public class Product implements Serializable {
    
    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;

    @Column(name="name")
    private String name;
    
    public Long getId() {
        return id;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
