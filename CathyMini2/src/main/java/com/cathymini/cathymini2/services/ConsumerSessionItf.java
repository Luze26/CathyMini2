/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Consumer;
import javax.ejb.Remote;
import javax.ejb.Remove;

/**
 *
 * @author Kraiss
 */
@Remote
public interface ConsumerSessionItf {
    /**
     * Create a new User
     * @param usr Username
     * @param pwd Password
     * @param mail Mail Adress
     */
    public String suscribeUser(String usr, String pwd, String mail) throws Exception;
    
    /**
     * Connect the user 'usr'
     * @param usr Username
     * @param pwd Password
     */
    public String connectUser(String usr, String pwd) throws Exception;
    
    /**
     * Log the current user out.
     */
    @Remove
    public String logout();
    
    public String deleteUser(String usr, String pwd) throws Exception;
    
    @Override
    public String toString();
}
