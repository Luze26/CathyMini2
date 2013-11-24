package com.cathymini.cathymini2.webservices.secure;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.services.ConsumerBean;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Kraiss
 */
public class ConsumerSessionSecuring {    
    private static final String USER_ATTR = "_USER_ATTR";
    private static final Logger logger = Logger.getLogger(com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring.class);
    private static ConsumerSessionSecuring instance; // Singleton
    
    private SecureRandom random;
    private Integer sessionTimeOut;
    private HashMap<String, SecureEntry> secureMap;

    private ConsumerSessionSecuring() {
        secureMap = new HashMap<String, SecureEntry>();
        random = new SecureRandom();
        sessionTimeOut = 30 * 60 * 1000; // 30 min
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
     
        secureMap.put(secureStr , new SecureEntry(consumer));
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
        
        if (entry.timestamp < Calendar.getInstance().getTimeInMillis() + sessionTimeOut) {
            entry.updateTimer();
            return true;
        } else {
            return false;
        }
        
    }
    
    public boolean isConnectedAsAdmin (HttpServletRequest request) {
        return isConnected(request) && getConsumer(request).getRole().equals(Role.ADMIN);
    }
    
    public Consumer getConsumer (HttpServletRequest request) {
        
        if (isConnected(request)) {
            SecureEntry entry = secureMap.get(getConsumerSession(request));
            return entry.consumer;
            
        } else {
            return null;
        }
    }
    
    public void closeSession (HttpServletRequest request) {
        isConnected(request);
        String consumerSession = getConsumerSession(request);
        
        request.getSession().invalidate();
        secureMap.remove(consumerSession);
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
