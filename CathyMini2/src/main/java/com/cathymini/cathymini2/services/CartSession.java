/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Cart;
import com.cathymini.cathymini2.model.CartLine;
import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.Product;
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
    private Cart cart;

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(CartSession.class);
    
    public Cart newCart(Consumer cons){
        Cart cart = new Cart();
        cart.setConsumer(cons);
        cart.setCartLineCollection(new ArrayList<CartLine>());
        manager.persist(cart);
        return cart;
    } 
    
    public void addProduct(Product prod, int qu, Cart cart){
        CartLine cl = new CartLine(prod, qu);
        manager.persist(cl);
        addProduct(cl, cart);
    }
    
    public void addProduct(Product prod, Cart cart){
        addProduct(prod, 1, cart);
    }
    
    public void addProduct(CartLine cl, Cart cart){
        cart.getCartLineCollection().add(cl);
        manager.merge(cart);
    }
    
    public void removeProduct(Product prod){
        
        Iterator<CartLine> it = cart.getCartLineCollection().iterator();
        CartLine myCartLine;
        while(it.hasNext()){
            myCartLine = it.next();
	// Manipulations avec l'élément actuel
            if(myCartLine.getProduct() == prod){
                cart.getCartLineCollection().remove(myCartLine);
            }
        }
        
    }
    
    public boolean subCartLine(CartLine cl){
        cart.getCartLineCollection().remove(cl);
        Query query = manager.createNamedQuery("DeleteCartLineById", CartLine.class); 
        query.setParameter("id", cl.getCartLineID());
        return query.executeUpdate() == 1;
    }
    
    public void subProduct(String nom, Float flux){
        subCartLine(getCartLine(nom, flux));
    }
    
    public CartLine getCartLine(String nom, Float flux){
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
    public String clear() {
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
    }
    
    public String mergeCart(Consumer cons, Cart cartTemp){
        Cart cartCons = findCartByConsumer(cons);
        if(cartCons.getCartLineCollection().isEmpty()){
            addCartToConsumer(cons, cartTemp);
        }
        else {
            for(CartLine cl : cartCons.getCartLineCollection()){
                for(CartLine clTemp : cartTemp.getCartLineCollection())
                {
                    if(cl.getProduct() == clTemp.getProduct()){
                        //change the quantity is it's the same product
                        cl.setQuantity(cl.getQuantity()+clTemp.getQuantity());
                    }
                    else{
                        //add the product
                        addProduct(clTemp, cartCons);
                    }
                }
            }
        }
        return "";
    }
}
