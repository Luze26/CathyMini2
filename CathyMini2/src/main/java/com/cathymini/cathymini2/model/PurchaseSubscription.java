/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

/**
 * The class {@link PurchaseSubscription} is an EJB Entity representing purchase made by a consumer every month
 * 
 * @author Kraiss
 */
@Entity(name="PurchaseSubscription")
@DiscriminatorValue("PurchaseSubscription")
@NamedQueries({
    @NamedQuery(name="SubscriptionById", query="select object(s) from PurchaseSubscription s where s.transactionID = :transactionID"),
    @NamedQuery(name="SubscriptionByConsumer", query="select object(s) from PurchaseSubscription s where s.consumer = :consumer")
})
public class PurchaseSubscription extends Purchase {
    /** Number of days between two purchase. It is calculated from the 'creationDate' */
    @Column(name="daysDelay")
    private Integer daysDelay;

    public Integer getDaysDelay() {
        return daysDelay;
    }

    public void setDaysDelay(Integer daysDelay) {
        this.daysDelay = daysDelay;
    }
}
