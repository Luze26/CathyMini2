/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Consumer;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.cathymini.cathymini2.services.ConsumerBean;
import com.cathymini.cathymini2.webservices.model.ConsumerSession;
import com.cathymini.cathymini2.webservices.model.form.Suscribe;
import com.cathymini.cathymini2.webservices.model.form.Connect;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;

/**
 * 
 * @author Kraiss
 */
@Path("/consumer")
public class ConsumerFacade{
    private static final String USER_ATTR = "_USER_ATTR";
    private static final Logger logger = Logger.getLogger(com.cathymini.cathymini2.webservices.ProductFacade.class);
    
    @EJB
    private ConsumerBean consumerBean;
    
    @POST
    @Path("/suscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String suscribe(Suscribe form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);

        if (session.getAttribute(USER_ATTR) == null) {
            try {
                logger.debug("Create user = " + form.username +" :: "+ form.pwd +" :: " + form.mail);
                Consumer user = consumerBean.suscribeUser(form.username, form.pwd, form.mail);
                session.setAttribute(USER_ATTR, ConsumerSession.getSession(user));
                return "You are suscribed!";
            } catch (Exception ex) {
                try {
                    response.sendError(400, ex.getMessage());
                    return ex.getMessage();
                } catch (IOException ex1) {
                }
            }
        }
        return "You are already connected";
    }
    
    @POST
    @Path("/connect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String connect(Connect form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);

        if (session.getAttribute(USER_ATTR) == null) {
            logger.debug("Connect user " + form.user);
            try {
                Consumer user = consumerBean.connectUser(form.user, form.pwd);
                session.setAttribute(USER_ATTR, ConsumerSession.getSession(user));
                return "You are connected!";
            } catch (Exception ex) {
                try {
                    response.sendError(400, ex.getMessage());
                    return ex.getMessage();
                } catch (IOException ex1) {
                }
            }
        }
        return "You are already connected";
    }
    
    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        
        if (session.getAttribute(USER_ATTR) != null) {
            consumerBean.logout();
            session.setAttribute(USER_ATTR, null);
            return "You logout.";
        } else {
            return "You are not connected.";
        }
    }
    
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(Connect form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        
        logger.debug("Delete user " + form);
        try {
            consumerBean.deleteUser(form.user, form.pwd);
            
            if (session.getAttribute(USER_ATTR) != null) {
                ConsumerSession cs = (ConsumerSession) session.getAttribute(USER_ATTR);
                if (cs.username.equals(form.user)) {
                    session.setAttribute(USER_ATTR, null);
                }
            }
            
            return "You delete your account.";
        } catch (Exception ex) {
            try {
                response.sendError(400, ex.getMessage());
                return ex.getMessage();
            } catch (IOException ex1) {
            }
        }
        
        return "";
    }
    
    @GET
    @Path("/seeCurrent")
    @Produces(MediaType.APPLICATION_JSON)
    public String seeCurrent(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        
        if (session.getAttribute(USER_ATTR) != null) {
            ConsumerSession cs = (ConsumerSession) session.getAttribute(USER_ATTR);
            return cs.username;
        } else {
            return "";
        }
    }
}
