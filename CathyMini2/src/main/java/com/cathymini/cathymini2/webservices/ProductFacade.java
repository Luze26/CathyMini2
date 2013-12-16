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
        if (form != null && form.validate()) {
            Product product = productBean.addProduct(form.name, form.marque, form.flux, form.price, form.type, form.description);
            return product;
        } else {
            response.setStatus(400);
            return null;
        }
    }

    @POST
    @Path("/uploadFile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadFile(String file, @Context final HttpServletResponse response) {
        /*if(file != null && file.validate()) {
         System.out.println(file.file);
         return file;
         }
         else {
         response.setStatus(400);
         return null;
         }*/
        System.out.println("Fichier : " + file);
        return "Upload pas réalisé!";
    }

    @POST
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Collection<Product> all(ProductSearch query, @Context final HttpServletResponse response) {
        if (query == null) {
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
        return productBean.editProduct(form.id, form.name, form.marque, form.flux, form.price, form.description);
    }

    @GET
    @Path("/populate")
    @Produces(MediaType.APPLICATION_JSON)
    public void populate(){
        Element racine;
        SAXBuilder sxb = new SAXBuilder();
        Document document = null;
        try {
           //On crée un nouveau document JDOM avec en argument le fichier XML
           //Le parsing est terminé ;)
           document = sxb.build(new URL("http://localhost:8080/assets/data/listeProduit.xml"));
        } catch(Exception e){
            System.out.println(e);
        }
      
        /*
         * Lecture du fichier XML
         */
        
        //On initialise un nouvel élément racine avec l'élément racine du document.
        racine = document.getRootElement();
        
        List productList = racine.getChildren("product");
        //On crée un Iterator sur notre liste
        Iterator i = productList.iterator();
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
                while (j.hasNext()) {
                    //On recrée l'Element courant à chaque tour de boucle afin de
                    //pouvoir utiliser les méthodes propres aux Element comme :
                    //sélectionner un nœud fils, modifier du texte, etc...
                    Element c = (Element) j.next();
                    Float fluxC = Float.parseFloat(c.getChild("name").getText());
                    Float prix = Float.parseFloat(c.getChildText("prix"));
                    String pict = c.getChildText("img");
                    Product newT = new Tampon(appli, nom, "tampon", prix, fluxC, description, marque, pict);
                    productBean.addProduct(newT);
                }
            } else {
                String typeS = courant.getChild("type").getText();
                List listFlux = courant.getChild("listeFlux").getChildren("flux");
                //On crée un Iterator sur notre liste
                Iterator j = listFlux.iterator();
                while (j.hasNext()) {
                    //On recrée l'Element courant à chaque tour de boucle afin de
                    //pouvoir utiliser les méthodes propres aux Element comme :
                    //sélectionner un nœud fils, modifier du texte, etc...
                    Element c = (Element) j.next();
                    Float fluxC = Float.parseFloat(c.getChild("name").getText());
                    Float prix = Float.parseFloat(c.getChildText("prix"));
                    String pict = c.getChildText("img");
                    Product newT = new Napkin(typeS, nom, "serviette", prix, fluxC, description, marque, pict);
                    productBean.addProduct(newT);
                }
             }
        }
        
        
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
        if (id >= 0) {
            boolean deleted = productBean.delete(id);
            if (deleted) {
                return "";
            }
        }
        response.setStatus(400);
        return "unknow product";
    }
}
