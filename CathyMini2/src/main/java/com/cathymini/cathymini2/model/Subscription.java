/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author anaelle
 */
@Entity(name="Subscription")
@Table(name="Subscription")
@NamedQueries({
    @NamedQuery(name="SubscriptionByName",
        query="select object(s) from Subscription s where s.consumer = :consumer"),
    @NamedQuery(name="DeleteSubscriptionById",
        query="DELETE FROM Subscription s WHERE s.cartID = :id"),
    @NamedQuery(name="SubscriptionByID",
        query="select object(s) from Subscription s where s.cartID=:id")
})
public class Subscription extends Cart{

    
    @Column(name="nbJ")
    private int nbJ;

    public int getNbJ() {
        return nbJ;
    }

    public void setNbJ(int nbJ) {
        this.nbJ = nbJ;
    }


    
}
