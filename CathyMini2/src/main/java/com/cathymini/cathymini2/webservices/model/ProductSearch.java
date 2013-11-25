package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.services.ProductBean;
import java.text.DecimalFormat;
import java.util.List;

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
    public String brand;
    public List<String> flux;
    

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

            if(minPrice < 0)
                minPrice = 0.00f;
            if (maxPrice < 0)
                maxPrice = 0.00f;
            
            if (minPrice > maxPrice){
                Float tmp = minPrice;
                minPrice = maxPrice;
                maxPrice = tmp;
            }
        }
        for(String s : flux){
            switch(Integer.parseInt(s)){
                case(1): s="1.0";
                case(2): s="2.0";
                case(3): s="3.0";
                case(4): s="4.0";
                case(5): s="5.0";
                case(6): s="6.0";
                default: s="0.0";
            }
        }
        System.out.println(flux);
        if(length == 0) {
            length = 10;
        }
     
    }
}
