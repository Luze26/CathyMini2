package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.model.PayementInfo;
import com.cathymini.cathymini2.model.Purchase;
import com.cathymini.cathymini2.model.PurchaseLine;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 *
 * @author yuzel
 */
public class Payment {
    String creationDate;
    String paymentDate;
    String deliveryDate;
    
    Integer cost;
    Collection<Payment.PurchaseProduct> products;
    
    /** Inner class to associate a product to a quantity */
    class PurchaseProduct {
        public String product; // product name
        public Integer quantity;
    }

    public Payment() {
    }

    public Payment(Purchase purchase) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(purchase.getCreationDate());
        this.creationDate = cal.getTime().toString();
        
        cal.setTimeInMillis(purchase.getDeliveryDate());
        this.deliveryDate = cal.getTime().toString();
        
        cal.setTimeInMillis(purchase.getPayementDate());
        this.paymentDate = cal.getTime().toString();
        
        this.cost = purchase.getTotalCost();
        
        this.products = new ArrayList<Payment.PurchaseProduct>();
        for(PurchaseLine pl : purchase.getPurchaseLineCollection()) {
            PurchaseProduct pp = new PurchaseProduct();
            pp.product = pl.getProduct().getName();
            pp.quantity = pl.getQuantity();
            this.products.add(pp);
        }
   }
}
