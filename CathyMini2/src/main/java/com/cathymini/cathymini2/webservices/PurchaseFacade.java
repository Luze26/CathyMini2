package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.services.ProductBean;
import com.cathymini.cathymini2.webservices.model.form.AddProduct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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
@Stateless
@Path("/purchase")
public class PurchaseFacade {
    
    private static final Logger logger = Logger.getLogger(PurchaseFacade.class);
    
    @EJB
    private ProductBean productBean;
    
    @POST
    @Path("/createPurchase")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createPurchase(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return "Not Implemented";
    }
    
    @POST
    @Path("/createSubscription")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createSubscription(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return "Not Implemented";
    }
    
    @POST
    @Path("/editPurchase")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editPurchase(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return "Not Implemented";
    }
    
    @POST
    @Path("/stopPurchase")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String stopPurchase(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        return "Not Implemented";
    }
}
