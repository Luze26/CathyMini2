/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices.secure;

import com.cathymini.cathymini2.model.Consumer;
import java.util.Calendar;

/**
 *
 * @author Kraiss
 */
public class SecureEntry {
    public Long timestamp;
    public Consumer consumer;

    public SecureEntry(Consumer consumer) {
        this.consumer = consumer;
        updateTimer();
    }
    
    public void updateTimer() {
        this.timestamp = Calendar.getInstance().getTimeInMillis();
    }
}
