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
    public String firstname;
    public String lastname;
    public String address;
    public String zipCode;
    public String city;
    public String country;

    public Address() {
    }

    public Address(DeliveryAddress addr) {
        id = addr.getDeliveryAddresID();
        
        firstname = addr.getFirstname();
        lastname = addr.getLastname();
        
        address = addr.getAddress();
        zipCode = addr.getZipCode();
        city = addr.getCity();
        country = addr.getCountry();
    }

    public boolean validate() {
        return checkString(address) && checkString(city) && checkZipCode(zipCode);
    }
}
