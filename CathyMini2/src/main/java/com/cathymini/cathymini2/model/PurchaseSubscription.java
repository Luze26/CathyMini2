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

/**
 * The class {@link PurchaseSubscription} is an EJB Entity representing purchase made by a consumer every month
 * 
 * @author Kraiss
 */
@Entity(name="PurchaseSubscription")
@DiscriminatorValue("PurchaseSubscription")
@NamedQueries({
    @NamedQuery(name="SubscriptionById", query="select object(s) from PurchaseSubscription s where s.transactionID = :transactionID"),
    @NamedQuery(name="SubscriptionByConsumer", query="select object(s) from PurchaseSubscription s where s.consumer = :consumer"),
    @NamedQuery(name="AllSubscription", query="select object(s) from PurchaseSubscription s")
})
public class PurchaseSubscription extends Purchase {
    /** Next delivery date */
    @Column(name="nextDelivery")
    private Long nextDelivery;
    
    /** Number of days between two purchase. It is calculated from the 'creationDate' */
    @Column(name="daysDelay")
    private Integer daysDelay;
    
    /** Name of the purchase subscription */
    @Column(name="name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNextDelivery() {
        return nextDelivery;
    }

    public void setNextDelivery(Long nextDelivery) {
        this.nextDelivery = nextDelivery;
    }
    
    public Integer getDaysDelay() {
        return daysDelay;
    }

    public void setDaysDelay(Integer daysDelay) {
        this.daysDelay = daysDelay;
    }
}
