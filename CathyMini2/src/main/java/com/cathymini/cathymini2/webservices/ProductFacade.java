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
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

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
    public Product create(AddProduct form, @Context final HttpServletResponse response) {
        if(form.validate()) {
            Product product = productBean.addProduct(form.name, form.price);
            return product;
        }
        else {
            response.setStatus(400);
            return null;
        }
    }
    
    @GET
    @Path("/all")
    @Produces("application/json")
    public Collection<Product> all(@QueryParam("offset") int offset,
            @QueryParam("length") int length, @QueryParam("orderBy") String orderBy) {
        if(length == 0) {
            length = 10;
        }
        return productBean.getProducts(offset, length, orderBy);
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
            productBean.addProduct(product + i, new Float(2));
        }
        return "populated";
    }
}
