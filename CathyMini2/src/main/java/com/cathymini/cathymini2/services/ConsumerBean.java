package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.DeliveryAddress;
import com.cathymini.cathymini2.webservices.model.ConsumerApi;
import com.cathymini.cathymini2.webservices.model.ConsumerSearch;
import com.cathymini.cathymini2.webservices.model.form.Address;
import com.cathymini.cathymini2.webservices.model.form.Connect;
import com.cathymini.cathymini2.webservices.secure.Role;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
* The class {@link ConsumerBean} is a stateless session bean to log in or
* suscribe a {@link Consumer}
*
* @author kraiss
*/
@Stateless
public class ConsumerBean {

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    @Resource(name = "mail/mailSession")
    private Session mailSession;

    private static final Logger logger = Logger.getLogger(ConsumerBean.class);

    /**
* Properties of a consumer (used to order by)
*/
    public enum ConsumerKeys {

        ID, USERNAME, MAIL
    }

    /**
* Create a new User
*
* @param usr Username
* @param pwd Password
* @param mail Mail Address
* @return The new consumer data
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
*
* @param usr Username
* @param pwd Password
* @return The consumer data
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
                String message = "The user " + usr + " is connected.";
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

    public void editConsumer(ConsumerApi newUser) throws Exception {
        Consumer user = this.findUserById(newUser.id);
        this.updateUser(user, newUser);
        if (newUser.address != null && !user.getDeliveryCollection().equals(newUser.address)) {
            for (DeliveryAddress a : newUser.address) {
                this.editAddress(user, new Address(a));
            }
        }
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
            manager.merge(user);
        }
    }

    public void addAddress(Consumer user, Address address) throws Exception {
        if (user != null && address != null) {
            DeliveryAddress delivery = new DeliveryAddress(user.getUsername(), user.getUsername(), address.address, address.zipCode, address.city);
            manager.persist(delivery);
            user.addDelivery(delivery);
            manager.merge(user);
        }
    }

    public void editAddress(Consumer user, Address address) throws Exception {
        if (user != null && address != null) {
            for (DeliveryAddress addr : user.getDeliveryCollection()) {
                if (addr.getDeliveryAddresID().equals(address.id)) {
                    addr.setAddress(address.address);
                    addr.setZipCode(address.zipCode);
                    addr.setCity(address.city);
                    manager.merge(addr);
                    manager.merge(user);
                }
                break;
            }
        }

    }

    public void deleteAddress(Consumer user, Address address) {
        if (user != null && address != null) {
            for (DeliveryAddress addr : user.getDeliveryCollection()) {
                if (addr.getDeliveryAddresID().equals(address.id)) {
                    Query query = manager.createNamedQuery("deleteByIdAddress", DeliveryAddress.class);
                    query.setParameter("id", address.id);
                    query.executeUpdate();
                    user.deleteDelivery(addr);
                }
                break;
            }
        }
    }

    /**
* Remove the user in parameter from the DB
*
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
                String message = "The user " + usr + " has been deleted from DB.";
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

    /**
* Remove the user in parameter from the DB
*
* @param id
* @return Information of consumer deleted
* @throws Exception
*/
    public Connect deleteUser(Long id) throws Exception {
        Consumer c = this.findUserById(id);
        Connect connect = new Connect();
        connect.user = c.getUsername();
        connect.pwd = c.getPwd();
        this.deleteUser(connect.user, connect.pwd);
        return connect;
    }

    /**
* Generate a token to reset a password and send the mail
*
* @param usr user who wants to reset his password
* @return generated token or null if the user doesn't exists
*/
    public String resetPassword(String usr) throws Exception {
        Consumer user = findUserByName(usr);
        if (user != null) {
            try {
                SecureRandom random = new SecureRandom();
                String token = new BigInteger(130, random).toString(32);
                user.setToken(token);
                manager.merge(user);

                //Send mail
                Message message = new MimeMessage(mailSession);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getMail(), false));
                message.setSubject("Demande de nouveau mot de passe");
                String url = "http://localhost:8080/resetPassword.xhtml?username=" + URLEncoder.encode(user.getUsername(), "UTF-8")
                        + "&token=" + user.getToken();
                String html = "<p>Bonjour,<br/>Une demande de nouveau mot de passe pour CathyMini a été faite.<br/>"
                        + "Si vous en êtes pas l'auteur, merci d'ignorer cet email. Sinon suivez le lien en-dessous:<br/>"
                        + "<a href=\"" + url + "\">" + url + "</a>";
                message.setContent(html, "text/html; charset=utf-8");
                Date timeStamp = new Date();
                message.setSentDate(timeStamp);
                Transport.send(message);

                return token;
            } catch (MessagingException ex) {
                throw new Exception("mail not sent");
            }
        }
        throw new Exception("user not found");
    }

    /**
* Check if a token match the token of the given user
*
* @param usr user
* @param token token
* @param newPassword new password
* @throws java.lang.Exception "user not found", "token incorrect"
*/
    public void resetPassword(String usr, String token, String newPassword) throws Exception {
        Consumer user = findUserByName(usr);
        if (user != null) {
            if (user.getToken() != null && user.getToken().equals(token)) {
                user.setToken(null);
                user.setPwd(newPassword);
                manager.merge(user);
            } else {
                throw new Exception("token incorrect");
            }
        } else {
            throw new Exception("user not found");
        }
    }

    private Consumer findUserByName(String username) {
        if (username == null) {
            return null;
        }

        Query q = manager.createNamedQuery("ConsumerByName", Consumer.class);
        q.setParameter("username", username);
        List results = q.getResultList();

        if (results.isEmpty()) {
            return null;
        }

        return (Consumer) results.get(0);
    }

    private Consumer findUserByMail(String mail) {
        Query q = manager.createNamedQuery("ConsumerByMail", Consumer.class);
        q.setParameter("mail", mail);

        if (q.getResultList().isEmpty()) {
            return null;
        }

        return (Consumer) q.getResultList().get(0);
    }

    public Consumer findUserById(Long userID) {
        Query q = manager.createNamedQuery("ConsumerById", Consumer.class);
        q.setParameter("userID", userID);

        if (q.getResultList().isEmpty()) {
            return null;
        }

        return (Consumer) q.getResultList().get(0);
    }

    public Collection<Consumer> getUsers(ConsumerSearch searchQuery) {
        searchQuery.validate();
        Query query = constructQuery(searchQuery);
        if (query != null) {
            return (Collection<Consumer>) query.getResultList();
        } else {
            return new ArrayList<Consumer>();
        }
    }

    public Query constructQuery(ConsumerSearch searchQuery) {
        String query = "SELECT c FROM Consumer c WHERE";

        //INPUT
        if (searchQuery.input != null) {
            query += " c.username LIKE '%" + searchQuery.input + "%'";
        }

        //ORDER
        query += " ORDER BY c." + searchQuery.orderBy + " " + (searchQuery.orderByASC ? "ASC" : "DESC");

        return manager.createQuery(query).setFirstResult(searchQuery.offset).setMaxResults(searchQuery.length);
    }
}