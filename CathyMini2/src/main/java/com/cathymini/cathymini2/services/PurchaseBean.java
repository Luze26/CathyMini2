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
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 * The class {@link PurchaseBean} is a stateless session bean to manage purchase
 * @author Kraiss
 */
@Stateless
public class PurchaseBean {

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(PurchaseBean.class);

    public void finalizePurchase(Cart cart, Consumer consumer, 
                DeliveryAddress da, PayementInfo pi) {
        // TODO : for each cartline, create a purchaseline and add to purchaseline collection
        Purchase purchase = new Purchase();
        purchase.setConsumer(consumer);
        purchase.setPayementInfo(pi);
        purchase.setDeliveryAddress(da);
        
        manager.persist(purchase);
        
        String message = consumer.getUsername() + "finalize a purchase.";
        logger.debug(message);
    }
    
    public void finalizeSubscription(Cart cart, Consumer consumer, 
                DeliveryAddress da, PayementInfo pi) {
        // TODO : for each cartline, create a purchaseline and add to purchaseline collection
        PurchaseSubscription purchase = new PurchaseSubscription();
        purchase.setConsumer(consumer);
        purchase.setPayementInfo(pi);
        purchase.setDeliveryAddress(da);
        
        manager.persist(purchase);
        
        String message = consumer.getUsername() + "finalize a subscription.";
        logger.debug(message);
    }
    
    public void editSubscription(Long subscriptionID, Cart cart, Consumer consumer, 
                DeliveryAddress da, PayementInfo pi) {
        PurchaseSubscription purchase = getSubscriptionById(subscriptionID);
        // TODO : empty the purchase lines collection (and delete it from DB)
        //     for each cartline, create a purchaseline and add to purchaseline collection
        purchase.setConsumer(consumer);
        purchase.setPayementInfo(pi);
        purchase.setDeliveryAddress(da);
        
        manager.persist(purchase);
        
        String message = consumer.getUsername() + "finalize a subscription.";
        logger.debug(message);
    }
    
    public void stopSubscription(Long subscriptionID, Cart cart, Consumer consumer, 
                DeliveryAddress da, PayementInfo pi) {
        PurchaseSubscription purchase = getSubscriptionById(subscriptionID);
        // TODO : empty the purchase lines collection (and delete it from DB)
        //     for each cartline, create a purchaseline and add to purchaseline collection
        purchase.setConsumer(consumer);
        purchase.setPayementInfo(pi);
        purchase.setDeliveryAddress(da);
        
        manager.persist(purchase);
        
        String message = consumer.getUsername() + "finalize a subscription.";
        logger.debug(message);
    }
    
    public Collection<Purchase> getConsumerPurchases(Consumer consumer) {
        Query q = manager.createNamedQuery("PurchaseByConsumer", Purchase.class);
        q.setParameter("consumer", consumer);

        if (q.getResultList().isEmpty())
            return null; 

        return q.getResultList();
    }
    
    public PurchaseSubscription getSubscriptionById(Long id) {
        Query q = manager.createNamedQuery("SubscriptionById", PurchaseSubscription.class);
        q.setParameter("transactionID", id);

        if (q.getResultList().isEmpty())
            return null; 

        return (PurchaseSubscription) q.getResultList().get(0);
    }
    
    public Collection<PurchaseSubscription> getConsumerSubscriptions(Consumer consumer) {
        Query q = manager.createNamedQuery("SubscriptionByConsumer", PurchaseSubscription.class);
        q.setParameter("consumer", consumer);

        if (q.getResultList().isEmpty())
            return null; 

        return q.getResultList();
    }
}
