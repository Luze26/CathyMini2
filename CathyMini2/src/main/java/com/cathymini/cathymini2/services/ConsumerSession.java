package com.cathymini.cathymini2.services;

import javax.ejb.Remove;
import com.cathymini.cathymini2.model.Consumer;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import org.apache.log4j.Logger;

/**
 * The class {@link UserSession} is a stateful session to log in or suscribe {@link Consumer}
 * @author kraiss
 */
@Stateful
public class ConsumerSession {
    private Consumer user;
    
    @EJB
    private ConsumerBean consumerBean;
    private static final Logger logger = Logger.getLogger(ConsumerSession.class);
    
    
    /**
     * Create a new User
     * @param usr Username
     * @param pwd Password
     * @param mail Mail Adress
     */
    public void suscribeUser(String usr, String pwd, String mail) {
        try {
            user = consumerBean.suscribeUser(usr, pwd, mail);
        logger.debug("Session = " + user.getUsername() +" :: "+ user.getPwd() +" :: " + user.getMail());
        } catch (Exception ex) {
            logger.info(ex);
            // TODO raise to IHM
        }
    }
    
    /**
     * Connect the user 'usr'
     * @param usr Username
     * @param pwd Password
     */
    public void connectUser(String usr, String pwd){
        try {
            user = consumerBean.connectUser(usr, pwd);
        } catch (Exception ex) {
            logger.info(ex);
            // TODO raise to IHM
        }
    }
    
    @Remove
    /**
     * Log the current user out.
     */
    public void logout() {}
    
    public void deleteUser(String usr, String pwd) {
        Consumer cons = user;
        logout();
        try {
            consumerBean.deleteUser(cons);
        } catch (Exception ex) {
            logger.info(ex);
            // TODO raise to IHM
        }
    }
    
    @Override
    public String toString() {
        logger.debug("See Session = " + user.getUsername() +" :: "+ user.getPwd() +" :: " + user.getMail());
        return "You are connected as "+user.getUsername()+".";
    }
    
}
