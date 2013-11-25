/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Napkin;
import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.model.Tampon;
import com.cathymini.cathymini2.services.ProductBean;
import com.cathymini.cathymini2.webservices.model.ProductSearch;
import com.cathymini.cathymini2.webservices.model.form.AddProduct;
import com.cathymini.cathymini2.webservices.model.form.EditProduct;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

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
            Product product = productBean.addProduct(form.name, form.marque, form.flux, form.price, form.type, form.description);
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
    
    @POST
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Product edit(EditProduct form, @Context final HttpServletResponse response) {
        return productBean.editProduct(form.id, form.name, form.price);
    }
    
    
    @GET
    @Path("/readXML")
    @Produces(MediaType.APPLICATION_JSON)
    public void readXML(){
            SAXBuilder sxb = new SAXBuilder();
            Document document = null;
      try
      {
         //On crée un nouveau document JDOM avec en argument le fichier XML
         //Le parsing est terminé ;)
         document = sxb.build(new URL("http://localhost:8080/assets/product/listeProduit.xml"));
      }
      catch(Exception e){
          System.out.println(e);
      }
      
      //On initialise un nouvel élément racine avec l'élément racine du document.
      racine = document.getRootElement();
      afficheAll();
      
    }
     Element racine;
    
    
    @GET
    @Path("/afficheAll")
    @Produces(MediaType.APPLICATION_JSON)
    public void afficheAll(){
        
        List listEtudiants = racine.getChildren("product");
        //On crée un Iterator sur notre liste
        Iterator i = listEtudiants.iterator();
        while(i.hasNext())
        {
           //On recrée l'Element courant à chaque tour de boucle afin de
           //pouvoir utiliser les méthodes propres aux Element comme :
           //sélectionner un nœud fils, modifier du texte, etc...
           Element courant = (Element)i.next();
           //récupérer information sur les produits
           String nom = courant.getChild("nom").getText();
           String marque = courant.getChild("marque").getText();
           String description = courant.getChild("description").getText().replace("'", "\'");
           if(courant.getAttributeValue("typeP").equals("tampon")){
               Boolean appli = courant.getChild("applicateur").getText().equals("true");
               
               List listFlux = courant.getChild("listeFlux").getChildren("flux");
                //On crée un Iterator sur notre liste
                Iterator j = listFlux.iterator();
                while(j.hasNext())
                {
                   //On recrée l'Element courant à chaque tour de boucle afin de
                   //pouvoir utiliser les méthodes propres aux Element comme :
                   //sélectionner un nœud fils, modifier du texte, etc...
                   Element c = (Element)j.next();
                   Float fluxC = Float.parseFloat(c.getChild("name").getText());
                   Float prix = Float.parseFloat(c.getChildText("prix"));
                   String pict = c.getChildText("img");
                   Product newT = new Tampon(appli, nom, "tampon", prix, fluxC, description, marque, pict);
                   productBean.addProduct(newT);
                }
           }
           else
           {
               String typeS = courant.getChild("type").getText();
               List listFlux = courant.getChild("listeFlux").getChildren("flux");
                //On crée un Iterator sur notre liste
                Iterator j = listFlux.iterator();
                while(j.hasNext())
                {
                   //On recrée l'Element courant à chaque tour de boucle afin de
                   //pouvoir utiliser les méthodes propres aux Element comme :
                   //sélectionner un nœud fils, modifier du texte, etc...
                   Element c = (Element)j.next();
                   Float fluxC = Float.parseFloat(c.getChild("name").getText());
                   Float prix = Float.parseFloat(c.getChildText("prix"));
                   String pict = c.getChildText("img");
                   Product newT = new Napkin(typeS, nom, "serviette", prix, fluxC, description, marque, pict);
                   productBean.addProduct(newT);
                }
             }
        }
    }
    
    @GET
    @Path("/populate")
    @Produces(MediaType.APPLICATION_JSON)
    public String populate(@QueryParam("size") int size) {
       /* final String lexicon = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz12345674890abcdefghijklmnopqrstuvwxyz";

        if(size == 0) {
            size = 500;
        }
        
        final java.util.Random rand = new java.util.Random();
        for(int j = 0; j < size; j++) {
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < 6; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            String typeProduit = "Serviette";
            if (new Integer(rand.nextInt(2)) == 1) {
               typeProduit  = "Tampon"; 
            }
            
            productBean.addProduct(builder.toString(), new Float(rand.nextInt(100)), typeProduit);
        }
        
        return "populated";*/
        readXML();
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
