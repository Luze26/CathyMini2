/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Product;
import java.util.Collection;
import java.util.Enumeration;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;
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

    public void addProduct() {
        Product prod = new Product();
        prod.setName("test");
        manager.persist(prod);
        logger.info("Product added");
    }

    public Collection<Product> getProducts() {
        Query query = manager.createQuery("SELECT p FROM Product p");
        return (Collection<Product>) query.getResultList();
    }
}
