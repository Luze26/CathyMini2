package com.cathymini.cathymini2.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;


/**
 *
 * @author flo
 */
@Entity(name="Article")
@DiscriminatorValue("Article")
public class Article implements Serializable {
    
    @Id
    @GeneratedValue
    @Column(name="ID")
    private Long id;
    
    @Column(name="titre")
    private String titre;
    
    @Column(name="description")
    private String description;
    
    @Column(name="detail")
    private String detail;
    
    @Column(name="type")
    private String type;
    
    @Column(name="datepost")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datepost;
    
    @Column(name="image")
    private String image;

    public Article() {
    }

    public Article(String titre, String description, String detail, String type, String image) {
        this.titre = titre;
        this.description = description;
        this.detail = detail;
        this.type = type;
        this.image = image;
    }

    public Article(String titre, String description, String detail, String type) {
        this.titre = titre;
        this.description = description;
        this.detail = detail;
        this.type = type;
        this.image = null;
    }
    
    public String toString(){
        return "Article "+ titre;
    }
    
}
