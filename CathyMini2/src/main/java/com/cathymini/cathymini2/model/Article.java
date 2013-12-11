package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


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

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getDetail() {
        return detail;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }
    
    @Override
    public String toString(){
        return "Article "+ titre;
    }
    
}
