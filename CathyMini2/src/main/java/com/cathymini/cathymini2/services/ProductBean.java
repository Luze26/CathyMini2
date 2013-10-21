/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.model.Tampon;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author uzely
 */
@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ProductBean {

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(ProductBean.class);
        
    public Product addProduct(String name, Float price) {
        Product prod = new Tampon();
        prod.setName(name);
        prod.setPrice(price);
        manager.persist(prod);
        return prod;
    }

    public Collection<Product> getProducts(int offset, int length) {
        Query allQuery = manager.createQuery("SELECT p FROM Product p").setFirstResult(offset).setMaxResults(length);
        return (Collection<Product>) allQuery.getResultList();
    }
}
