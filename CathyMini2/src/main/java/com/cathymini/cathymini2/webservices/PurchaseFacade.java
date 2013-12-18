package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Cart;
import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.DeliveryAddress;
import com.cathymini.cathymini2.model.PaymentInfos;
import com.cathymini.cathymini2.model.Purchase;
import com.cathymini.cathymini2.model.PurchaseLine;
import com.cathymini.cathymini2.model.PurchaseSubscription;
import com.cathymini.cathymini2.services.CartSession;
import com.cathymini.cathymini2.services.PurchaseBean;
import com.cathymini.cathymini2.webservices.model.Payment;
import com.cathymini.cathymini2.webservices.model.form.PurchaseForm;
import com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring;
import com.cathymini.cathymini2.webservices.secure.Role;
import com.cathymini.cathymini2.webservices.secure.Secure;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    @EJB
    private CartSession cartBean;

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
    @Secure(Role.MEMBER)
    public String createPurchase(PurchaseForm form, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        Consumer user = sessionSecuring.getConsumer(request);
        
        Cart cart = cartBean.getUserCart(user);
        
        Collection<DeliveryAddress> addresses = user.getDeliveryCollection();
        DeliveryAddress selectedAddr = null;
        for (DeliveryAddress da : addresses){
            if(da.getDeliveryAddresID().compareTo(form.addressId) == 0) {
                selectedAddr = da;
            }
        }
            
        /* Block until Payement Infos undefined
        Collection<PaymentInfos> paymentInfos = user.getPaymentInfoCollection();
        PaymentInfos selectedPI;
        for (PaymentInfos pi : paymentInfos){
            if(pi.getPayementInfoID() == form.paymentInfoId) {
                selectedPI = pi;
            }
        }
        */
        PaymentInfos selectedPI = null;
        
        if (cart != null && selectedAddr != null) {
            purchaseBean.finalizePurchase(user, cart, selectedAddr, selectedPI);
            cartBean.clearCart(cart);
        }
        
        return "purchase validated";
    }
    
    @POST
    @Path("/createSubscription")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)

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
     * @param request
     * @param response
     * @return 
     * @throws java.lang.Exception
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
     * @param request
     * @param response
     * @return 
     * @throws java.lang.Exception 
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
        } else {
            PurchaseSubscription ps = new PurchaseSubscription();
            PaymentInfos pi = new PaymentInfos();
            Collection<PaymentInfos> col = new ArrayList<PaymentInfos>();
            col.add(pi);
            pi.setConsumer(user);
            pi.setInfo("Test d'un abonnement.");
            ps.setConsumer(user);
            ps.setDaysDelay(21);
            ps.setPurchaseLineCollection(new ArrayList<PurchaseLine>());
            ps.setPayementInfo(pi);
            Payment p = new Payment(ps);
            payments.add(p);
        }
        
        return payments;
    }
    
    /**
     * Get all subscriptions
     * @param response
     * @return 
     * @throws java.lang.Exception 
     */
    @POST
    @Path("/allSubscriptions")
    @Produces(MediaType.APPLICATION_JSON)
    //@Secure(Role.ADMIN)
    public Collection<Payment> getAllSubscriptions(@Context HttpServletResponse response) throws Exception {
        
        Collection<PurchaseSubscription> subscriptions = purchaseBean.getAllSubscriptions();
        Collection<Payment> payments = new ArrayList<Payment>();
        if (subscriptions != null) {
            for (Purchase subscription : subscriptions) {
                payments.add(new Payment(subscription));
            }
        } else {
            Calendar cal = Calendar.getInstance();
            PurchaseSubscription ps = new PurchaseSubscription();
            Consumer c = new Consumer();
            PaymentInfos pi = new PaymentInfos();
            Collection<PaymentInfos> col = new ArrayList<PaymentInfos>();
            col.add(pi);
            pi.setConsumer(c);
            pi.setInfo("Test d'un abonnement.");
            c.setUsername("Corentin");
            c.setMail("corentindijoux@gmail.com");
            c.setRole(Role.MEMBER);
            c.setPaymentInfoCollection(col);
            ps.setConsumer(c);
            ps.setDaysDelay(21);
            ps.setCreationDate(cal.getTimeInMillis());
            ps.setPayementDate(cal.getTimeInMillis());
            ps.setDeliveryDate(cal.getTimeInMillis());
            ps.setNextDelivery(cal.getTimeInMillis());
            ps.setPurchaseLineCollection(new ArrayList<PurchaseLine>());
            ps.setPayementInfo(pi);
            Payment p = new Payment(ps);
            payments.add(p);
            payments.add(p);
        }
        
        return payments;
    }
}
