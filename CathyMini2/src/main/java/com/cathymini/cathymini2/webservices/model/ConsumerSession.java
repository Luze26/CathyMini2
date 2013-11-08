/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.model.Consumer;

/**
 *
 * @author Kraiss
 */
public class ConsumerSession {
    public String username;
    public Long id;
    
    public static ConsumerSession getSession(Consumer consumer) {
        ConsumerSession cs = new ConsumerSession();
        cs.username = consumer.getUsername();
        cs.id = consumer.getUserID();
        return cs;
    }
}
