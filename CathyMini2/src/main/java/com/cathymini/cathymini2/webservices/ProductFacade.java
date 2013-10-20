/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices;

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
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.cathymini.cathymini2.webservices.model.form.AddProduct;
import javax.ws.rs.QueryParam;

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
        productBean.addProduct(form.name);
        return "product created";
    }
    
    @GET
    @Path("/all")
    @Produces("application/json")
    public Collection<Product> all(@QueryParam("offset") int offset,
            @QueryParam("length") int length) {
        if(length == 0) {
            length = 10;
        }
        return productBean.getProducts(offset, length);
    }
    
    @GET
    @Path("/populate")
    @Produces("application/json")
    public String populate(@QueryParam("size") int size) {
        if(size == 0) {
            size = 500;
        }
        String product = "product";
        for(int i = 0; i < size; i++) {
            productBean.addProduct(product + i);
        }
        return "populated";
    }
}
