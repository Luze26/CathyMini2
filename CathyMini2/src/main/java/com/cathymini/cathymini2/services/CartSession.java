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
        logger.debug("avant create query");
        Query q = manager.createNamedQuery("CartByName", Cart.class); 
        q.setParameter("consumer", consumer);
        logger.debug("dans findCartByConsumer");
        if (q.getResultList().isEmpty())
            return null; 
        logger.debug("liste : r"+q.getResultList().size());

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
        logger.debug("quantity : "+quantity);
        cl.setQuantity(quantity);
        logger.debug("apres setQuantity");
        if(persist)
            manager.merge(cl);
        logger.debug("fin fonction");
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
                            addProductToCart(clTemp, cartCons, true);
                    }
                }
                logger.debug("merge finish");
            }
        }
        else{
            addCartToConsumer(cons, cartTemp);
            logger.debug("pas de cart pour le consumer donc mis a jour");
        }
        return "";
    }
    
     public Subscription newSubscription(Consumer cons){
        logger.debug("1");
        Subscription sub = new Subscription();
        logger.debug("2");
        sub.setConsumer(cons);
        logger.debug("3");
        sub.setNbJ(21);
        logger.debug("4");
        sub.setCartLineCollection(new ArrayList<CartLine>());
        logger.debug("5");
        if(cons != null)
            manager.persist(sub);
        logger.debug("6");
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
    
    private Subscription findSubByConsumer(Consumer consumer) {
        Query q = manager.createNamedQuery("SubscriptionByName", Subscription.class); 
        logger.debug("dans findSubByConsumer");
        q.setParameter("consumer", consumer);
        
        if (q.getResultList().isEmpty())
            return null; 
        logger.debug("nombre de sub pour ce consumer : "+q.getResultList().size());
        
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
                            addProductToSub(clTemp, subCons, true);
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
    
    public int changeNbJ(Subscription sub, int nbJ, boolean persist){
        sub.setNbJ(nbJ);
        if(persist)
            manager.merge(sub);
        return sub.getNbJ();
    }
    
}
