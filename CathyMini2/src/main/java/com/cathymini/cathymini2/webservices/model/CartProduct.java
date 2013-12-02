package com.cathymini.cathymini2.webservices.model;


/**
 *
 * @author yuzel
 */
public class CartProduct {

    public Long productId;
    public int quantity;
    
        public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
