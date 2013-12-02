package com.cathymini.cathymini2.webservices.model.form;

import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkMail;
import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkString;

/**
 * The model {@link Subscribe} is used to representing a JSon object for the subscription form
 * @author Kraiss
 */
public class Subscribe {
    public String username;
    public String pwd;
    public String confirmPwd;
    public String mail;

    public boolean validate() {
        return checkString(username) && checkMail(mail) && checkString(pwd) && pwd.equals(confirmPwd);
    }
}
