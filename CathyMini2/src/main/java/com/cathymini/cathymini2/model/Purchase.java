/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * The class {@link Purchase} is an EJB Entity representing a list of {@link PurchaseLine} buy by a {@link Consumer}.
 * @see PurchaseSubscription
 * @author Kraiss
 */
@Entity(name="Purchase")
@Table(name="Purchase")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="purchaseType", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("Purchase")
@NamedQueries({
    @NamedQuery(name="PurchaseById", query="select object(p) from Purchase p where p.transactionID = :transactionID"),
    @NamedQuery(name="PurchaseByConsumer", query="select object(p) from Purchase p where p.consumer = :consumer")
})
public class Purchase implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="transactionID")
    /** Integer primary Key */
    private Long transactionID;
    
    @OneToMany
    /** Purchase {@link PurchaseLine} collection */
    private Collection<PurchaseLine> purchaseLineCollection;
    
    @ManyToOne
    /* Purchase owner */
    private Consumer consumer;
    
    @OneToOne
    /** Consumer {@link DeliveryAddress} for the purchase */
    private DeliveryAddress deliveryAddress;
    
    @OneToOne
    /** Consumer {@link PaymentInfo} for the purchase */
    private PayementInfo payementInfo;
    
    /** List of payement dates for the purchase (unique for Purchase, multiple for Subscription) */
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="payementDate")
    private Collection<Date> PayementDate;
    
    /** List of delivery dates for the purchase (unique for Purchase, multiple for Subscription) */
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="deliveryDate")
    private Collection<Date> DeliveryDate;

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PayementInfo getPayementInfo() {
        return payementInfo;
    }

    public void setPayementInfo(PayementInfo payementInfo) {
        this.payementInfo = payementInfo;
    }

    public Long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Long transactionID) {
        this.transactionID = transactionID;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Collection<PurchaseLine> getPurchaseLineCollection() {
        return purchaseLineCollection;
    }

    public void setPurchaseLineCollection(Collection<PurchaseLine> purchaseLineCollection) {
        this.purchaseLineCollection = purchaseLineCollection;
    }

    public Collection<Date> getPayementDate() {
        return PayementDate;
    }

    public void setPayementDate(Collection<Date> PayementDate) {
        this.PayementDate = PayementDate;
    }

    public Collection<Date> getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(Collection<Date> DeliveryDate) {
        this.DeliveryDate = DeliveryDate;
    }
}