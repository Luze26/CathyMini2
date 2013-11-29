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
    
    public void addProduct(Product prod, int qu, Cart cart, boolean persist){
        CartLine cl = new CartLine(prod, qu);
        addProduct(cl, cart, persist);
    }
    
    public void addProduct(Product prod, Cart cart, Boolean persist){
        addProduct(prod, 1, cart, persist);
    }
    
     
    public void addProduct(CartLine cl, Cart cart, boolean persist){
         if(cart.getCartLineCollection() == null){
             cart.setCartLineCollection(new ArrayList<CartLine>());
         }
         cart.getCartLineCollection().add(cl);
         if(persist){
            manager.persist(cl);
            manager.merge(cart);
         }
    }
    
    public void removeProduct(Product prod, Cart cart){
        
        Iterator<CartLine> it = cart.getCartLineCollection().iterator();
        CartLine myCartLine;
        while(it.hasNext()){
            myCartLine = it.next();
	// Manipulations avec l'élément actuel
            if(myCartLine.getProduct() == prod){
                cart.getCartLineCollection().remove(myCartLine);
            }
        }
        manager.merge(cart);
        
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
    
    public CartLine getCartLineByID(Long id, Cart cart){
        CartLine cLine = null;
        Iterator<CartLine> it = cart.getCartLineCollection().iterator();
        CartLine myCartLine;
        while(it.hasNext()){
            myCartLine = it.next();
            if(myCartLine.getCartLineID() == id){
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
    public String clear(Cart cart) {
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
                            logger.debug("change quantity to a product");
                            cl.setQuantity(cl.getQuantity()+clTemp.getQuantity());
                            find = true;
                        }
                    }
                    if(!find){
                        logger.debug("product not found so add to cart cartLIne");
                            //add the product
                            addProduct(clTemp, cartCons, true);
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
}
