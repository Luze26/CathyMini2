package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Cart;
import com.cathymini.cathymini2.model.CartLine;
import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.services.CartSession;
import com.cathymini.cathymini2.services.ConsumerBean;
import com.cathymini.cathymini2.services.ProductBean;
import com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author yuzel
 */
@Path("/cart")
public class CartFacade {

    private static final String CART_ATTR = "_CART_ATTR";
    private static final Logger logger = Logger.getLogger(CartFacade.class);
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();

    @EJB
    private CartSession cartBean;
    @EJB
    private ProductBean productBean;

    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean add(Long id, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons;
        try{
            cons = sessionSecuring.getConsumer(request);
        }catch(Exception ex){
            cons = null;
        }
        try{
            System.out.println(cons != null);
            if (cons != null) {
                Cart cart  = cartBean.getUserCart(cons);
                if(cart == null){
                    cart = cartBean.newCart(cons);
                }
                Product prod = productBean.getProduct(id);
                cartBean.addProduct(prod, cart, true);
                return true;
            }
            else{
                Product prod = productBean.getProduct(id);
                Cart newCartTemp = null;
                Boolean noCart = false;
               try{
                    newCartTemp = getCartSession(request);
                    if(newCartTemp == null)
                        noCart = true;
               }
               catch(Exception ex){
                   noCart = true;
                   return false;
               }
               
               if(noCart){
                    newCartTemp = cartBean.newCart(null);
                    setCartSession(request, newCartTemp);
               }
                cartBean.addProduct(prod, newCartTemp, false);
                return true;
            }
        } catch (Exception ex) {
            response.setStatus(400);
            return false;
            
        }
    }
    
    private Cart getCartSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return (Cart) session.getAttribute(CART_ATTR);
    }
    
    private void setCartSession(HttpServletRequest request, Cart cart) {
        HttpSession session = request.getSession(true);
        session.setAttribute(CART_ATTR, cart);
    }
    
    @POST
    @Path("/consumerIsConnected")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cart consumerIsConnected(@Context HttpServletRequest request, @Context HttpServletResponse response){
        System.out.println("Lien marche");
        Cart oldCart = getCartSession(request);
        Consumer cons  = sessionSecuring.getConsumer(request);
        if(oldCart != null){
            logger.debug("cartSession have been finded");
            cartBean.mergeCart(cons, oldCart);
            setCartSession(request, null);
            logger.debug("apres merge");
        }
        try{
                Cart cartToSend = cartBean.getUserCart(cons);
                logger.debug("on a un cart a envoyer");
                return cartToSend;
        }
        catch(Exception ex){
            logger.debug("retourne un cart null");
            return null;
        }

    }
    
    /*
    @POST
    @Path("/changeQuantity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String changeQuantity(Long id, int quantity, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons  = sessionSecuring.getConsumer(request);
        if(cons != null){
            try{
                Cart cart = cartBean.getUserCart(cons);
                CartLine cl = cartBean.getCartLineByID(id, cart);
                cartBean.changeQuantityCartLine(cl, quantity);
            }
            catch(Exception ex){
                return "the quantity haven't been changed";
            }
        }
        else{
            Cart cart = getCartSession(request);
            if(cart != null){
                CartLine cl = cartBean.getCartLineByID(id, cart);
                cartBean.changeQuantityCartLine(cl, quantity);
            }
            else{
                return "the cart doesn't exist, the quantity haven't been changed";
            }
        }
        return "the quantity have been changed";
    }*/
    
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cart delete(Long id, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Product prod = productBean.getProduct(id);
        Consumer cons  = sessionSecuring.getConsumer(request);
        Cart cart = null;
        if(cons != null){
            try{
                cart = cartBean.getUserCart(cons);
            }
            catch(Exception ex){
                return null;
            }
        }
        else{
            cart = getCartSession(request);
        }
        cartBean.removeProduct(prod, cart);
        return cart;
    }
    
}
