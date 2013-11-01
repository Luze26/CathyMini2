package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.services.ProductBean;

/**
 *
 * @author yuzel
 */
public class ProductSearch {
    public int offset;
    public int length;
    public String orderBy;
    public boolean orderByASC;
    public String input;
    public Integer minPrice;
    public Integer maxPrice;
    
    public void validate() {
        try {
            ProductBean.ProductKeys.valueOf(orderBy.toUpperCase());
            orderBy = orderBy.toLowerCase();
        }
        catch(Exception e) {
            orderBy = "id";
        }
        
        if(length == 0) {
            length = 10;
        }
    }
}
