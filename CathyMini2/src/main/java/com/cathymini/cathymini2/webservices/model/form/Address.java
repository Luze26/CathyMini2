package com.cathymini.cathymini2.webservices.model.form;

import com.cathymini.cathymini2.model.DeliveryAddress;
import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkString;
import static com.cathymini.cathymini2.webservices.model.UtilsFormatter.checkZipCode;

/**
 *
 * @author yuzel
 */
public class Address {

    public Long id;
    public String address;
    public String zipCode;
    public String city;

    public Address() {
    }

    public Address(DeliveryAddress addr) {
        id = addr.getDeliveryAddresID();
        address = addr.getAddress();
        zipCode = addr.getZipCode();
        city = addr.getCity();
    }

    public boolean validate() {
        return checkString(address) && checkString(city) && checkZipCode(zipCode);
    }
}
