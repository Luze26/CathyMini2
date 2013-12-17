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
 * @author Kraiss
 */
@Stateless
public class CartSession {

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(CartSession.class);
    
    public Cart newCart(Consumer cons){
        Cart cart = new Cart();
        cart.setConsumer(cons);
        cart.setCartLineCollection(new ArrayList<CartLine>());
        if(cons != null)
            manager.persist(cart);
        return cart;
    }
    
    public void addProductToCart(Product prod, int qu, Cart cart, boolean persist){
        CartLine cl = new CartLine(prod, qu);
        addProductToCart(cl, cart, persist);
    }
    
    public void addProductToCart(Product prod, Cart cart, Boolean persist){
        addProductToCart(prod, 1, cart, persist);
    }
    
     
    public void addProductToCart(CartLine cl, Cart cart, boolean persist){
         if(cart.getCartLineCollection() == null){
             cart.setCartLineCollection(new ArrayList<CartLine>());
         }
         Boolean find = false;
         for(CartLine clTemp : cart.getCartLineCollection()){
             if(clTemp.getProduct().getId() == cl.getProduct().getId()){
                 int q = clTemp.getQuantity() + cl.getQuantity();
                 changeQuantityCartLine(clTemp, q, persist);
                 find = true;
             }
         }
         if(!find)
            cart.getCartLineCollection().add(cl);
         if(persist){
            manager.persist(cl);
            manager.merge(cart);
         }
    }
    
    public int removeProductToCart(Product prod, Cart cart){
        
        int place = -1;
        Iterator<CartLine> it = cart.getCartLineCollection().iterator();
        int i = 0;
        for(CartLine myCartLine : cart.getCartLineCollection()){
	// Manipulations avec l'élément actuel
            if(myCartLine.getProduct().getId() == prod.getId()){
                cart.getCartLineCollection().remove(myCartLine);
                manager.merge(cart);
                return i;
            }
            i++;
        }
        manager.merge(cart);
        return place;
    }
    
    public boolean subCartLineToCart(CartLine cl, Cart cart){
        cart.getCartLineCollection().remove(cl);
        Query query = manager.createNamedQuery("DeleteCartLineById", CartLine.class); 
        query.setParameter("id", cl.getCartLineID());
        return query.executeUpdate() == 1;
    }
    
    public void subProductToCart(String nom, Float flux, Cart cart){
        subCartLineToCart(getCartLineToCart(nom, flux, cart), cart);
    }
    
    public CartLine getCartLineToCart(String nom, Float flux, Cart cart){
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
    
    public CartLine getCartLineCartByID(Long id, Cart cart){
        CartLine cLine = null;
        for(CartLine myCartLine : cart.getCartLineCollection()){
            if(myCartLine.getProduct().getId() == id){
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
    public String clearCart(Cart cart) {
        cart.getCartLineCollection().clear();
        return "Empty cart";
    }
    
    private Cart findCartByConsumer(Consumer consumer) {
        Query q = manager.createNamedQuery("CartByName", Cart.class); 
        q.setParameter("consumer", consumer);
        if (q.getResultList().isEmpty())
            return null; 

        return (Cart) q.getResultList().get(0);
        
    }
    
    public Cart findCartByID(Long id){
        Query q = manager.createNamedQuery("CartByID", Cart.class); 
        q.setParameter("id", id);
        
        if (q.getResultList().isEmpty())
            return null; 
        
        return (Cart) q.getResultList().get(0);
    }
    
    public void addCartToConsumer(Consumer cons, Cart cart){
        cart.setConsumer(cons);
        manager.merge(cart);
    }
    
    public void addItemToCartLine(CartLine cl, Boolean persist){
        cl.setQuantity(cl.getQuantity()+1);
        if(persist)
            manager.merge(cl);
    }
    
    public void changeQuantityCartLine(CartLine cl, int quantity, Boolean persist){
        cl.setQuantity(quantity);
        if(persist)
            manager.merge(cl);
    }
    
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
                        if(cl.getProduct().getId() == clTemp.getProduct().getId()){
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
    
     public Subscription newSubscription(Consumer cons, String name){
        Subscription sub = new Subscription();
        sub.setConsumer(cons);
        sub.setNbJ(21);
        sub.setCartLineCollection(new ArrayList<CartLine>());
        sub.setName(name);
        if(cons != null)
            manager.persist(sub);
        return sub; 
    }
    

    public void addProductToSub(Product prod, int qu, Subscription sub, boolean persist){
        CartLine cl = new CartLine(prod, qu);
        addProductToSub(cl, sub, persist);
    }
    
    public void addProductToSub(Product prod, Subscription sub, Boolean persist){
        addProductToSub(prod, 1, sub, persist);
    }
    
     
    public void addProductToSub(CartLine cl, Subscription sub, boolean persist){
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
    
    public int removeProductToSub(Product prod, Subscription sub){
        
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
    
    public boolean subCartLineToSub(CartLine cl, Subscription sub){
        sub.getCartLineCollection().remove(cl);
        Query query = manager.createNamedQuery("DeleteCartLineById", CartLine.class); 
        query.setParameter("id", cl.getCartLineID());
        return query.executeUpdate() == 1;
    }
    
    public void subCartLineToSub(String nom, Float flux, Subscription sub){
        subCartLineToSub(getCartLine(nom, flux, sub), sub);
    }
    
    public CartLine getCartLine(String nom, Float flux, Subscription sub){
        CartLine cLine = null;
        Iterator<CartLine> it = sub.getCartLineCollection().iterator();
        CartLine myCartLine;
        while(it.hasNext()){
            myCartLine = it.next();
            if(myCartLine.getProduct().getName().equals(nom) && myCartLine.getProduct().getFlux() == flux){
                cLine = myCartLine;
            }
        }
        return cLine;
    }
    
    public CartLine getCartLineSubByID(Long id, Subscription sub){
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
    public String clearSub(Subscription sub) {
        sub.getCartLineCollection().clear();
        return "Empty cart";
    }
    
    private ArrayList<Subscription> findSubByConsumer(Consumer consumer) {
        Query q = manager.createNamedQuery("SubscriptionByCons", Subscription.class); 
        q.setParameter("consumer", consumer);
        
        if (q.getResultList().isEmpty())
            return null; 
        
        return (ArrayList<Subscription>) q.getResultList();
    }
    
    private Subscription findSubByConsumerAndName(Consumer consumer, String name) {
        Query q = manager.createNamedQuery("SubscriptionByName", Subscription.class); 
        q.setParameter("consumer", consumer);
        q.setParameter("name", name);
        
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
        sub.setConsumer(cons);
        manager.merge(sub);
    }
    
    public String mergeSub(Consumer cons, Subscription subTemp){
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
                        if(cl.getProduct().getId() == clTemp.getProduct().getId()){
                            //change the quantity is it's the same product
                            cl.setQuantity(cl.getQuantity()+clTemp.getQuantity());
                            find = true;
                        }
                    }
                    if(!find){
                            //add the product
                            addProductToSub(clTemp, subCons, true);
                    }
                }
            }
        }
        else{
            addSubscriptionToConsumer(cons, subTemp);
        }
        return "";
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
    
    public int changeNbJ(Subscription sub, int nbJ, boolean persist){
        sub.setNbJ(nbJ);
        if(persist)
            manager.merge(sub);
        return sub.getNbJ();
    }

    public String changeName(Subscription sub, String name, boolean persist){
        sub.setName(name);
        if(persist)
            manager.merge(sub);
        return sub.getName();
    }
}
