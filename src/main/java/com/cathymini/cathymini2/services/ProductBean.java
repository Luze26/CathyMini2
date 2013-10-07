/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Product;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author uzely
 */
public class ProductBean {
    
    @PersistenceContext(unitName="ProductService") private EntityManager manager;
    
    public void addProduct() {
        Product prod = new Product();
        prod.setName("test");
        manager.persist(prod);   
    }
    
    public Collection<Product> getProducts() {
        Query query = manager.createQuery("SELECT p FROM Product p");
        return (Collection<Product>) query.getResultList();
    }
}
