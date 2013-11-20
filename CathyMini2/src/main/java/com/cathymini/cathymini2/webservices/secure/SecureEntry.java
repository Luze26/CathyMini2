/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices.secure;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.webservices.model.ConsumerSession;
import java.util.Calendar;

/**
 *
 * @author Kraiss
 */
public class SecureEntry {
    public ConsumerSession session;
    public Consumer consumer;
    public Long timestamp;

    public SecureEntry(ConsumerSession session, Consumer consumer) {
        this.session = session;
        this.consumer = consumer;
        updateTimer();
    }
    
    public void updateTimer() {
        this.timestamp = Calendar.getInstance().getTimeInMillis();
    }
    
    public boolean isTimeout() {
        return false;
    }
}
