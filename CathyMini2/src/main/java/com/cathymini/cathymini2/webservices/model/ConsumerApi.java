package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.model.Consumer;
import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkMail;
import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkString;

/**
 *
 * @author yuzel
 */
public class ConsumerApi {

    public ConsumerApi() {
    }

    public ConsumerApi(Consumer user) {
        this.username = user.getUsername();
        this.mail = user.getMail();
    }

    public String username;

    /**
     * Consumer mail address
     */
    public String mail;

    public boolean validate() {
        return checkString(username) && checkMail(mail);
    }
}
