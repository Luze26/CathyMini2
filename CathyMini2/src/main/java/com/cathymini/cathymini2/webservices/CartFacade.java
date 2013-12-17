package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Cart;
import com.cathymini.cathymini2.model.CartLine;
import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.model.Subscription;
import com.cathymini.cathymini2.services.CartSession;
import com.cathymini.cathymini2.services.ProductBean;
import com.cathymini.cathymini2.webservices.model.CartProduct;
import com.cathymini.cathymini2.webservices.model.SubProduct;
import com.cathymini.cathymini2.webservices.model.SubscriptionDays;
import com.cathymini.cathymini2.webservices.model.SubscriptionName;
import com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring;
import java.util.ArrayList;
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
 * Cart facade, operation on cart and subscription.
 *
 * @author yuzel
 */
@Path("/cart")
public class CartFacade {

    private static final String CART_ATTR = "_CART_ATTR";
    private static final String SUB_ATTR = "_SUB_ATTR";
    private static final Logger logger = Logger.getLogger(CartFacade.class);
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();

    @EJB
    private CartSession cartBean;
    @EJB
    private ProductBean productBean;

    /**
     * Add a product to a cart
     *
     * @param id product's id to add
     * @param request
     * @param response
     * @return true if the product has been added, false otherwise
     */
    @POST
    @Path("/addProductToCart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean addProductToCart(Long id, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons;
        try{
            cons = sessionSecuring.getConsumer(request);
        }catch(Exception ex){
            cons = null;
        }
        try{
            if (cons != null) {
                Cart cart  = cartBean.getUserCart(cons);
                if(cart == null){
                    cart = cartBean.newCart(cons);
                }
                Product prod = productBean.getProduct(id);
                cartBean.addProductToCart(prod, cart, true);
                return true;
            }
            else{
                Product prod = productBean.getProduct(id);
                Cart newCartTemp;
                Boolean noCart = false;
                try {
                    newCartTemp = getCartSession(request);
                    if (newCartTemp == null) {
                        noCart = true;
                    }
                } catch (Exception ex) {
                    return false;
                }

                if (noCart) {
                    newCartTemp = cartBean.newCart(null);
                    setCartSession(request, newCartTemp);
                }
                cartBean.addProductToCart(prod, newCartTemp, false);
                return true;
            }
        } catch (Exception ex) {
            response.setStatus(400);
            return false;
            
        }
    }

    /**
     * Return the cart
     *
     * @param request
     * @param response
     * @return the cart for the current user
     */
    @POST
    @Path("/getCart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cart consumerIsConnected(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Cart oldCart = getCartSession(request);
        Consumer cons  = sessionSecuring.getConsumer(request);
        if(oldCart != null){
            cartBean.mergeCart(cons, oldCart);
            setCartSession(request, null);
        }
        try{
                Cart cartToSend = cartBean.getUserCart(cons);
                return cartToSend;
        }
        catch(Exception ex){
            response.setStatus(400);
            return null;
        }

    }

    /**
     * Change the quantity of a product in the cart
     *
     * @param clTemp product's id + quantity
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/changeQuantityToCart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int changeQuantityToCart(CartProduct clTemp, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons  = sessionSecuring.getConsumer(request);
        if(cons != null){
            try{
                Cart cart = cartBean.getUserCart(cons);
                CartLine cl = cartBean.getCartLineCartByID(Long.parseLong(String.valueOf(clTemp.getProductId())), cart);
                cartBean.changeQuantityCartLine(cl, clTemp.getQuantity(), true);
            }
            catch(Exception ex){
                response.setStatus(400);
                return -1;
            }
        }
        else{
            Cart cart = getCartSession(request);
            if(cart != null){
                CartLine cl = cartBean.getCartLineCartByID(Long.parseLong(String.valueOf(clTemp.getProductId())), cart);
                cartBean.changeQuantityCartLine(cl, clTemp.getQuantity(), false);
            }
            else{
                response.setStatus(400);
                return -1;
            }
        }
        return clTemp.getQuantity();
    }

    /**
     * Delete a product from the cart
     *
     * @param id product's id
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/deleteToCart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int deleteToCart(Long id, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Product prod = productBean.getProduct(id);
        Consumer cons  = sessionSecuring.getConsumer(request);
        Cart cart;
        int place = -1;
        if(cons != null){
            try{
                cart = cartBean.getUserCart(cons);
            }
            catch(Exception ex){
                response.setStatus(400);
                return -1;
            }
        }
        else{
            cart = getCartSession(request);
        }
        place = cartBean.removeProductToCart(prod, cart);
        return place;
    }

    /**
     * Add a product to a subscription
     *
     * @param id product's id
     * @param request
     * @param response
     * @return true if the product has been added, false otherwise
     */
    @POST
    @Path("/newSubscription")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String newSubscription(SubProduct subP, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons;
        String name = "abonnement";
        int nbAbo;
        
        cons  = sessionSecuring.getConsumer(request);
        try{
            ArrayList li = cartBean.getUserSubscription(cons);
            nbAbo = li.size()+1;
        }
        catch(Exception ex){
            nbAbo = 1;
        }
        name += nbAbo;
        
        Subscription sub = cartBean.newSubscription(cons,name);
        setSubSession(request, sub);
        return name;
        
    }
    
    

