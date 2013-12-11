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
    
}
