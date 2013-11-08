package com.cathymini.cathymini2.services;

import javax.ejb.*;
import com.cathymini.cathymini2.model.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 * The class {@link UserSession} is a stateful session to log in or suscribe {@link Consumer}
 * @author kraiss
 */
@Stateless
public class ConsumerSession { //implements ConsumerSessionItf {
    private Consumer user;
    
    @PersistenceContext(unitName="com.cathymini_CathyMini2_PU")
    private EntityManager em;
    
    private static final Logger logger = Logger.getLogger(ConsumerSession.class);
    
    
    /**
     * Create a new User
     * @param usr Username
     * @param pwd Password
     * @param mail Mail Adress
     */
    public String suscribeUser(String usr, String pwd, String mail) throws Exception {
        if (findUserByName(usr) == null) {
            if (findUserByMail(mail) == null) {
                user = new Consumer();
                user.setUsername(usr);
                user.setPwd(pwd);
                user.setMail(mail);

                em.persist(user);
                String message = "The user suscribe with success.";
                logger.debug(message);
                return message;
            } else {
                String message = "This mail address is already used by another user.";
                logger.error(message);
                throw new Exception(message);
            }
        } else {
                String message = "This username already exist.";
                logger.error(message);
                throw new Exception(message);
        }
    }
    
    /**
     * Connect the user 'usr'
     * @param usr Username
     * @param pwd Password
     */
    public String connectUser(String usr, String pwd) throws Exception {
        Consumer consumer;
        if (usr.contains("@")) { // Decide if 'usr' is a mail address or a username
            consumer = findUserByMail(usr);
        } else {
            consumer = findUserByName(usr);
        }
        
        if (consumer != null) {
            if (consumer.getPwd().equals(pwd)) {
                String message = "The user "+usr+" is connected.";
                logger.debug(message);
                user = consumer;
                return message;
            } else {
                // Error : This user exists but the pwd is wrong
                String message = "This user does not exist or the password is wrong.";
                logger.error(message);
                throw new Exception(message);
            }
            
        } else {
            // Error : This user does not exist
            String message = "This user does not exist or the password is wrong.";
            logger.error(message);
            throw new Exception(message);
        }
    }
    
    /**
     * Log the current user out.
     */
    @Remove
    public String logout() {
        String message = "The user log out.";
        logger.error(message);
        return message;
    }
    
    /**
     * Delete the user 'usr'
     * @param usr
     * @param pwd
     */
    public String deleteUser(String usr, String pwd) throws Exception {
        connectUser(usr, pwd);
            
        if (user == null) {
            String message = "This user cannot be deleted.";
            logger.error(message);
            throw new Exception(message);
        } else {
            String message = "This user has been deleted.";
            logger.error(message);
            deleteUser(user);
            return message;
        }
        
    }
    
    @Override
    public String toString() {
        //logger.debug("See Session = " + user.getUsername() +" :: "+ user.getPwd() +" :: " + user.getMail());
        if (user != null)
            return "You are connected as "+user.getUsername()+".";
        else 
            return "User not defined ..";
    }
    
    private void deleteUser(Consumer user) {
        em.merge(user); // enforse synch with DB
        em.remove(user);
    }
    
    private Consumer findUserByName(String username) {
        Query q = em.createNamedQuery("ConsumerByName", Consumer.class); 
        q.setParameter("username", username);
        
        if (q.getResultList().isEmpty())
            return null; 
        
        return (Consumer) q.getResultList().get(0);
    }
    
    private Consumer findUserByMail(String mail) {
        Query q = em.createNamedQuery("ConsumerByMail", Consumer.class); 
        q.setParameter("mail", mail);
        
        if (q.getResultList().isEmpty())
            return null; 
        
        return (Consumer) q.getResultList().get(0);
    }
}
