package com.cathymini.cathymini2.webservices.model;


/**
 *
 * @author AnaelleBernard
 */
public class SubProduct {

    public Long productId;
    public int quantity;
    public String name;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
