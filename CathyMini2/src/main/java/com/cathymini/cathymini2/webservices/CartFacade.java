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
    private static final String SUB_ATTR = "_SUB_ATTR";
    private static final Logger logger = Logger.getLogger(CartFacade.class);
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();

    @EJB
    private CartSession cartBean;
    @EJB
    private ProductBean productBean;

    
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
                cartBean.addProductToCart(prod, newCartTemp, false);
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
    
    @POST
    @Path("/changeQuantityToCart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int changeQuantityToCart(CartProduct clTemp, @Context HttpServletRequest request, @Context HttpServletResponse response){
        logger.debug("changeQuantity work :)");
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
    
    @POST
    @Path("/deleteToCart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int deleteToCart(Long id, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Product prod = productBean.getProduct(id);
        Consumer cons  = sessionSecuring.getConsumer(request);
        Cart cart = null;
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
    
    @POST
    @Path("/newSubscription")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String newSubscription(SubProduct subP, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons;
        String name = "abonnement1";
        
        cons  = sessionSecuring.getConsumer(request);
        cartBean.newSubscription(cons,name);
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
                    sub = cartBean.newSubscription(cons, "abonnement2");
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
                    newSubTemp = cartBean.newSubscription(null, "abonnement3");
                    setSubSession(request, newSubTemp);
               }
                cartBean.addProductToSub(prod, newSubTemp, false);
                return true;
            }
        } catch (Exception ex) {
            response.setStatus(400);
            return false;
            
        }
    }
    
    private Subscription getSubSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return (Subscription) session.getAttribute(SUB_ATTR);
    }
    
    private void setSubSession(HttpServletRequest request, Subscription sub) {
        HttpSession session = request.getSession(true);
        session.setAttribute(SUB_ATTR, sub);
    }
    
    @POST
    @Path("/getSub")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Subscription consumerIsConnectedSub(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Subscription sub = getSubSession(request);
        Consumer cons  = sessionSecuring.getConsumer(request);
        if(sub != null){
            cartBean.mergeSub(cons, sub);
            setSubSession(request, null);
        }
        try{
                Subscription cartToSend = null;//cartBean.getUserSubscription(cons);
                return cartToSend;
        }
        catch(Exception ex){
            response.setStatus(400);
            return null;
        }

    }
    
    @POST
    @Path("/changeQuantityToSub")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int changeQuantityToSub(SubProduct clTemp, @Context HttpServletRequest request, @Context HttpServletResponse response){
        logger.debug("changeQuantity work :)");
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
     
   /* @Path("/changeName")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String changeName(String name, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons  = sessionSecuring.getConsumer(request);
        Subscription sub = null;
        if(cons != null){
            try{
                sub = cartBean.getUserSubscriptionByName(cons, name);
                cartBean.changeName(sub, name, true);
            }
            catch(Exception ex){
                response.setStatus(400);
                return "";
            }
        }
        else{
            sub = getSubSession(request);
            if(sub != null){
                cartBean.changeName(sub, name, false);
            }
            else{
                response.setStatus(400);
                return "";
            }
        }
        return sub.getName();
    }*/
    
}
