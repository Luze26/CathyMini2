/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices.model.form;

import java.text.DecimalFormat;

/**
 *
 * @author zang
 */
public class AddProduct {

    public String name;
    public Float price;
    
    public boolean validate() {
        if(name != null && price != null) {
            name = name.trim();
            return name.length() > 0;
        }
        
        DecimalFormat df = new DecimalFormat("##.##");
        price =  new Float(df.format(price.doubleValue()));
        
        return false;
    }
}
