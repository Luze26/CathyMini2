package com.cathymini.cathymini2.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author yuzel
 */
@Entity(name="Tampon")
@DiscriminatorValue("Tampon")
public class Tampon extends Product {
    @Column(name="appli")
    private Boolean appli;

    public Boolean getAppli() {
        return appli;
    }

    public void setAppli(Boolean appli) {
        this.appli = appli;
    }

    public Tampon() {
    }

    public Tampon(Boolean appli, String name, String type, Float price, Float flux, String description, String marque, String pict) {
        super(name, type, price, flux, description, marque, pict);
        this.appli = appli;
    }
    
    
}
