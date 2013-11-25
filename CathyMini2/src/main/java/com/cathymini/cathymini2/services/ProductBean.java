/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Cart;
import com.cathymini.cathymini2.model.Napkin;
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
 * Product's services
 *
 * @author uzely
 */
@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ProductBean {

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(ProductBean.class);

    /**
     * Properties of a product (used to order by)
     */
    public enum ProductKeys {
        ID, NAME, TYPE, PRICE
    }

    /**
     * Add a product to the database
     *
     * @param name
     * @param marque
     * @param flux
     * @param price
     * @param type
     * @param description
     * @return the product
     */
    public Product addProduct(String name, String marque, Float flux, Float price, String type, String description) {
        Product prod;
        if(type.equals(new String("Tampon")))
            prod = new Tampon();
        else
            prod = new Napkin();
        prod.setName(name);
        prod.setMarque(marque);
        prod.setFlux(flux);
        prod.setPrice(price);
        prod.setType(type);
        prod.setDescription(description);
        manager.persist(prod);
        logger.info("New product : " + prod);
        return prod;
    }
    
    public Product addProduct(Product prod) {
        manager.persist(prod);
        logger.info("New product : " + prod);
        return prod;
    }

    /**
     * Edit the product
     *
     * @param id
     * @param name
     * @param price
     * @return the product edited or null if the product doesn't exists
     */
    public Product editProduct(Long id, String name, Float price) {
        Product prod;
        try {
            prod = manager.find(Product.class, id);
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
            prod=null;
        }

        if (prod != null) {
            prod.setName(name);
            prod.setPrice(price);
            return prod;
        }
        return null;
    }
    
    public Collection<Product> getProducts(ProductSearch searchQuery) {
        searchQuery.validate();
        Query query = constructQuery(searchQuery);
        return (Collection<Product>) query.getResultList();
    }
    
    public Product getProduct(Long id){
        System.out.println("Dans getProduct");
        System.out.println("id produit to add : "+id);
        Query query = manager.createNamedQuery("ProductById", Product.class);
        
        query.setParameter("id", id);
        
        if (query.getResultList().isEmpty()){
            System.out.println("dans productbean c'est vide");
            return null; 
        }
            
        return (Product) query.getResultList().get(0);
    }
    
    public boolean delete(int id) {
        Query query = manager.createNamedQuery("deleteById", Product.class); 
        query.setParameter("id", id);
        return query.executeUpdate() == 1;
    }

    private Query constructQuery(ProductSearch searchQuery) {
        String query = "SELECT p FROM Product p WHERE p.name";
        if (searchQuery.input != null) {
            query += " LIKE '%" + searchQuery.input + "%'";
        }
        if (searchQuery.tampon) {
            System.out.println("TAMPON");
            query += " AND ( p.type = \"tampon\"";
            if(searchQuery.napkin) {
                System.out.println("SERVIETTE2");
                query += " OR p.type = \"serviette\" )";
            }
            else {
                query += " )";
            }
        } else if (searchQuery.napkin) {
            System.out.println("SERVIETTE");
            query += " AND p.type = \"serviette\"";
        } else {
            System.out.println("RIEN");
            query = "SELECT p FROM Product p WHERE p.name = \"Rien\"";
            return manager.createQuery(query).setFirstResult(searchQuery.offset).setMaxResults(searchQuery.length);
        }
        for(String s : searchQuery.flux){
            if(Float.parseFloat(s) != 0.0) {
                query += " AND p.flux = "+s;
            }
        }
        if (searchQuery.brand != null) {
            query += " AND p.marque LIKE '%" + searchQuery.brand + "%'";
        }
        
        if (searchQuery.minPrice != null) {
            System.out.println("MINPRIX");
            query += " AND p.price >= " + searchQuery.minPrice;
        }

        if (searchQuery.maxPrice != null) {
            System.out.println("MAXPRIX");
            query += " AND p.price <= " + searchQuery.maxPrice;
        }

        query += " ORDER BY p." + searchQuery.orderBy + " " + (searchQuery.orderByASC ? "ASC" : "DESC");

        return manager.createQuery(query).setFirstResult(searchQuery.offset).setMaxResults(searchQuery.length);
    }
}
