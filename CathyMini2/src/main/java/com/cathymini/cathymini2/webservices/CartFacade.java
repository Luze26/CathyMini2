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
    private static final String CART_ATTR = "_CART_ATTR";
    private static final Logger logger = Logger.getLogger(CartFacade.class);
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();

    @EJB
    private CartSession cartBean;
    @EJB
    private ProductBean productBean;

    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String add(Long id, @Context HttpServletRequest request, @Context HttpServletResponse response){
        System.out.println("dans add :!! id :"+id);
        Consumer cons = null;
        try{
            cons = sessionSecuring.getConsumer(request);
            System.out.println("nom cons : "+cons.getUsername());
        }catch(Exception ex){
            cons = null;
        }
        try{
            System.out.println(cons != null);
            if (cons != null) {
                System.out.println("J'ai un consumer");
                Cart cart  = cartBean.getUserCart(cons);
                if(cart == null){
                    System.out.println("Creation d'un caddie");
                    cart = cartBean.newCart(cons);
                    System.out.println("le caddie a etecreer");
                }
                System.out.println("Ajout du produit");
                Product prod = productBean.getProduct(id);
                cartBean.addProduct(prod, cart);
                return "The product has been added";
            }
            else{
                System.out.println("Pas de consumer");
                Product prod = productBean.getProduct(id);
                Cart newCartTemp;
               try{
                    newCartTemp = cartBean.findCartByID(getCartID(request));
               }
               catch(Exception ex){
                    newCartTemp = cartBean.newCart(null);
                    setCartID(request, newCartTemp.getCartID());
               }
                cartBean.addProduct(prod, newCartTemp);
                return "The product have been added to temp cart";
            }
        } catch (Exception ex) {
            System.out.println("DANS EX "+ex.getMessage());
            response.setStatus(400);
            return ex.getMessage();
            
        }
    }
    
    private Long getCartID(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return (Long) session.getAttribute(CART_ATTR);
    }
    
    private void setCartID(HttpServletRequest request, Long cartID) {
        HttpSession session = request.getSession(true);
        session.setAttribute(CART_ATTR, cartID);
    }
    
    @POST
    @Path("/consumerIsConnected")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String consumerIsConnected(){
        
        System.out.println("Lien marche");
        return "";
    }

    
}
