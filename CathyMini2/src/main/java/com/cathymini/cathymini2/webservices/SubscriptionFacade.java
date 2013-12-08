/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Cart;
import com.cathymini.cathymini2.model.CartLine;
import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.model.Subscription;
import com.cathymini.cathymini2.services.CartSession;
import com.cathymini.cathymini2.services.SubscriptionBean;
import com.cathymini.cathymini2.services.ProductBean;
import com.cathymini.cathymini2.webservices.model.CartProduct;
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
 * @author anaelle
 */
@Path("/sub")
public class SubscriptionFacade {
        private static final String SUB_ATTR = "_SUB_ATTR";
    private static final Logger logger = Logger.getLogger(SubscriptionFacade.class);
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();

    @EJB
    private SubscriptionBean subBean;
    @EJB
    private ProductBean productBean;

    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean add(Long id, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Consumer cons;
        try{
           // logger.debug("rechercher cons");
            cons = sessionSecuring.getConsumer(request);
        }catch(Exception ex){
            cons = null;
        }
        try{
            if (cons != null) {
             //   logger.debug("cons pas null");
                Subscription sub  = subBean.getUserSubscription(cons);
               // logger.debug("recupération sub");
                if(sub == null){
                 //   logger.debug("creation new sub");
                    sub = subBean.newSubscription(cons);
                }
                Product prod = productBean.getProduct(id);
                //logger.debug("prod récupéré");
                subBean.addProduct(prod, sub, true);
                //logger.debug("produit ajouté!!");
                return true;
            }
            else{
                //logger.debug("cons null");
                Product prod = productBean.getProduct(id);
                //logger.debug("prod récupéré");
                Subscription newSubTemp = null;
                Boolean noSub = false;
               try{
                    newSubTemp = getSubSession(request);
                   // logger.debug("recupération sub temp");
                    if(newSubTemp == null)
                        noSub = true;
               }
               catch(Exception ex){
                   noSub = true;
                   return false;
               }
               
               if(noSub){
                   //logger.debug("creation subTemp avec cons null");
                    newSubTemp = subBean.newSubscription(null);
                    setSubSession(request, newSubTemp);
                    //logger.debug("mise deans session");
               }
                subBean.addProduct(prod, newSubTemp, false);
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
    @Path("/consumerIsConnected")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Subscription consumerIsConnected(@Context HttpServletRequest request, @Context HttpServletResponse response){
        Subscription sub = getSubSession(request);
        Consumer cons  = sessionSecuring.getConsumer(request);
        if(sub != null){
            subBean.mergeCart(cons, sub);
            setSubSession(request, null);
        }
        try{
                Subscription cartToSend = subBean.getUserSubscription(cons);
                return cartToSend;
        }
        catch(Exception ex){
            response.setStatus(400);
            return null;
        }

    }
    
    @POST
    @Path("/changeQuantity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int changeQuantity(CartProduct clTemp, @Context HttpServletRequest request, @Context HttpServletResponse response){
        logger.debug("changeQuantity work :)");
        Consumer cons  = sessionSecuring.getConsumer(request);
        if(cons != null){
            try{
                Subscription sub = subBean.getUserSubscription(cons);
                CartLine cl = subBean.getCartLineByID(Long.parseLong(String.valueOf(clTemp.getProductId())), sub);
                subBean.changeQuantityCartLine(cl, clTemp.getQuantity(), true);
            }
            catch(Exception ex){
                response.setStatus(400);
                return -1;
            }
        }
        else{
            Subscription sub = getSubSession(request);
            if(sub != null){
                CartLine cl = subBean.getCartLineByID(Long.parseLong(String.valueOf(clTemp.getProductId())), sub);
                subBean.changeQuantityCartLine(cl, clTemp.getQuantity(), false);
            }
            else{
                response.setStatus(400);
                return -1;
            }
        }
        return clTemp.getQuantity();
    }
    
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int delete(Long id, @Context HttpServletRequest request, @Context HttpServletResponse response){
        Product prod = productBean.getProduct(id);
        Consumer cons  = sessionSecuring.getConsumer(request);
        Subscription sub = null;
        int place = -1;
        if(cons != null){
            try{
                sub = subBean.getUserSubscription(cons);
            }
            catch(Exception ex){
                response.setStatus(400);
                return -1;
            }
        }
        else{
            sub = getSubSession(request);
        }
        place = subBean.removeProduct(prod, sub);
        return place;
    }
}
