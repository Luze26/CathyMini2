package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.services.ProductBean;
import java.text.DecimalFormat;

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
    public boolean tampon;
    public boolean napkin;
    public Float minPrice;
    public Float maxPrice;

    public void validate() {
        try {
            ProductBean.ProductKeys.valueOf(orderBy.toUpperCase());
            orderBy = orderBy.toLowerCase();
        }
        catch(Exception e) {
            orderBy = "id";
        }
        if( minPrice != null && maxPrice != null) {
            DecimalFormat mindf = new DecimalFormat("##.##");
            minPrice =  new Float(mindf.format(minPrice.doubleValue()));

            DecimalFormat maxdf = new DecimalFormat("##.##");
            maxPrice =  new Float(maxdf.format(maxPrice.doubleValue()));

            if (minPrice > maxPrice){
                Float tmp = minPrice;
                minPrice = maxPrice;
                maxPrice = tmp;
            }
        }
        if(length == 0) {
            length = 10;
        }
     
    }
}
