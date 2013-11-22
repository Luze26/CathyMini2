package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.services.ConsumerBean;
import com.cathymini.cathymini2.webservices.model.form.Connect;
import com.cathymini.cathymini2.webservices.model.form.Suscribe;
import com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring;
import com.cathymini.cathymini2.webservices.secure.Role;
import com.cathymini.cathymini2.webservices.secure.Secure;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        
        try {
            Consumer user = consumerBean.suscribeUser(form.username, form.pwd, form.mail);
            sessionSecuring.openSession(request, user);
            logger.debug("Create user = " + form.username +" :: "+ form.pwd +" :: " + form.mail);
            return "You suscribe !";
        } catch (Exception ex) {
            response.setStatus(400);
            return ex.getMessage();
        }
    }
    
    @POST
    @Path("/connect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.ANONYM)
    public String connect(Connect form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        
        logger.debug("Connect user " + form.user);
        try {
            Consumer user = consumerBean.connectUser(form.user, form.pwd);
            sessionSecuring.openSession(request, user);
            return "You are connected!";
        } catch (Exception ex) {
            response.setStatus(400);
            return ex.getMessage();
        }
    }
    
    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public String logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        sessionSecuring.closeSession(request);
        consumerBean.logout();
        return "You logout.";
    }
    
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public String delete(Connect form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        
        logger.debug("Delete user " + form);
        try {
            consumerBean.deleteUser(form.user, form.pwd);
            Consumer user = sessionSecuring.getConsumer(request);
            
            if (user.getUsername().equals(form.user)) {
                sessionSecuring.closeSession(request);
                return "You delete your account.";
            } else {
                return "You delete the account of '"+form.user+"'.";
            }
        } catch (Exception ex) {
            response.setStatus(400);
            return ex.getMessage();
        }
    }
    
    @GET
    @Path("/seeCurrent")
    @Produces(MediaType.APPLICATION_JSON)
    public String seeCurrent(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        Consumer user = sessionSecuring.getConsumer(request);
        
        if (user != null) {
            return user.getUsername();
        } else {
            return "";
        }
    }
}
