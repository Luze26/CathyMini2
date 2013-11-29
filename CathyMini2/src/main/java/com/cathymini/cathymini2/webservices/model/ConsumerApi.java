package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.model.Consumer;
import java.io.Serializable;

/**
 *
 * @author yuzel
 */
public class ConsumerApi implements Serializable {

    public ConsumerApi(Consumer user) {
        this.username = user.getUsername();
        this.mail = user.getMail();
    }

    public String username;

    /**
     * Consumer mail address
     */
    public String mail;
}
