/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.services.ProductBean;
import com.cathymini.cathymini2.webservices.model.ProductSearch;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Product create(AddProduct form, @Context final HttpServletResponse response) {
        if(form != null && form.validate()) {
            Product product = productBean.addProduct(form.name, form.price);
            return product;
        }
        else {
            response.setStatus(400);
            return null;
        }
    }
    
    @POST
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Collection<Product> all(ProductSearch query, @Context final HttpServletResponse response) {
        if(query == null) {
            response.setStatus(400);
            return null;    
        }
        return productBean.getProducts(query);
    }
    
    @GET
    @Path("/populate")
    @Produces(MediaType.APPLICATION_JSON)
    public String populate(@QueryParam("size") int size) {
        final String lexicon = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz12345674890abcdefghijklmnopqrstuvwxyz";

        if(size == 0) {
            size = 500;
        }
        
        final java.util.Random rand = new java.util.Random();
        for(int j = 0; j < size; j++) {
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < 6; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            productBean.addProduct(builder.toString(), new Float(rand.nextInt(100)));
        }
        
        return "populated";
    }
    
    /**
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@QueryParam("id") @DefaultValue("-1") int id, @Context final HttpServletResponse response) {
        if(id >= 0) {
            boolean deleted = productBean.delete(id);
            if(deleted) {
                return "";
            }            
        }
        response.setStatus(400);
        return "unknow product";
    }
}
