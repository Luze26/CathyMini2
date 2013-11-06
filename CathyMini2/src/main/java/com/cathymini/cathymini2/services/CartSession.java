/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Cart;
import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.Product;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Kraiss
 */
@Stateless
public class CartSession implements CartSessionItf {
    private Cart cart;

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(CartSession.class);
    
    /**
     * Add a product to the Cart
     */
    @Override
    public String addProduct(Product p) throws Exception {
        return "Not Implemented";
    }
    
    /**
     * Remove a product from the cart
     */
    @Override
    public String subProduct(Product p) throws Exception {
        return "Not Implemented";
    }
    
    /**
     * Get a cart sessionBean of an user.
     */
    @Override
    public String getUserCart(Consumer consumer) throws Exception {
        return "Not Implemented";
    }
    
    /**
     * Empty the cart.
     */
    @Remove
    @Override
    public String clear() {
        return "Not Implemented";
    }
}
