package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.Purchase;
import com.cathymini.cathymini2.model.PurchaseSubscription;
import com.cathymini.cathymini2.services.PurchaseBean;
import com.cathymini.cathymini2.webservices.model.Payment;
import com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring;
import com.cathymini.cathymini2.webservices.secure.Role;
import com.cathymini.cathymini2.webservices.secure.Secure;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Kraiss
 */
@Stateless
@Path("/purchase")
public class PurchaseFacade {
    
    private static final Logger logger = Logger.getLogger(PurchaseFacade.class);
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();
    
    @EJB
    private PurchaseBean purchaseBean;

    /**
     * Create a purchase
     *
     * @param request
     * @param response
     * @return
     */
    @POST
    @Path("/createPurchase")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createPurchase(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return "Not Implemented";
    }
    
    @POST
    @Path("/createSubscription")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createSubscription(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return "Not Implemented";
    }
    
    @POST
    @Path("/editSubscription")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editSubscription(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return "Not Implemented";
    }
    
    @POST
    @Path("/stopSubscription")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String stopSubscription(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return "Not Implemented";
    }
    
    /**
     * Get purchases
     */
    @GET
    @Path("/purchases")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public Collection<Payment> getPurchases(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        Consumer user = sessionSecuring.getConsumer(request);
        Collection<Purchase> purchases = purchaseBean.getConsumerPurchases(user);
        Collection<Payment> payments = new ArrayList<Payment>();
        if (purchases != null) {
            for (Purchase purchase : purchases) {
                payments.add(new Payment(purchase));
            }
        } 
        return payments;
    }
    
    /**
     * Get subscriptions
     */
    @GET
    @Path("/subscriptions")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public Collection<Payment> getSubscriptions(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        Consumer user = sessionSecuring.getConsumer(request);
        Collection<PurchaseSubscription> subscriptions = purchaseBean.getConsumerSubscriptions(user);
        Collection<Payment> payments = new ArrayList<Payment>();
        if (subscriptions != null) {
            for (Purchase subscription : subscriptions) {
                payments.add(new Payment(subscription));
            }
        }
        
        return payments;
    }
}
