package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.model.Purchase;
import com.cathymini.cathymini2.model.PurchaseLine;
import com.cathymini.cathymini2.model.PurchaseSubscription;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 *
 * @author yuzel
 */
public class Payment {
    /** Inner class to associate a product to a quantity */
    public class PurchaseProduct {
        public String product; // product name
        public Integer quantity;

        public PurchaseProduct(String name, Integer quantity) {
            this.product = name;
            this.quantity = quantity;
        }
    }
    
    private final static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    
    public String creationDate;
    public String paymentDate;
    public String deliveryDate;
    public String nextDelivery;
    
    public Integer daysDelay;
    public Integer cost;
    public Collection<Payment.PurchaseProduct> products;

    public Payment() {
    }

    public Payment(Purchase purchase) {
        Calendar cal = Calendar.getInstance();
        if (purchase.getCreationDate() != null) {
            cal.setTimeInMillis(purchase.getCreationDate());
            this.creationDate = formatter.format(cal.getTime().toString());
        } else {
            this.creationDate = "";
        }
        
        if (purchase.getDeliveryDate() != null) {
            cal.setTimeInMillis(purchase.getDeliveryDate());
            this.deliveryDate = formatter.format(cal.getTime().toString());
        } else {
            this.deliveryDate = "";
        }
        
        if (purchase.getPayementDate() != null) {
        cal.setTimeInMillis(purchase.getPayementDate());
        this.paymentDate = formatter.format(cal.getTime().toString());
        } else {
            this.paymentDate = "";
        }
        
        
        this.cost = purchase.getTotalCost();
        
        this.products = new ArrayList<Payment.PurchaseProduct>();
        for(PurchaseLine pl : purchase.getPurchaseLineCollection()) {
            PurchaseProduct pp = new PurchaseProduct(pl.getProduct().getName(), pl.getQuantity());
            this.products.add(pp);
        }
    }
    
    public Payment(PurchaseSubscription subscription) {
        Calendar cal = Calendar.getInstance();
        if (subscription.getCreationDate() != null) {
            cal.setTimeInMillis(subscription.getCreationDate());
            this.creationDate = formatter.format(cal.getTime().toString());
        } else {
            this.creationDate = "";
        }
        
        if (subscription.getDeliveryDate() != null) {
            cal.setTimeInMillis(subscription.getDeliveryDate());
            this.deliveryDate = formatter.format(cal.getTime().toString());
        } else {
            this.deliveryDate = "";
        }
        
        if (subscription.getPayementDate() != null) {
        cal.setTimeInMillis(subscription.getPayementDate());
        this.paymentDate = formatter.format(cal.getTime().toString());
        } else {
            this.paymentDate = "";
        }
        
        if (subscription.getNextDelivery()!= null) {
        cal.setTimeInMillis(subscription.getNextDelivery());
        this.nextDelivery = formatter.format(cal.getTime().toString());
        } else {
            this.nextDelivery = "";
        }
        
        this.daysDelay = subscription.getDaysDelay();
        this.cost = subscription.getTotalCost();
        
        this.products = new ArrayList<Payment.PurchaseProduct>();
        for(PurchaseLine pl : subscription.getPurchaseLineCollection()) {
            PurchaseProduct pp = new PurchaseProduct(pl.getProduct().getName(), pl.getQuantity());
            this.products.add(pp);
        }
   }
}
