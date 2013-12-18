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
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author Kraiss
 */
@Stateless
public class CartBean {

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(CartBean.class);
    
    /**
     * Create a new cart
     * @param cons the consumer
     * @return the new cart 
     */
    public Cart newCart(Consumer cons){
        Cart cart = new Cart();
        cart.setConsumer(cons);
        cart.setCartLineCollection(new ArrayList<CartLine>());
        if(cons != null)
            manager.persist(cart);
        return cart;
    }
    
    /**
     * Add a product to a cart
     * @param prod the product
     * @param qu the new quantity
     * @param cart the cart
     * @param persist true if should be save
     */
    public void addProductToCart(Product prod, int qu, Cart cart, boolean persist){
        CartLine cl = new CartLine(prod, qu);
        addProductToCart(cl, cart, persist);
    }
    
    /**
     * Add a product to a cart when you add 1 product
     * @param prod the product
     * @param cart the cart
     * @param persist true if should be save
     */
    public void addProductToCart(Product prod, Cart cart, Boolean persist){
        addProductToCart(prod, 1, cart, persist);
    }
    
    /**
     * Add product to a cart
     * @param cl the cartLine which contains the product to add
     * @param cart the cart
     * @param persist true if should be save
     */ 
    public void addProductToCart(CartLine cl, Cart cart, boolean persist){
         if(cart.getCartLineCollection() == null){
             cart.setCartLineCollection(new ArrayList<CartLine>());
         }
         Boolean find = false;
         for(CartLine clTemp : cart.getCartLineCollection()){
             if(clTemp.getProduct().getId().compareTo(cl.getProduct().getId())==0){
                 int q = clTemp.getQuantity() + cl.getQuantity();
                 changeQuantityCartLine(clTemp, q, persist);
                 find = true;
             }
         }
         if(!find){
            cart.getCartLineCollection().add(cl);
            if(persist)
                manager.persist(cl);
         }
         if(persist){
            manager.merge(cart);
         }
    }
    
    /**
     * Change the quantity to a product in a cart
     * @param prod the product to change
     * @param qu the new quantity
     * @param cart the cart 
     * @param persist true if should be save
     */
    public void changeQuantityToCart(Product prod, int qu, Cart cart, boolean persist){
        CartLine cl = new CartLine(prod, qu);
         for(CartLine clTemp : cart.getCartLineCollection()){
             if(clTemp.getProduct().getId().compareTo(cl.getProduct().getId())==0){
                 int q = cl.getQuantity();
                 changeQuantityCartLine(clTemp, q, persist);
             }
         }
         if(persist){
            manager.merge(cart);
         }
    }
    
    /**
     * remove a product to a cart
     * @param prod the product to delete
     * @param cart the cart
     * @return 0 if the product have been removed
     */
    public int removeProductToCart(Product prod, Cart cart){
        
        int place = -1;
        int i = 0;
        for(CartLine myCartLine : cart.getCartLineCollection()){
	// Manipulations avec l'élément actuel
            if(myCartLine.getProduct().getId().compareTo(prod.getId()) == 0){
                subCartLineToCart(myCartLine, cart);
                manager.merge(cart);
                return i;
            }
            i++;
        }
        manager.merge(cart);
        return place;
    }
    
    /**
     * Remove a product to a cart with the cartLine
     * @param cl the cartLine which contain the product to delete
     * @param cart the cart
     * @return true if the line have been saved
     */
    public boolean subCartLineToCart(CartLine cl, Cart cart){
        cart.getCartLineCollection().remove(cl);
        Query query = manager.createNamedQuery("DeleteCartLineById", CartLine.class); 
        query.setParameter("id", cl.getCartLineID());
        return query.executeUpdate() == 1;
    }
    
    /**
     * get the cartLine with the id
     * @param id the cartLine's Id
     * @param cart the cart
     * @return the cartLine
     */
    public CartLine getCartLineCartByID(Long id, Cart cart){
        CartLine cLine = null;
        for(CartLine myCartLine : cart.getCartLineCollection()){
            if(myCartLine.getProduct().getId().compareTo(id) == 0){
                cLine = myCartLine;
            }
        }
        return cLine;
    }
    
    /**
     * Get a cart sessionBean of an user.
     */
    public Cart getUserCart(Consumer consumer) throws Exception {
        return findCartByConsumer(consumer);
    }
    
    /**
     * Empty the cart.
     */
    @Remove
    public void clearCart(Cart cart) {
        cart.getCartLineCollection().clear();
        manager.merge(cart);
    }
    
    /**
     * find the cart the a consumer
     * @param consumer the consumer
     * @return the cart of the consumer
     */
    private Cart findCartByConsumer(Consumer consumer) {
        Query q = manager.createNamedQuery("CartByName", Cart.class); 
        q.setParameter("consumer", consumer);
        if (q.getResultList().isEmpty())
            return null; 

        return (Cart) q.getResultList().get(0);
        
    }
    
