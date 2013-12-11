package com.cathymini.cathymini2.webservices.model;

import com.cathymini.cathymini2.model.PayementInfo;

/**
 *
 * @author yuzel
 */
public class Payment {

    public String info;

    public Payment() {
    }

    public Payment(PayementInfo payment) {
        info = payment.getInfo();
    }
}
