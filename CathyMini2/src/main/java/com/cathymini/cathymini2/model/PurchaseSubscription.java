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
@Entity(name="Subscription")
@DiscriminatorValue("Subscription")
@NamedQueries({
    @NamedQuery(name="SubscriptionById", query="select object(s) from Subscription s where s.transactionID = :transactionID"),
    @NamedQuery(name="SubscriptionByConsumer", query="select object(s) from Subscription s where s.consumer = :consumer")
})
public class PurchaseSubscription extends Purchase {
    /** Date to store the start date of the subscription
     *  The next payement and delivery date is calculate from the subscription's start date
     */
    @Column(name="startDate")
    private Long startDate;

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }
}
