package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.DeliveryAddress;
import com.cathymini.cathymini2.services.ConsumerBean;
import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkMail;
import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkString;
import java.util.Collection;

/**
 *
 * @author yuzel
 */
public class ConsumerApi {

    /**
     * Consumer id
     */
    public Long id;
    
    /**
     * Consumer username
     */
    public String username;

    /**
     * Consumer mail address
     */
    public String mail;
    
    /**
     * Consumer collection delivery address
     */
    public Collection<DeliveryAddress> address;

    public ConsumerApi() {
    }

    public ConsumerApi(Consumer user) {
        this.id = user.getUserID();
        this.username = user.getUsername();
        this.mail = user.getMail();
        this.address = user.getDeliveryCollection();
    }

    public boolean validate() {
        return checkString(username) && checkMail(mail) && (id != null);
    }
}
