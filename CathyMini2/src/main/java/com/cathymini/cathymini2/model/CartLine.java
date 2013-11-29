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
 *
 * @author anaelle
 */
@Entity(name="CartLine")
@Table(name="CartLine")

public class CartLine implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="cartLineID")
    /** Integer primary Key */
    private Long cartLineID;
    
    @OneToOne
    private Product product;
    
    @Column(name="quantity")
    private Integer quantity;
    
    public CartLine(){
        
    }
    
    public CartLine(Product prod, int qu){
        this.product = prod;
        this.quantity = qu;
    }
    
    public Long getCartLineID() {
        return cartLineID;
    }

    public void setCartLineID(Long cartLineID) {
        this.cartLineID = cartLineID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
}
