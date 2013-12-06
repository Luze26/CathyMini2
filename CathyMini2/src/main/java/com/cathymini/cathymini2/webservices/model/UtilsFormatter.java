package com.cathymini.cathymini2.webservices.model;

import java.util.regex.Pattern;

/**
 *
 * @author yuzel
 */
public class UtilsFormatter {

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean checkString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean checkMail(String mail) {
        return mail != null && pattern.matcher(mail).matches();
    }
}