    /**
     * find a cart by id
     * @param id the cart's id
     * @return the cart
     */
    public Cart findCartByID(Long id){
        Query q = manager.createNamedQuery("CartByID", Cart.class); 
        q.setParameter("id", id);
        
        if (q.getResultList().isEmpty())
            return null; 
        
        return (Cart) q.getResultList().get(0);
    }
    
    /**
     * Add a cart to a cnsumer
     * @param cons the consumer
     * @param cart the cart to add
     */
    public void addCartToConsumer(Consumer cons, Cart cart){
        cart.setConsumer(cons);
        manager.merge(cart);
    }
    
    /**
     * add an item to a cartLine
     * @param cl the cartLine to update
     * @param persist true if it should be save
     */
    public void addItemToCartLine(CartLine cl, Boolean persist){
        cl.setQuantity(cl.getQuantity()+1);
        if(persist)
            manager.merge(cl);
    }
    
    /**
     * change the quantity on a cartLine
     * @param cl the cartLine to update
     * @param quantity the new quantity of the product
     * @param persist true if it should be save
     */
    public void changeQuantityCartLine(CartLine cl, int quantity, Boolean persist){
        cl.setQuantity(quantity);
        if(persist)
            manager.merge(cl);
    }
    
    /**
     * mergea cart with the consumer's cart
     * @param cons the consumer
     * @param cartTemp the cart to merge
     * @return 
     */
    public String mergeCart(Consumer cons, Cart cartTemp){
        Cart cartCons = findCartByConsumer(cons);
        if(cartCons != null){
            if(cartCons.getCartLineCollection().isEmpty()){
                addCartToConsumer(cons, cartTemp);       
            }
            else {
                for(CartLine clTemp : cartTemp.getCartLineCollection()){
                    boolean find = false;
                    for(CartLine cl : cartCons.getCartLineCollection())
                    {
                        if(cl.getProduct().getId().compareTo(clTemp.getProduct().getId()) == 0){
                            //change the quantity is it's the same product
                            cl.setQuantity(cl.getQuantity()+clTemp.getQuantity());
                            find = true;
                        }
                    }
                    if(!find){
                            //add the product
                            addProductToCart(clTemp, cartCons, true);
                    }
                }
            }
        }
        else{
            addCartToConsumer(cons, cartTemp);
        }
        return "";
    }
    
    /**
     * create a new subscription
     * @param cons the consumer
     * @param name the name of the subscription
     * @return the new subscription
     */
     public Subscription newSubscription(Consumer cons, String name){
        Subscription sub = new Subscription();
        sub.setConsumer(cons);
        sub.setDaysDelay(21);
        sub.setCartLineCollection(new ArrayList<CartLine>());
        sub.setName(name);
        if(cons != null)
            manager.persist(sub);
        return sub; 
    }
     
     /**
      * record a subscription
      * @param cons the consumer
      * @param sub the subscription to add at the consumer
      * @return the subscription updated
      */
     public Subscription recordSubscription(Consumer cons, Subscription sub){
         if(sub.getConsumer() != cons){
            sub.setConsumer(cons);
            if(cons != null){
                for (CartLine cl : sub.getCartLineCollection()) {
                    manager.persist(cl);
                }
               manager.persist(sub);
            }
         }
         return sub;
     }
    
     /**
      * Add qu product to subscription
      * @param prod the product to add
      * @param qu is quantity
      * @param sub the subscription
      * @param persist true if it should be saved
      */
    public void addProductToSub(Product prod, int qu, Subscription sub, boolean persist){
        CartLine cl = new CartLine(prod, qu);
        addProductToSub(cl, sub, persist);
    }
    
    /**
     * Add one product to subscription
     * @param prod the product to add
     * @param sub the subscription
     * @param persist true if it should be saved
     */
    public void addProductToSub(Product prod, Subscription sub, Boolean persist){
        addProductToSub(prod, 1, sub, persist);
    }
    
     /**
      * Add product to subscription
      * @param cl the cartLine to add
      * @param sub the subscription
      * @param persist true if it sould be saved
      */
    public void addProductToSub(CartLine cl, Subscription sub, boolean persist){
         if(sub.getCartLineCollection() == null){
             sub.setCartLineCollection(new ArrayList<CartLine>());
         }
         Boolean find = false;
         for(CartLine clTemp : sub.getCartLineCollection()){
             if(clTemp.getProduct().getId().compareTo(cl.getProduct().getId()) == 0){
                 int q = clTemp.getQuantity() + cl.getQuantity();
                 changeQuantityCartLine(clTemp, q, persist);
                 find = true;
             }
         }
         if(!find){
            if(persist){
                manager.persist(cl);
            }
            sub.getCartLineCollection().add(cl);
         }
         if(persist){
            manager.merge(sub);
         }
    }
    
