/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices;

import com.cathymini.cathymini2.model.Article;
import com.cathymini.cathymini2.services.ArticleBean;
import com.cathymini.cathymini2.webservices.model.ArticleSearch;
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
 * @author Flo
 */
@Stateless
@Path("/article")
public class ArticleFacade {
    
    private static final Logger logger = Logger.getLogger(ArticleFacade.class);
    
    
    @EJB
    private ArticleBean articleBean;
  /*  @POST
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
    }*/
    
    @POST
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public Collection<Article> all(ArticleSearch query, @Context final HttpServletResponse response) {
        if(query == null) {
            response.setStatus(400);
            return null;    
        }
        return articleBean.getArticle(query);
    }
    
 /*   @POST
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Product edit(EditProduct form, @Context final HttpServletResponse response) {
        return productBean.editProduct(form.id, form.name, form.marque, form.flux, form.price, form.description);
    }
   */ 
    
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
         document = sxb.build(new URL("http://localhost:8080/assets/article/listeArticle.xml"));
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
        
        List listEtudiants = racine.getChildren("article");
        //On crée un Iterator sur notre liste
        Iterator i = listEtudiants.iterator();
        while(i.hasNext())
        {
           //On recrée l'Element courant à chaque tour de boucle afin de
           //pouvoir utiliser les méthodes propres aux Element comme :
           //sélectionner un nœud fils, modifier du texte, etc...
           Element courant = (Element)i.next();
           //récupérer information sur les produits
           String titre = courant.getChild("titre").getText();
           String type = courant.getChild("type").getText();
           String description = courant.getChild("description").getText();
           String detail = courant.getChild("detail").getText();
           String image = courant.getChild("image").getText();
           Article a = new Article(titre, description, detail, type, image);
           articleBean.addArticle(a);
        }
    }
    
    @GET
    @Path("/populate")
    @Produces(MediaType.APPLICATION_JSON)
    public String populate(@QueryParam("size") int size) {
        readXML();
        return "article populated";
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
            boolean deleted = articleBean.delete(id);
            if(deleted) {
                return "";
            }            
        }
        response.setStatus(400);
        return "unknow product";
    }
}