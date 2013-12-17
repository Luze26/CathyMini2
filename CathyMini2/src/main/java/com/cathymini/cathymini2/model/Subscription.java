/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author anaelle
 */
@Entity(name="Subscription")
@Table(name="Subscription")
@DiscriminatorValue("Subscription")
@NamedQueries({
    @NamedQuery(name="SubscriptionByCons",
        query="select object(s) from Subscription s where s.consumer = :consumer and type(s) = Subscription"),
    @NamedQuery(name="SubscriptionByName",
        query="select object(s) from Subscription s where s.consumer = :consumer and s.name = :name and type(s) = Subscription"),
    @NamedQuery(name="DeleteSubscriptionById",
        query="DELETE FROM Subscription s WHERE s.cartID = :id"),
    @NamedQuery(name="SubscriptionByID",
        query="select object(s) from Subscription s where s.cartID=:id")
})
public class Subscription extends Cart{

    
    @Column(name="nbJ")
    private int nbJ;
    
    @Column(name="name")
    private String name;

    public int getNbJ() {
        return nbJ;
    }

    public void setNbJ(int nbJ) {
        this.nbJ = nbJ;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }


    
}
