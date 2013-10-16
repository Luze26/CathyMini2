/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.services.ProductBean;
import java.util.Collection;
import javax.ejb.EJB;
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
    
    @EJB
    private ProductBean productBean;
    
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String create(AddProduct form) {
        logger.debug("Create product" + form);
        productBean.addProduct();
        return "product created";
    }
    
    @GET
    @Path("/all")
    @Produces("application/json")
    public Collection<Product> all() {
        return productBean.getProducts();
    }
}
