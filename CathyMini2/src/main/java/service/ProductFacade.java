/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.cathymini.cathymini2.services.ProductBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import service.model.form.AddProduct;

/**
 *
 * @author zang
 */
@Stateless
@Path("/product")
public class ProductFacade {
    
    private static final Logger logger = Logger.getLogger(ProductFacade.class);
    
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String create(AddProduct form) {
        logger.debug("ici" + form.name + " ppp " + form.price);
        return "HELLLOOO";
    }
}
