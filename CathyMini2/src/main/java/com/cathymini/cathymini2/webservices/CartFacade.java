package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.services.ProductBean;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author yuzel
 */
@Path("/cart")
public class CartFacade {

    private static final Logger logger = Logger.getLogger(CartFacade.class);

    @EJB
    private ProductBean productBean;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Product add(Long id) {

        return null;
    }
}
