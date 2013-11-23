package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Cart;
import com.cathymini.cathymini2.model.Consumer;
import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.services.CartSession;
import com.cathymini.cathymini2.services.ConsumerBean;
import com.cathymini.cathymini2.services.ProductBean;
import com.cathymini.cathymini2.webservices.secure.ConsumerSessionSecuring;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author yuzel
 */
@Path("/cart")
public class CartFacade {

    private static final String USER_ATTR = "_USER_ATTR";
    private static final Logger logger = Logger.getLogger(CartFacade.class);
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();

    @EJB
    private CartSession cartBean;
    private ProductBean productBean;
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String add(Long id, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        System.out.println("dans add :!!");
        try{
            Consumer cons = sessionSecuring.getConsumer(request);
            if (cons != null) {
                Cart cart  = cartBean.getUserCart(cons);
                if(cart == null){
                    cart = cartBean.newCart(cons);      
                }
                Product prod = productBean.getProduct(id);
                cartBean.addProduct(prod, cart);
                return "The product has been added";
            }
            
            return null;
        } catch (Exception ex) {
            response.setStatus(400);
            return ex.getMessage();
        }
    }
}