    /**
     * change the quantity to a product in a sub
     * @param prod the product to update
     * @param qu the new quantity
     * @param sub the subscription
     * @param persist true if it sould be saved
     */
    public void changeQuantityToSub(Product prod, int qu, Subscription sub, boolean persist){
        CartLine cl = new CartLine(prod, qu);
         for(CartLine clTemp : sub.getCartLineCollection()){
             if(clTemp.getProduct().getId().compareTo(cl.getProduct().getId()) == 0){
                 int q = cl.getQuantity();
                 changeQuantityCartLine(clTemp, q, persist);
             }
         }
         if(persist){
            manager.merge(sub);
         }
    }
    
    /**
     * remove a product to a subscription
     * @param prod the product to remove
     * @param sub the subscription
     * @return 0 if the product have been removed, else -1
     */
    public int removeProductToSub(Product prod, Subscription sub){
        int place = -1;
        int i = 0;
        
        for(CartLine myCartLine : sub.getCartLineCollection()){
	// Manipulations avec l'élément actuel
            if(myCartLine.getProduct().getId().compareTo(prod.getId()) == 0){
                subCartLineToSub(myCartLine, sub);
                manager.merge(sub);
                return i;
            }
            i++;
        }
        manager.merge(sub);
        return place;
    }
    
    /**
     * remove a cartLine to a subscription
     * @param cl the cartLine to remove
     * @param sub the subscription
     * @return true if have been removed
     */
    public boolean subCartLineToSub(CartLine cl, Subscription sub){
        sub.getCartLineCollection().remove(cl);
        Query query = manager.createNamedQuery("DeleteCartLineById", CartLine.class); 
        query.setParameter("id", cl.getCartLineID());
        return query.executeUpdate() == 1;
    }
    
    /**
     * Empty the cart.
     */
    @Remove
    public String clearSub(Subscription sub) {
        sub.getCartLineCollection().clear();
        return "Empty cart";
    }
    
    /**
     * find all consumer's subscriptions
     * @param consumer
     * @return the list of his subscriptions
     */
    private ArrayList<Subscription> findSubByConsumer(Consumer consumer) {
        Query q = manager.createNamedQuery("SubscriptionByCons", Subscription.class); 
        q.setParameter("consumer", consumer);
        
        if (q.getResultList().isEmpty())
            return null; 
        return  new ArrayList<Subscription>(q.getResultList());
    }
    
    /**
     * find the consumer's subscription with the name name
     * @param consumer the consumer
     * @param name the name
     * @return the subscription
     */
    private Subscription findSubByConsumerAndName(Consumer consumer, String name) {
        Query q = manager.createNamedQuery("SubscriptionByName", Subscription.class); 
        q.setParameter("consumer", consumer);
        q.setParameter("name", name);
        
        if (q.getResultList().isEmpty()){
            return null; 
        }
        
        return (Subscription) q.getResultList().get(0);
    }
    
    /**
     * Get a cart sessionBean of an user.
     */
    public ArrayList<Subscription> getUserSubscription(Consumer consumer) throws Exception {
        return findSubByConsumer(consumer);
    }
    
    /**
     * Get a cart sessionBean of an user.
     */
    public Subscription getUserSubscriptionByName(Consumer consumer, String name) throws Exception {
        return findSubByConsumerAndName(consumer, name);
    }
    
    /**
     * change the number of days between 2 purchase
     * @param sub the subscription
     * @param nbJ the new number of days
     * @param persist true if it should be saved
     * @return the number of days
     */
    public int changeNbJ(Subscription sub, int nbJ, boolean persist){
        sub.setDaysDelay(nbJ);
        if(persist)
            manager.merge(sub);
        return sub.getDaysDelay();
    }

    /**
     * change the name of a subscription
     * @param sub the subscription
     * @param name the new name
     * @param persist true if it should be saved
     * @return the new name
     */
    public String changeName(Subscription sub, String name, boolean persist){
        sub.setName(name);
        if(persist)
            manager.merge(sub);
        return sub.getName();
    }
    
    /**
     * look if the new name is correct
     * @param name the new name
     * @param cons the consumer
     * @return true if the name is ok
     */
    public boolean correctName(String name, Consumer cons){
        boolean ok = true;
        try{
            ArrayList<Subscription> mySub = getUserSubscription(cons);
            for(Subscription s : mySub){
                if(s.getName().equals(name))
                    ok = false;
            }
        }
        catch(Exception ex){
            ok = false;
        }
        return ok;
    }
}
