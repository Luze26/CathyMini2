/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.cathymini.cathymini2.services.ConsumerSession;
import com.cathymini.cathymini2.webservices.model.form.Suscribe;
import com.cathymini.cathymini2.webservices.model.form.Connect;
import javax.ws.rs.GET;

/**
 * 
 * @author Kraiss
 */
@Stateless
@Path("/consumer")
public class ConsumerFacade {
    private static final Logger logger = Logger.getLogger(com.cathymini.cathymini2.webservices.ProductFacade.class);
    
    @EJB
    private ConsumerSession consumerSession;
    
    @POST
    @Path("/suscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String suscribe(Suscribe form) {
        logger.debug("Create user = " + form.username +" :: "+ form.pwd +" :: " + form.mail);
        consumerSession.suscribeUser(form.username, form.pwd, form.mail);
        return "User suscribed";
    }
    
    @POST
    @Path("/connect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String connect(Connect form) {
        logger.debug("Connect user " + form);
        consumerSession.connectUser(form.user, form.pwd);
        return "User connected";
    }
    
    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String logout() {
        consumerSession.logout();
        return "User disconnected";
    }
    
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String delete(Connect form) {
        logger.debug("Delete user " + form);
        consumerSession.deleteUser(form.user, form.pwd);
        return "User deleted";
    }
    
    @GET
    @Path("/seeCurrent")
    @Produces("application/json")
    public String seeCurrent() {
        String s = consumerSession.toString();
        logger.debug("See current session = "+s);
        return s;
    }
}
