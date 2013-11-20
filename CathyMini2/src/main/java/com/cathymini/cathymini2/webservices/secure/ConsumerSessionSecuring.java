package com.cathymini.cathymini2.webservices.secure;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.webservices.model.ConsumerSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Kraiss
 */
public class ConsumerSessionSecuring {
    private static ConsumerSessionSecuring instance; // Singleton
    
    private Random random;
    private List<SecureEntry> secureTable;

    private ConsumerSessionSecuring() {
        secureTable = new ArrayList<SecureEntry>();
        random = new Random();
    }
    
    public static ConsumerSessionSecuring getInstance() {
        if (instance == null) {
            instance = new ConsumerSessionSecuring();
        }
        
        return instance;
    }
    
    public ConsumerSession openSession (Consumer consumer) {
        ConsumerSession session = new ConsumerSession(random.nextLong());
        secureTable.add(new SecureEntry(session, consumer));
        
        return session;
    }
    
    public Consumer getConsumer (ConsumerSession session) {
        for (SecureEntry entry: secureTable) {
            if (entry.session.equals(session)) {
                entry.updateTimer();
                return entry.consumer;
            }
        }
        
        return null;
    }
    
    public boolean closeSession (ConsumerSession session) {
        int index = -1;
        for (SecureEntry entry: secureTable) {
            if (entry.session.equals(session)) {
                index = secureTable.indexOf(entry);
            }
        }
        
        if (index != -1) {
            secureTable.remove(index);
            return true;
        }
        
        return false;
    }
}
