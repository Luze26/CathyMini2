package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.services.ConsumerBean;
import com.cathymini.cathymini2.webservices.model.ConsumerSession;
import com.cathymini.cathymini2.webservices.model.form.Connect;
import java.io.IOException;
import com.cathymini.cathymini2.webservices.model.form.Suscribe;
import com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring;
import com.cathymini.cathymini2.webservices.secure.Role;
import com.cathymini.cathymini2.webservices.secure.Secure;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 * 
 * @author Kraiss
 */
@Path("/consumer")
public class ConsumerFacade{
    private static final String USER_ATTR = "_USER_ATTR";
    private static final Logger logger = Logger.getLogger(com.cathymini.cathymini2.webservices.ProductFacade.class);
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();
    
    @EJB
    private ConsumerBean consumerBean;
    
    @POST
    @Path("/suscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.ANONYM)
    public String suscribe(Suscribe form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);

        try {
            logger.debug("Create user = " + form.username +" :: "+ form.pwd +" :: " + form.mail);
            Consumer user = consumerBean.suscribeUser(form.username, form.pwd, form.mail);
            session.setAttribute(USER_ATTR, sessionSecuring.openSession(user));
            return "You suscribe !";
        } catch (Exception ex) {
            try {
                response.sendError(400, ex.getMessage());
                return ex.getMessage();
            } catch (IOException ex1) {
                return ex1.getMessage();
            }
        }
    }
    
    @POST
    @Path("/connect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.ANONYM)
    public String connect(Connect form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);

        logger.debug("Connect user " + form.user);
        try {
            Consumer user = consumerBean.connectUser(form.user, form.pwd);
            session.setAttribute(USER_ATTR, sessionSecuring.openSession(user));
            return "You are connected!";
        } catch (Exception ex) {
            try {
                response.sendError(400, ex.getMessage());
                return ex.getMessage();
            } catch (IOException ex1) {
                return ex1.getMessage();
            }
        }
    }
    
    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public String logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        
        sessionSecuring.closeSession((ConsumerSession) session.getAttribute(USER_ATTR));
        consumerBean.logout();
        session.setAttribute(USER_ATTR, null);
        return "You logout.";
    }
    
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public String delete(Connect form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        
        logger.debug("Delete user " + form);
        try {
            consumerBean.deleteUser(form.user, form.pwd);
            
            Consumer user = sessionSecuring.getConsumer((ConsumerSession) session.getAttribute(USER_ATTR));
            if (user.getUsername().equals(form.user)) {
                sessionSecuring.closeSession((ConsumerSession) session.getAttribute(USER_ATTR));
                session.setAttribute(USER_ATTR, null);
                return "You delete your account.";
            } else {
                return "You delete the account of '"+form.user+"'.";
            }
        } catch (Exception ex) {
            try {
                response.sendError(400, ex.getMessage());
                return ex.getMessage();
            } catch (IOException ex1) {
                return ex1.getMessage();
            }
        }
    }
    
    @GET
    @Path("/seeCurrent")
    @Produces(MediaType.APPLICATION_JSON)
    public String seeCurrent(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        
        if (session.getAttribute(USER_ATTR) != null) {
            Consumer user = sessionSecuring.getConsumer((ConsumerSession) session.getAttribute(USER_ATTR));
            return user.getUsername();
        } else {
            return "";
        }
    }
}
