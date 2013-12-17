package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.*;
import java.util.ArrayList;
import java.util.Calendar;
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

    public void finalizePurchase(Cart cart, DeliveryAddress da, PayementInfo pi) {
        Purchase purchase = new Purchase();
        purchase.setConsumer(cart.getConsumer());
        purchase.setPurchaseLineCollection(cartLineToPurchaseLine(
                cart.getCartLineCollection()));
        purchase.setPayementInfo(pi);
        purchase.setDeliveryAddress(da);
        Calendar cal = Calendar.getInstance();
        purchase.setCreationDate(cal.getTimeInMillis());
        
        manager.persist(purchase);
        
        String message = cart.getConsumer().getUsername() + "finalize a purchase.";
        logger.debug(message);
    }
    
    public void finalizeSubscription(Cart cart, Long startDate, DeliveryAddress da, PayementInfo pi, Integer daysDelay) {
        PurchaseSubscription purchase = new PurchaseSubscription();
        purchase.setConsumer(cart.getConsumer());
        purchase.setPurchaseLineCollection(cartLineToPurchaseLine(
                cart.getCartLineCollection()));
        purchase.setPayementInfo(pi);
        purchase.setDeliveryAddress(da);
        Calendar cal = Calendar.getInstance();
        purchase.setCreationDate(cal.getTimeInMillis());
        purchase.setDaysDelay(daysDelay);
        
        manager.persist(purchase);
        
        String message = cart.getConsumer().getUsername() + "finalize a subscription.";
        logger.debug(message);
    }
    
    public void editSubscription(Long subscriptionID, Cart cart,  Long startDate,
                DeliveryAddress da, PayementInfo pi, Integer daysDelay) {
        
        PurchaseSubscription purchase = getSubscriptionById(subscriptionID);
        
        purchase.setConsumer(cart.getConsumer());
        purchase.setPurchaseLineCollection(cartLineToPurchaseLine(
                cart.getCartLineCollection()));
        purchase.setPayementInfo(pi);
        purchase.setDeliveryAddress(da);
        Calendar cal = Calendar.getInstance();
        purchase.setCreationDate(cal.getTimeInMillis()); // Re-set to re-calculate next delivery
        purchase.setDaysDelay(daysDelay);
        
        manager.persist(purchase);
        
        String message = cart.getConsumer().getUsername() + "finalize a subscription.";
        logger.debug(message);
    }
    
    public void stopSubscription(Long subscriptionID, Cart cart, Consumer consumer, 
                DeliveryAddress da, PayementInfo pi) {
        PurchaseSubscription subscription = getSubscriptionById(subscriptionID);
        
        manager.remove(subscription);
        
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
    
    public Collection<PurchaseSubscription> getConsumerSubscriptions(Consumer consumer) {
        Query q = manager.createNamedQuery("SubscriptionByConsumer", PurchaseSubscription.class);
        q.setParameter("consumer", consumer);

        if (q.getResultList().isEmpty())
            return null; 

        return q.getResultList();
    }
    
    public Collection<PurchaseSubscription> getAllSubscriptions() {
        Query q = manager.createNamedQuery("AllSubscription", PurchaseSubscription.class);

        if (q.getResultList().isEmpty())
            return null;

        return q.getResultList();
    }
    
    private Collection<PurchaseLine> cartLineToPurchaseLine(Collection<CartLine> cartLines) {
        Collection<PurchaseLine> purchase = new ArrayList<PurchaseLine>();
        for (CartLine cartline : cartLines){
            PurchaseLine purchaseline = new PurchaseLine();
            purchaseline.setProduct(cartline.getProduct());
            purchaseline.setQuantity(cartline.getQuantity());
            purchase.add(purchaseline);
        }
        return purchase;
    }
    
    private PurchaseSubscription getSubscriptionById(Long id) {
        Query q = manager.createNamedQuery("SubscriptionById", PurchaseSubscription.class);
        q.setParameter("transactionID", id);

        if (q.getResultList().isEmpty())
            return null; 

        return (PurchaseSubscription) q.getResultList().get(0);
    }
}
