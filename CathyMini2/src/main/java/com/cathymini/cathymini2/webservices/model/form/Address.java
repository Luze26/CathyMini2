package com.cathymini.cathymini2.webservices.model.form;

import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkString;
import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkZipCode;

/**
 *
 * @author yuzel
 */
public class Address {
    public String address;
    public String zipCode;
    public String city;

    public boolean validate() {
        return checkString(address) && checkString(city) && checkZipCode(zipCode);
    }
}
