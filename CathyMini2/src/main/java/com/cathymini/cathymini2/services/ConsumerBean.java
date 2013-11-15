package com.cathymini.cathymini2.services;

import javax.ejb.*;
import com.cathymini.cathymini2.model.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 * The class {@link ConsumerBean} is a stateless session bean to log in or suscribe a {@link Consumer}
 * @author kraiss
 */
@Stateless
public class ConsumerBean { 
    @PersistenceContext(unitName="com.cathymini_CathyMini2_PU")
    private EntityManager manager;
    
    private static final Logger logger = Logger.getLogger(ConsumerBean.class);
    
    /**
     * Create a new User
     * @param usr Username
     * @param pwd Password
     * @param mail Mail Adress
     */
    public Consumer suscribeUser(String usr, String pwd, String mail) throws Exception {
        if (findUserByName(usr) == null) {
            if (findUserByMail(mail) == null) {
                Consumer user = new Consumer();
                user.setUsername(usr);
                user.setPwd(pwd);
                user.setMail(mail);

                manager.persist(user);
                String message = "The user suscribe with success.";
                logger.debug(message);
                return user;
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
    public Consumer connectUser(String usr, String pwd) throws Exception {
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
                return consumer;
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
    public void logout() {
        String message = "The user log out.";
        logger.error(message);
    }
    
    /**
     * Remove the user in parameter from the DB
     */
    public void deleteUser(String usr, String pwd) throws Exception {
        Consumer consumer;
        if (usr.contains("@")) { // Decide if 'usr' is a mail address or a username
            consumer = findUserByMail(usr);
        } else {
            consumer = findUserByName(usr);
        }
        
        if (consumer != null) {
            if (consumer.getPwd().equals(pwd)) {
                String message = "The user "+usr+" has been deleted from DB.";
                logger.debug(message);
                // De-comment if needed 
                //manager.merge(user); // enforse synch with DB
                manager.remove(consumer);
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
    
    private Consumer findUserByName(String username) {
        Query q = manager.createNamedQuery("ConsumerByName", Consumer.class); 
        q.setParameter("username", username);
        
        if (q.getResultList().isEmpty())
            return null; 
        
        return (Consumer) q.getResultList().get(0);
    }
    
    private Consumer findUserByMail(String mail) {
        Query q = manager.createNamedQuery("ConsumerByMail", Consumer.class); 
        q.setParameter("mail", mail);
        
        if (q.getResultList().isEmpty())
            return null; 
        
        return (Consumer) q.getResultList().get(0);
    }
}
