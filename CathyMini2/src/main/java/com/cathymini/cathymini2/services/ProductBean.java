/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Napkin;
import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.model.Tampon;
import com.cathymini.cathymini2.webservices.model.ProductSearch;
import java.util.ArrayList;
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
        ID, NAME, MARQUE, FLUX, TYPE, PRICE
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
     * @param marque
     * @param flux
     * @param price
     * @param description
     * @return the product edited or null if the product doesn't exists
     */
    public Product editProduct(Long id, String name, String marque, Float flux, Float price, String description) {
        Product prod;
        try {
            prod = manager.find(Product.class, id);
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
            prod=null;
        }

        if (prod != null) {
            prod.setName(name);
            prod.setMarque(marque);
            prod.setFlux(flux);
            prod.setPrice(price);
            prod.setDescription(description);
            return prod;
        }
        return null;
    }
    
    public Collection<Product> getProducts(ProductSearch searchQuery) {
        searchQuery.validate();
        Query query = constructQuery(searchQuery);
        if (query != null) {
            return (Collection<Product>) query.getResultList();
        } else {
            return new ArrayList<Product>();
        }
    }
    
    public Product getProduct(Long id){
        Query query = manager.createNamedQuery("ProductById", Product.class);
        query.setParameter("id", id);
        if (query.getResultList().isEmpty()){
            return null; 
        }
          Product pr =  (Product) query.getResultList().get(0);
          return pr;
        //return (Product) query.getResultList().get(0);
    }
    
    public boolean delete(int id) {
        Query query = manager.createNamedQuery("deleteById", Product.class); 
        query.setParameter("id", id);
        return query.executeUpdate() == 1;
    }

    private Query constructQuery(ProductSearch searchQuery) {
        String query = "SELECT p FROM Product p WHERE";

        //INPUT
        if (searchQuery.input != null) {
            query += " p.name LIKE '%" + searchQuery.input.toUpperCase() + "%'";
        }

        //TAMPON & NAPKIN
        if (searchQuery.tampon) {
            query += " AND ( p.type = \"tampon\"";
            if(searchQuery.napkin) {
                query += " OR p.type = \"serviette\" )";
            }
            else {
                query += " )";
            }
        } else if (searchQuery.napkin) {
            query += " AND p.type = \"serviette\"";
        } else {
            return null;
        }

        //FLUX
        boolean first = true;
        if (searchQuery.flux != null && searchQuery.flux.size() > 0) {
            for (Float s : searchQuery.flux) {
                if (s != 0.0 && first) {
                    query += " AND ( p.flux = " + s;
                    first = false;
                } else if (s != 0.0) {
                    query += " OR  p.flux = " + s;
                }
            }
            if (!first) {
                query += " )";
            } else {
                query += " AND p.flux = " + -1.0;
            }
        }

        //BRANDS
        if (searchQuery.brands != null && searchQuery.brands.size() > 0) {
            query += " AND ";
            first = true;
            for (String brand : searchQuery.brands) {
                if (!first) {
                    query += " OR ";
                }
                first = false;
                query += "p.marque='" + brand.toUpperCase() + "'";
            }
        }

        //MIN PRICE
        if (searchQuery.minPrice != null) {
            query += " AND p.price >= " + searchQuery.minPrice;
        }

        //MAX PRICE
        if (searchQuery.maxPrice != null) {
            query += " AND p.price <= " + searchQuery.maxPrice;
        }

        //ORDER
        query += " ORDER BY p." + searchQuery.orderBy + " " + (searchQuery.orderByASC ? "ASC" : "DESC");

        return manager.createQuery(query).setFirstResult(searchQuery.offset).setMaxResults(searchQuery.length);
    }
}
