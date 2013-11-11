/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.*;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

/**
 * The class {@link PurchaseBean} is a bean to manage purchase
 * @author Kraiss
 */
@Stateless
public class PurchaseBean {

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(ProductBean.class);

    public void finalizePurchase(Collection<Product> productList, Consumer consumer, 
                DeliveryAddress da, PayementInfo pi) {
        Purchase purchase = new Purchase();
        purchase.setProductCollection(productList);
        purchase.setConsumer(consumer);
        purchase.setPayementInfo(pi);
        purchase.setDeliveryAddress(da);
        
        manager.persist(purchase);
        
        String message = consumer.getUsername() + "finalize a purchase.";
        logger.debug(message);
    }
    
    public void finalizeSubscription(Collection<Product> productList, Consumer consumer, 
                DeliveryAddress da, PayementInfo pi) {
        PurchaseSubscription purchase = new PurchaseSubscription();
        purchase.setProductCollection(productList);
        purchase.setConsumer(consumer);
        purchase.setPayementInfo(pi);
        purchase.setDeliveryAddress(da);
        
        manager.persist(purchase);
        
        String message = consumer.getUsername() + "finalize a subscription.";
        logger.debug(message);
    }
    
}
