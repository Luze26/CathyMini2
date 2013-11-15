package com.cathymini.cathymini2.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 *
 * @author yuzel
 */
@Entity(name="Napkin")
@DiscriminatorValue("Napkin")
public class Napkin extends Product {
    
     @Column(name="typeServiette")
    private String typeServiette;

    public String getAppli() {
        return typeServiette;
    }

    public void setAppli(String typeS) {
        this.typeServiette = typeS;
    }

    public Napkin() {
    }

    public Napkin(String typeS, String name, String type, Float price, Float flux, String description, String marque) {
        super(name, type, price, flux, description, marque);
        this.typeServiette = typeS;
    }
    
}
