/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    /** Integer primary Key */
    @Id
    @GeneratedValue
    @Column(name="transactionID")
    private Long transactionID;
    
    /** Purchase {@link PurchaseLine} collection */
    @OneToMany
    @Basic(fetch = FetchType.EAGER)
    private Collection<PurchaseLine> purchaseLineCollection;
    
    /* Purchase owner */
    @ManyToOne
    private Consumer consumer;
    
    /** Cost of the purchase */
    @Column(name="totalCost")
    private Float totalCost;
    
    /** Consumer {@link DeliveryAddress} for the purchase */
    @OneToOne
    private DeliveryAddress deliveryAddress;
    
    /** Consumer {@link PaymentInfo} for the purchase */
    @OneToOne
    private PaymentInfos payementInfo;
    
    /** Creation date of the purchase */
    @Column(name="creationDate")
    private Long creationDate;
    
    /** Payement date of the purchase */
    @Column(name="payementDate")
    private Long payementDate;
    
    /** Delivery date of the purchase */
    @Column(name="deliveryDate")
    private Long deliveryDate;

    public Float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Float totalCost) {
        this.totalCost = totalCost;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Long getPayementDate() {
        return payementDate;
    }

    public void setPayementDate(Long payementDate) {
        this.payementDate = payementDate;
    }

    public Long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public PaymentInfos getPayementInfo() {
        return payementInfo;
    }

    public void setPayementInfo(PaymentInfos payementInfo) {
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
}
