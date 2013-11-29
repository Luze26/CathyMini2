/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The class {@link PurchaseLine} is an EJB Entity representing a couple ({@link Product}, quantity)
 * @author Kraiss
 */
@Entity(name="PurchaseLine")
@Table(name="PurchaseLine")
public class PurchaseLine implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;
    
    @Column(name="quantity")
    private Integer quantity;
    
    @OneToOne
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