    @POST
    @Path("/addProductToSub")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean addProductToSub(SubProduct subP, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons;
        try{
            cons = sessionSecuring.getConsumer(request);
        }catch(Exception ex){
            cons = null;
        }
        try{
            if (cons != null) {
                Subscription sub  = cartBean.getUserSubscriptionByName(cons, subP.getName());
                if(sub == null){
                    /*logger.debug("dans addProductToSub : "+subP.getName());
                    sub = cartBean.newSubscription(cons, "abonnement2");*/
                    response.setStatus(400);
                    return false;
                }
                Product prod = productBean.getProduct(subP.getProductId());
                cartBean.addProductToSub(prod, sub, true);
                return true;
            }
            else{
                Product prod = productBean.getProduct(subP.getProductId());
                Subscription newSubTemp = null;
                Boolean noSub = false;
               try{
                    newSubTemp = getSubSession(request);
                    if(newSubTemp == null)
                        noSub = true;
               }
               catch(Exception ex){
                   noSub = true;
                   return false;
               }
               
               if(noSub){
                    /*newSubTemp = cartBean.newSubscription(null, "abonnement3");
                    setSubSession(request, newSubTemp);*/
                    response.setStatus(400);
                    return false;
               }
                cartBean.addProductToSub(prod, newSubTemp, false);
                return true;
            }
        } catch (Exception ex) {
            response.setStatus(400);
            return false;
            
        }
    }

    /**
     * Get the subscription for the current user
     *
     * @param request
     * @param response
     * @return the subscription for the current user
     */
    @POST
    @Path("/getSub")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList consumerIsConnectedSub(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Subscription sub = getSubSession(request);
        Consumer cons  = sessionSecuring.getConsumer(request);
        if(sub != null){
            cartBean.recordSubscription(cons, sub);
            setSubSession(request, null);
        }
        try{
                ArrayList  cartToSend = cartBean.getUserSubscription(cons);
                return cartToSend;
        }
        catch(Exception ex){
            response.setStatus(400);
            return null;
        }

    }

    /**
     * Change the quantity of a product in the subscription
     *
     * @param clTemp product's id + quantity
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/changeQuantityToSub")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int changeQuantityToSub(SubProduct clTemp, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons  = sessionSecuring.getConsumer(request);
        if(cons != null){
            try{
                Subscription sub = cartBean.getUserSubscriptionByName(cons, clTemp.getName());
                CartLine cl = cartBean.getCartLineSubByID(Long.parseLong(String.valueOf(clTemp.getProductId())), sub);
                cartBean.changeQuantityCartLine(cl, clTemp.getQuantity(), true);
            }
            catch(Exception ex){
                response.setStatus(400);
                return -1;
            }
        }
        else{
            Subscription sub = getSubSession(request);
            if(sub != null){
                CartLine cl = cartBean.getCartLineSubByID(Long.parseLong(String.valueOf(clTemp.getProductId())), sub);
                cartBean.changeQuantityCartLine(cl, clTemp.getQuantity(), false);
            }
            else{
                response.setStatus(400);
                return -1;
            }
        }
        return clTemp.getQuantity();
    }

    /**
     * Delete the product from the subscription
     *
     * @param id product's id to delete
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/deleteToSub")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int deleteToSub(SubProduct subP, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Product prod = productBean.getProduct(subP.getProductId());
        Consumer cons  = sessionSecuring.getConsumer(request);
        Subscription sub = null;
        int place = -1;
        if(cons != null){
            try{
                sub = cartBean.getUserSubscriptionByName(cons, subP.getName());
            }
            catch(Exception ex){
                response.setStatus(400);
                return -1;
            }
        }
        else{
            sub = getSubSession(request);
        }
        place = cartBean.removeProductToSub(prod, sub);
        return place;
    }

    /**
     * Change the number of days for a subscription
     *
     * @param nbJ number of days
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/changeNbJ")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int changeNbJ(SubscriptionDays subD, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons  = sessionSecuring.getConsumer(request);
        Subscription sub = null;
        if(cons != null){
            try{
                sub = cartBean.getUserSubscriptionByName(cons, subD.getName());
                cartBean.changeNbJ(sub, subD.getNbJ(), true);
            }
            catch(Exception ex){
                response.setStatus(400);
                return -1;
            }
        }
        else{
            sub = getSubSession(request);
            if(sub != null){
                cartBean.changeNbJ(sub, subD.getNbJ(), false);
            }
            else{
                response.setStatus(400);
                return -1;
            }
        }
        return sub.getNbJ();
    }
    
    /**
     * Change the number of days for a subscription
     *
     * @param name new name of 
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/changeName")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String changeName(SubscriptionName infoSubName, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons  = sessionSecuring.getConsumer(request);
        Subscription sub = null;
        if(cons != null){
            try{
                sub = cartBean.getUserSubscriptionByName(cons, infoSubName.getOldName());
                cartBean.changeName(sub, infoSubName.getNewName(), true);
            }
            catch(Exception ex){
                response.setStatus(400);
                return "";
            }
        }
        else{
            sub = getSubSession(request);
            if(sub != null){
                cartBean.changeName(sub, infoSubName.getNewName(), false);
            }
            else{
                response.setStatus(400);
                return "";
            }
        }
        return sub.getName();
    }
    

    /**
     * Return the cart
     *
     * @param request
     * @return the cart for the session
     */
    private Cart getCartSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return (Cart) session.getAttribute(CART_ATTR);
    }

    /**
     * Set the cart for the session
     *
     * @param request
     * @param cart the cart to add to the session
     */
    private void setCartSession(HttpServletRequest request, Cart cart) {
        HttpSession session = request.getSession(true);
        session.setAttribute(CART_ATTR, cart);
    }

    /**
     * Get the subscription for the session
     *
     * @param request
     * @return the subscription
     */
    private Subscription getSubSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return (Subscription) session.getAttribute(SUB_ATTR);
    }

    /**
     * Set the subscription
     *
     * @param request
     * @param sub the subscription
     */
    private void setSubSession(HttpServletRequest request, Subscription sub) {
        HttpSession session = request.getSession(true);
        session.setAttribute(SUB_ATTR, sub);
    }
}
