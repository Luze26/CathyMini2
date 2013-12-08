/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Cart;
import com.cathymini.cathymini2.model.CartLine;
import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.model.Subscription;
import java.util.ArrayList;
import java.util.Iterator;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author anaelle
 */
@Stateless
public class SubscriptionBean {
    
    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(SubscriptionBean.class);
    
    public Subscription newSubscription(Consumer cons){
        Subscription sub = new Subscription();
        sub.setConsumer(cons);
        sub.setNbJ(21);
        sub.setCartLineCollection(new ArrayList<CartLine>());
        if(cons != null)
            manager.persist(sub);
        return sub;
        
    }
    

    public void addProduct(Product prod, int qu, Subscription sub, boolean persist){
        CartLine cl = new CartLine(prod, qu);
        addProduct(cl, sub, persist);
    }
    
    public void addProduct(Product prod, Subscription sub, Boolean persist){
        addProduct(prod, 1, sub, persist);
    }
    
     
    public void addProduct(CartLine cl, Subscription sub, boolean persist){
         if(sub.getCartLineCollection() == null){
             sub.setCartLineCollection(new ArrayList<CartLine>());
         }
         Boolean find = false;
         for(CartLine clTemp : sub.getCartLineCollection()){
             if(clTemp.getProduct().getId() == cl.getProduct().getId()){
                 int q = clTemp.getQuantity() + cl.getQuantity();
                 changeQuantityCartLine(clTemp, q, persist);
                 find = true;
             }
         }
         if(!find)
            sub.getCartLineCollection().add(cl);
         if(persist){
            manager.persist(cl);
            manager.merge(sub);
         }
    }
    
    public int removeProduct(Product prod, Subscription sub){
        
        int place = -1;
        Iterator<CartLine> it = sub.getCartLineCollection().iterator();
        int i = 0;
        for(CartLine myCartLine : sub.getCartLineCollection()){
	// Manipulations avec l'élément actuel
            if(myCartLine.getProduct().getId() == prod.getId()){
                sub.getCartLineCollection().remove(myCartLine);
                manager.merge(sub);
                return i;
            }
            i++;
        }
        manager.merge(sub);
        return place;
    }
    
    public boolean subCartLine(CartLine cl, Cart cart){
        cart.getCartLineCollection().remove(cl);
        Query query = manager.createNamedQuery("DeleteCartLineById", CartLine.class); 
        query.setParameter("id", cl.getCartLineID());
        return query.executeUpdate() == 1;
    }
    
    public void subProduct(String nom, Float flux, Cart cart){
        subCartLine(getCartLine(nom, flux, cart), cart);
    }
    
    public CartLine getCartLine(String nom, Float flux, Cart cart){
        CartLine cLine = null;
        Iterator<CartLine> it = cart.getCartLineCollection().iterator();
        CartLine myCartLine;
        while(it.hasNext()){
            myCartLine = it.next();
            if(myCartLine.getProduct().getName().equals(nom) && myCartLine.getProduct().getFlux() == flux){
                cLine = myCartLine;
            }
        }
        return cLine;
    }
    
    public CartLine getCartLineByID(Long id, Subscription sub){
        CartLine cLine = null;
        for(CartLine myCartLine : sub.getCartLineCollection()){
            if(myCartLine.getProduct().getId() == id){
                cLine = myCartLine;
            }
        }
        return cLine;
    }
    
    
    /**
     * Empty the cart.
     */
    @Remove
    public String clear(Cart cart) {
        cart.getCartLineCollection().clear();
        return "Empty cart";
    }
    
    private Subscription findSubByConsumer(Consumer consumer) {
        Query q = manager.createNamedQuery("SubscriptionByName", Subscription.class); 
        q.setParameter("consumer", consumer);
        
        if (q.getResultList().isEmpty())
            return null; 
        
        return (Subscription) q.getResultList().get(0);
    }
    
    public Cart findSubByID(Long id){
        Query q = manager.createNamedQuery("SubscriptionByID", Subscription.class); 
        q.setParameter("id", id);
        
        if (q.getResultList().isEmpty())
            return null; 
        
        return (Cart) q.getResultList().get(0);
    }
    
    public void addSubscriptionToConsumer(Consumer cons, Subscription sub){
        sub.setCartID(sub.getCartID());
        manager.merge(sub);
    }
    
    public void addItemToCartLine(CartLine cl, Boolean persist){
        cl.setQuantity(cl.getQuantity()+1);
        if(persist)
            manager.merge(cl);
    }
    
    public void changeQuantityCartLine(CartLine cl, int quantity, Boolean persist){
        logger.debug("quantity : "+quantity);
        cl.setQuantity(quantity);
        logger.debug("apres setQuantity");
        if(persist)
            manager.merge(cl);
        logger.debug("fin fonction");
    }
    
    public String mergeCart(Consumer cons, Subscription subTemp){
        Subscription subCons = null;//TOdofindCartByConsumer(cons);
        if(subCons != null){
            if(subCons.getCartLineCollection().isEmpty()){
                addSubscriptionToConsumer(cons, subTemp);       
            }
            else {
                for(CartLine clTemp : subTemp.getCartLineCollection()){
                    boolean find = false;
                    for(CartLine cl : subCons.getCartLineCollection())
                    {
                        logger.debug("id cl : "+cl.getProduct().getId()+ "// id clTemp : "+ clTemp.getProduct().getId());
                        if(cl.getProduct().getId() == clTemp.getProduct().getId()){
                            //change the quantity is it's the same product
                            logger.debug("change quantity to a product");
                            cl.setQuantity(cl.getQuantity()+clTemp.getQuantity());
                            find = true;
                        }
                    }
                    if(!find){
                        logger.debug("product not found so add to cart cartLIne");
                            //add the product
                            addProduct(clTemp, subCons, true);
                    }
                }
                logger.debug("merge finish");
            }
        }
        else{
            addSubscriptionToConsumer(cons, subTemp);
            logger.debug("pas de cart pour le consumer donc mis a jour");
        }
        return "";
    }
    
    /**
     * Get a cart sessionBean of an user.
     */
    public Subscription getUserSubscription(Consumer consumer) throws Exception {
        return findSubByConsumer(consumer);
    }
    
}
