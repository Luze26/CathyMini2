/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cathymini.cathymini2.services;

import com.cathymini.cathymini2.model.Article;
import com.cathymini.cathymini2.model.Product;
import com.cathymini.cathymini2.webservices.model.ArticleSearch;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author Flo
 */
@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ArticleBean {

    @PersistenceContext(unitName = "com.cathymini_CathyMini2_PU")
    private EntityManager manager;

    private static final Logger logger = Logger.getLogger(ArticleBean.class);
    
    public boolean delete(int id) {
        Query query = manager.createNamedQuery("deleteById", Product.class); 
        query.setParameter("id", id);
        return query.executeUpdate() == 1;
    }

    public Collection<Article> getArticle(ArticleSearch searchQuery) {
        Query query = constructQuery(searchQuery);
        return (Collection<Article>) query.getResultList();
    }

    public void addArticle(Article a) {
        manager.persist(a);
        logger.info("New article : " + a);
    }
    
    private Query constructQuery(ArticleSearch searchQuery) {
        String query = "SELECT a FROM Article a ORDER BY a.id";
        return manager.createQuery(query).setFirstResult(searchQuery.offset).setMaxResults(searchQuery.length);
    }

    public Article addArticle(String titre, String descritpion, String type, String detail, String image) {
        Article article = new Article(titre, descritpion, detail, type, image);
        manager.persist(article);
        logger.info("New article : " + article);
        return article;
    }
}
