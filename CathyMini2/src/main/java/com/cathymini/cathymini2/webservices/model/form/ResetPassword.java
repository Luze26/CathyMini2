package com.cathymini.cathymini2.webservices.model.form;

import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkString;

/**
 *
 * @author yuzel
 */
public class ResetPassword {

    public String username;
    public String token;
    public String pwd;
    public String confirmPwd;

    public boolean validate() {
        return checkString(username) && checkString(token) && checkString(pwd) && pwd.equals(confirmPwd) && pwd.length() >= 3;
    }
}
