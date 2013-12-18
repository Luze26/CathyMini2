/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.services.ConsumerBean;

/**
 *
 * @author Corentin
 */
public class SubscriptionSearch {
    
    public int offset;
    public int length;
    public String orderBy;
    public boolean orderByASC;
    public String input;
    
    
    public void validate() {
        try {
            ConsumerBean.ConsumerKeys.valueOf(orderBy.toUpperCase());
            orderBy = orderBy.toLowerCase();
            if(orderBy.equals("id")) {
                orderBy = "userID";
            }
        }
        catch(Exception e) {
            orderBy = "userID";
        }
        if(length == 0) {
            length = 10;
        }
    }
}
