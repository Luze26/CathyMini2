package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Product;
import javax.ejb.Stateless;
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
@Stateless
@Path("/cart")
public class CartFacade {

    private static final Logger logger = Logger.getLogger(CartFacade.class);

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Product add(Long id) {

        return null;
    }
}
