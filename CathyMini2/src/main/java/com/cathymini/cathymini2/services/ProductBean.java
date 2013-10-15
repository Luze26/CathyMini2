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
@ManagedBean(name="productBean")
@Stateless
@TransactionManagement(value=TransactionManagementType.CONTAINER)
public class ProductBean {
    
    @PersistenceContext(unitName="com.cathymini_CathyMini2_PU") private EntityManager manager;
    
    private static final Logger logger = Logger.getLogger(ProductBean.class);
    
    public void addProduct() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String param = request.getParameter("addProduct");
        logger.debug(param);
        Enumeration<String> elems = request.getParameterNames();
        while(elems.hasMoreElements()) {
            logger.debug(elems.nextElement());
        }
        
        Product prod = new Product();
        prod.setName("test");
        UserTransaction tx;
        try {
            tx = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
            tx.begin();
            manager.persist(prod);   
            tx.commit();
            logger.info("Product added");
        } catch (Exception ex) {
            logger.fatal("Erro while addind a product", ex);
        }
    }
    
    public Collection<Product> getProducts() {
        Query query = manager.createQuery("SELECT p FROM Product p");
        return (Collection<Product>) query.getResultList();
    }
}
