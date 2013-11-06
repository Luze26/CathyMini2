/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.Product;
import javax.ejb.Remove;
import javax.ejb.Remote;

/**
 *
 * @author Kraiss
 */
@Remote
public interface CartSessionItf {
    
    /**
     * Add a product to the Cart
     * @param p Product to add to the cart
     */
    public String addProduct(Product p) throws Exception;
    
    /**
     * Remove a product from the cart
     * @param p Product to remove from the cart
     */
    public String subProduct(Product p) throws Exception;
    
    /**
     * Get a cart sessionBean of an user.
     * @param consumer The target user
     */
    public String getUserCart(Consumer consumer) throws Exception;
    
    /**
     * Empty the cart.
     */
    @Remove
    public String clear();
}
