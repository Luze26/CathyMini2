package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.DeliveryAddress;
import com.cathymini.cathymini2.webservices.model.ConsumerApi;
import com.cathymini.cathymini2.webservices.model.form.Address;
import com.cathymini.cathymini2.webservices.secure.Role;
import javax.ejb.Stateless;
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
     * @throws Exception
     */
    public Consumer subscribeUser(String usr, String pwd, String mail) throws Exception {
        // Check if one of the field is null 
        if (usr == null || pwd == null || mail == null) {
                String message = "One of the field is <code>null</code>.";
                logger.error(message);
                throw new Exception(message);
        }
        
        // Check if the username or the mail is not already use
        if (findUserByName(usr) == null) {
            if (findUserByMail(mail) == null) {
                // Add the new consumer in the data base
                Consumer user = new Consumer();
                user.setUsername(usr);
                user.setPwd(pwd);
                user.setMail(mail);
                user.setRole(Role.MEMBER);

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
     * @throws Exception
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
    public void logout() {
        String message = "The user log out.";
        logger.debug(message);
    }

    public void updateUser(Consumer user, ConsumerApi newUser) throws Exception {
        if (user != null && newUser != null) {
            if (!user.getUsername().equals(newUser.username) && findUserByName(newUser.username) != null) {
                throw new Exception("username already exist");
            }
            if (!user.getMail().equals(newUser.mail) && findUserByMail(newUser.mail) != null) {
                throw new Exception("mail already exist");
            }
            user.setUsername(newUser.username);
            user.setMail(newUser.mail);
        }
    }

    public void addAddress(Consumer user, Address address) throws Exception {
        if (user != null && address != null) {
            DeliveryAddress delivery = new DeliveryAddress(user.getUsername(), user.getUsername(), address.address, address.zipCode, address.city);
            user.addDelivery(delivery);
        }
    }

    public void editAddress(Consumer user, Address address) throws Exception {
        if (user != null && address != null) {
            for (DeliveryAddress addr : user.getDeliveryCollection()) {
                if (addr.getDeliveryAddresID().equals(address.id)) {
                    addr.setAddress(address.address);
                    addr.setZipCode(address.zipCode);
                    addr.setCity(address.city);
                }
                break;
            }
        }
    }

    /**
     * Remove the user in parameter from the DB
     * @param usr Username
     * @param pwd Password
     * @throws Exception
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
    
    private Consumer findUserById(Long userID) {
        Query q = manager.createNamedQuery("ConsumerById", Consumer.class);
        q.setParameter("userID", userID);

        if (q.getResultList().isEmpty())
            return null; 

        return (Consumer) q.getResultList().get(0);
    }
}
