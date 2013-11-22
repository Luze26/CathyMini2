package com.cathymini.cathymini2.webservices.secure;

import com.cathymini.cathymini2.model.Consumer;
import java.security.SecureRandom;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Kraiss
 */
public class ConsumerSessionSecuring {
    private static final String USER_ATTR = "_USER_ATTR";
    private static final Integer sessionTimeOut = 300; // In minutes
    private static ConsumerSessionSecuring instance; // Singleton
    
    private SecureRandom random;
    private HashMap<String, SecureEntry> secureMap;

    private ConsumerSessionSecuring() {
        secureMap = new HashMap<String, SecureEntry>();
        random = new SecureRandom();
    }
    
    public static ConsumerSessionSecuring getInstance() {
        if (instance == null) {
            instance = new ConsumerSessionSecuring();
        }
        
        return instance;
    }
    
    public void openSession (HttpServletRequest request, Consumer consumer) {
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        
        String secureStr = "";
        
        for (int i = 0; i < bytes.length; i++) {
            secureStr += (char) bytes[i];
        }
     
        secureMap.put(secureStr , new SecureEntry(consumer.getUserID()));
        setConsumerSession(request, secureStr);
    }
    
    public boolean isConnected (HttpServletRequest request) {
        
        String consumerSession = getConsumerSession(request);
        if (consumerSession == null) {
            return false;
        }
            
        SecureEntry entry = secureMap.get(consumerSession);
        if (entry == null) {
            return false;
        }
        
        // TODO : test timeout !!!
        if (entry != null) {
            entry.updateTimer();
        }
        
        return true;
    }
    
    public Consumer getConsumer (HttpServletRequest request) {
        if (isConnected(request)) {
        SecureEntry entry = secureMap.get(getConsumerSession(request));
            
        }
        
        return null;
    }
    
    public void closeSession (HttpServletRequest request) {
        isConnected(request);
        
        SecureEntry entry = secureMap.remove(getConsumerSession(request));
        request.getSession().invalidate();
    }
    
    private String getConsumerSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return (String) session.getAttribute(USER_ATTR);
    }
    
    private void setConsumerSession(HttpServletRequest request, String consumerSession) {
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ATTR, consumerSession);
    }
}
