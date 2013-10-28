/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.model.Tampon;
import com.cathymini.cathymini2.webservices.model.ProductSearch;
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

    public enum ProductKeys {
        ID, NAME, PRICE
    }
    
    private static final Logger logger = Logger.getLogger(ProductBean.class);
        
    public Product addProduct(String name, Float price) {
        Product prod = new Tampon();
        prod.setName(name);
        prod.setPrice(price);
        manager.persist(prod);
        return prod;
    }

    public Product editProduct(Long id, String name, Float price) {
        logger.debug("llla)" + id);
        Product prod;
        try {
        prod = manager.find(Product.class, id);
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
                    prod=null;
        }
        logger.debug(prod);
        if(prod != null) {
            prod.setName(name);
            prod.setPrice(price);
            return prod;
        }
        return null;
    }
    
    public Collection<Product> getProducts(ProductSearch searchQuery) {
        searchQuery.validate();
        Query query = manager.createQuery("SELECT p FROM Product p WHERE p.name LIKE :searchString ORDER BY p." + searchQuery.orderBy + (searchQuery.orderByASC ? " ASC" : " DESC"))
                    .setFirstResult(searchQuery.offset).setMaxResults(searchQuery.length).setParameter("searchString", "%" + searchQuery.input + "%");
        return (Collection<Product>) query.getResultList();
    }
    
    public boolean delete(int id) {
        Query query = manager.createNamedQuery("deleteById", Product.class); 
        query.setParameter("id", id);
        return query.executeUpdate() == 1;
    }
}
