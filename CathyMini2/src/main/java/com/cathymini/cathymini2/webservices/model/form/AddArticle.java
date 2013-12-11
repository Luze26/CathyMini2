/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices.model.form;

import java.text.DecimalFormat;

/**
 *
 * @author Flo
 */
public class AddArticle {

    public String titre;
    public String description;
    public String type;
    public String detail;
    public String image;
    
    public boolean validate() {
        if(titre != null && description != null && (type.equals("promo") || type.equals("news"))) {
            titre = titre.trim();
            description = description.trim();
            detail = detail.trim();
            return titre.length() > 0;
        }
        
        return false;
    }
}
