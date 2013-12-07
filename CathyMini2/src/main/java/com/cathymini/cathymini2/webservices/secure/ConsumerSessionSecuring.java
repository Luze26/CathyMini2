package com.cathymini.cathymini2.webservices.secure;

import com.cathymini.cathymini2.model.Consumer;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * The class {@link ConsumerSessionSecuring} is used to the consumer HTTP session.
 * This class implements a {@link HashMap} which associate a random {@link String} to a {@link Consumer} and a timestamp
 * @author Kraiss
 */
public class ConsumerSessionSecuring {    
    private static final String USER_ATTR = "_USER_ATTR";
    private static final Logger logger = Logger.getLogger(com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring.class);
    private static ConsumerSessionSecuring instance; // Singleton
    
    private SecureRandom random;
    private Integer sessionTimeOut;
    private Map<String, SecureEntry> secureMap;

    private ConsumerSessionSecuring() {
        // Init a clear Map
        secureMap = new HashMap<String, SecureEntry>();
        random = new SecureRandom();
        
        // Init session timeout value
        sessionTimeOut = 30 * 60 * 1000; // 30 min
        
        // Run a thread to clear outdated entry of the Map
        new Thread(new SessionSecuringMapCleaner()).start();
    }
    
    /**
     * Return a unique instance of {@link ConsumerSessionSecuring}
     * @return Unique instance of {@link ConsumerSessionSecuring}
     */
    public static ConsumerSessionSecuring getInstance() {
        if (instance == null) {
            instance = new ConsumerSessionSecuring();
        }
        
        return instance;
    }
    
    /**
     * Inner class implementing a {@link Thread} to clean the {@link HashMap} of out-dated entries.
     */
    public class SessionSecuringMapCleaner implements Runnable {
        @Override
        public void run() {
            // Run the worker every 'sessionTimeOut'
            try {
                Thread.sleep(sessionTimeOut);
            } catch (InterruptedException ex) {
            }
            //execute();
        }
        
        /**
         * Search and clear outdated entries in the Map
         */
        public void execute(){
            Long deadLine = Calendar.getInstance().getTimeInMillis() - sessionTimeOut;
            
            for (Map.Entry<String,SecureEntry> entry : secureMap.entrySet()) {
                if (entry.getValue().timestamp > deadLine){
                    secureMap.remove(entry.getKey());
                }
            }
        }
    }
    
    /**
     * Create a new entry in the Map to manage a new connexion
     * @param request HTTP request
     * @param consumer Consumer to connect
     */
    public void openSession (HttpServletRequest request, Consumer consumer) {
        // Get a random byte array
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        
        // Turn it to a String
        String secureStr = "";
        for (int i = 0; i < bytes.length; i++) {
            secureStr += (char) bytes[i];
        }
     
        // Associate the String to a consumer connexion
        secureMap.put(secureStr , new SecureEntry(consumer));
        setConsumerSession(request, secureStr);
    }
    
    /**
     * Return <code>true</code> if the requester is connected, else <code>false</code>
     * @param request HTTP request
     * @return <code>true</code> if the requester is connected, else <code>false</code>
     */
    public boolean isConnected (HttpServletRequest request) {
        // Check if the HTTP session exists
        String consumerSession = getConsumerSession(request);
        if (consumerSession == null) {
            return false;
        }
            
        // Check if the HTTP session refers to a valid entry
        SecureEntry entry = secureMap.get(consumerSession);
        if (entry == null) {
            return false;
        }
        
        // Update the timer if the entry is not outdated
        if (entry.timestamp < Calendar.getInstance().getTimeInMillis() + sessionTimeOut) {
            entry.updateTimer();
            return true;
        } else {
            return false;
        }
        
    }
    
    
    /**
     * Return <code>true</code> if the requester is connected with an admin account, else <code>false</code>
     * @param request HTTP request
     * @return <code>true</code> if the requester is connected with an admin account, else <code>false</code>
     */
    public boolean isConnectedAsAdmin (HttpServletRequest request) {
        return isConnected(request) && getConsumer(request).getRole().equals(Role.ADMIN);
    }
    
    /**
     * If the requester is connected, return his {@link Consumer} account
     * @param request HTTP request
     * @return If the requester is connected, return his {@link Consumer} account
     */
    public Consumer getConsumer (HttpServletRequest request) {
        if (isConnected(request)) {
            SecureEntry entry = secureMap.get(getConsumerSession(request));
            return entry.consumer;
        } else {
            return null;
        }
    }
    
    /**
     * Close a consumer connexion
     * @param request HTTP request
     */
    public void closeSession (HttpServletRequest request) {
        isConnected(request);
        String consumerSession = getConsumerSession(request);
        
        request.getSession().invalidate(); // Clear all session data
        secureMap.remove(consumerSession);
    }
    
    /**
     * Return the value of the consumer session attribute of the requester
     * @param request HTTP request
     * @return The value of the consumer session attribute of the requester
     */
    private String getConsumerSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return (String) session.getAttribute(USER_ATTR);
    }
    
    /**
     * Set the value of the consumer session attribute of the requester
     * @param request HTTP request
     * @param consumerSession String to refer to the consumer connexion
     */
    private void setConsumerSession(HttpServletRequest request, String consumerSession) {
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ATTR, consumerSession);
    }
    
}
