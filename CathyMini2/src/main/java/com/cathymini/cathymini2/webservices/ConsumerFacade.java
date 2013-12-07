package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.DeliveryAddress;
import com.cathymini.cathymini2.services.ConsumerBean;
import com.cathymini.cathymini2.webservices.model.ConsumerApi;
import com.cathymini.cathymini2.webservices.model.form.Address;
import com.cathymini.cathymini2.webservices.model.form.Connect;
import com.cathymini.cathymini2.webservices.model.form.Subscribe;
import com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring;
import com.cathymini.cathymini2.webservices.secure.Role;
import com.cathymini.cathymini2.webservices.secure.Secure;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
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

    /**
     * Rest service to subscribe a new consumer
     *
     * @return A String containing the service termination message
     */
    @POST
    @Path("/subscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.ANONYM)
    public ConsumerApi subscribe(Subscribe form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        try {
            if (!form.validate()) {
                throw new Exception("form error");
            }
            // Subscribe the new consumer
            Consumer user = consumerBean.subscribeUser(form.username, form.pwd, form.mail);
            
            // Automatically connect the consumer
            sessionSecuring.openSession(request, user);
            logger.debug("Create user = " + form.username + " :: " + form.pwd + " :: " + form.mail);
            return new ConsumerApi(user);
        } catch (Exception ex) {
            try {
                response.sendError(400, ex.getMessage());
            } catch (IOException ex1) {
                logger.debug("Failed to send error after user creation");
            }
            return null;
        }
    }

    /**
     * Rest service to connect a consumer
     *
     * @param form Connection form
     * @param request
     * @param response
     * @return A String containing the service termination message
     */
    @POST
    @Path("/connect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.ANONYM)
    public ConsumerApi connect(Connect form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        
        logger.debug("Connect user " + form.user);
        try {
            // Check if the (usr,pwd) is correct
            Consumer user = consumerBean.connectUser(form.user, form.pwd);
            
            // Connect the consumer
            sessionSecuring.openSession(request, user);
            
            return new ConsumerApi(user);
        } catch (Exception ex) {
            try {
                response.sendError(400, ex.getMessage());
            } catch (IOException ex1) {
                java.util.logging.Logger.getLogger(ConsumerFacade.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return null;
        }
    }

    /**
     * Rest service to logout a consumer
     *
     * @param request
     * @param response
     * @return A String containing the service termination message
     */
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

    /**
     * Rest service to delete a consumer
     *
     * @return A String containing the service termination message
     */
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public String delete(Connect form, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        
        logger.debug("Delete user " + form);
        try {
            // Delete a consumer account from the data base
            consumerBean.deleteUser(form.user, form.pwd);
            
            // If the consumer which delete the account is the owner, he is disconnected
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

    /**
     * Rest service to check if the client is connected
     *
     * @return The username if the consumer is connected, else an empty String
     */
    @GET
    @Path("/seeCurrent")
    @Produces(MediaType.APPLICATION_JSON)
    public ConsumerApi seeCurrent(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        
        Consumer user = sessionSecuring.getConsumer(request);
        
        if (user != null) {
            return new ConsumerApi(user);
        } else {
            return null;
        }
    }

    /**
     * Upade consumer
     *
     * @return The username if the consumer is connected, else an empty String
     */
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public ConsumerApi updateConsumer(ConsumerApi consumer, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        if (consumer.validate()) {
            Consumer user = sessionSecuring.getConsumer(request);
            try {
                consumerBean.updateUser(user, consumer);
                return new ConsumerApi(user);
            } catch (Exception ex) {
                throw new Exception("error");
            }
        } else {
            ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity("Missing idLanguage parameter on request");
            Response res = builder.build();
            throw new WebApplicationException(res);
        }
    }

    /**
     * Add address
     */
    @POST
    @Path("/addAddress")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public void addAddress(Address address, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        if (address.validate()) {
            Consumer user = sessionSecuring.getConsumer(request);
            consumerBean.addAddress(user, address);
        } else {
            ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity("address error");
            Response res = builder.build();
            throw new WebApplicationException(res);
        }
    }

    /**
     * Get address
     */
    @GET
    @Path("/address")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(Role.MEMBER)
    public Collection<Address> address(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        Consumer user = sessionSecuring.getConsumer(request);
        Collection<DeliveryAddress> deliveryAddress = user.getDeliveryCollection();
        Collection<Address> address = new ArrayList<Address>();
        for (DeliveryAddress addr : deliveryAddress) {
            address.add(new Address(addr));
        }
        return address;
    }
}
