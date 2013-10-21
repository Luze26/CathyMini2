package com.cathymini.cathymini2.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.cathymini.cathymini2.model.Consumer;
import org.apache.log4j.Logger;

/**
 * The class {@link UserSession} is a stateful session to log in or suscribe {@link Consumer}
 * @author kraiss
 */
@Stateless
public class ConsumerBean {
    @PersistenceContext(unitName="com.cathymini_CathyMini2_PU")
    private EntityManager em;
    
    private static final Logger logger = Logger.getLogger(ConsumerBean.class);
    
    /**
     * Create a new User in the DataBase.
     * @param usr Username
     * @param pwd Password
     * @param mail Mail Adress
     */
    public Consumer suscribeUser(String usr, String pwd, String mail) throws Exception {
        if (findUserByName(usr) == null) {
            if (findUserByMail(mail) == null) {
                Consumer user;
                user = new Consumer();
                user.setUsername(usr);
                user.setPwd(pwd);
                user.setMail(mail);

                em.persist(user);
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
    
    public Consumer connectUser(String usr, String pwd) throws Exception {
        Consumer user;
        if (usr.contains("@")) { // Decide if 'usr' is a mail address or a username
            user = findUserByMail(usr);
        } else {
            user = findUserByName(usr);
        }
        
        if (user != null) {
            if (user.getPwd().equals(pwd)) {
                String message = "The user "+usr+" is connected.";
                logger.debug(message);
                return user;
                
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
    
    public void deleteUser(String usr, String pwd) throws Exception {
        Consumer user;
        try {
            user = connectUser(usr, pwd);
        } catch (Exception e) {
            String message = e.getMessage() + "This user cannot be deleted.";
            logger.error(message);
            throw new Exception(message);
        }
        
        deleteUser(user);
    }
    
    public void deleteUser(Consumer user) {
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
