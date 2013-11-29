package com.cathymini.cathymini2.webservices.model.form;

import java.text.DecimalFormat;

/**
 *
 * @author yuzel
 */
public class EditProduct {
    public Long id;
    public String name;
    public String marque;
    public Float flux;
    public Float price;
    public String description;
    
    public boolean validate() {
        if(id != null && id >= 0) {
            if(name != null && price != null) {
                name = name.trim();
                return name.length() > 0;
            }

            DecimalFormat df = new DecimalFormat("##.##");
            price =  new Float(df.format(price.doubleValue()));
        }
        
        return false;
    }   
}
