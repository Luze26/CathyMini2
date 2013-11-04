/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Kraiss
 */
@Entity(name="Purchase")
@Table(name="Purchase")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="purchaseType", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("Purchase")
public class Purchase implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="transactionID")
    /** Integer primary Key */
    private Long transactionID;
    
    @ManyToOne
    /* Purchase owner */
    private Consumer consumer;
    
    @OneToOne
    /** Consumer {@link PaymentInfo} for the purchase */
    private PayementInfo payementInfo;
    
    @OneToMany
    /** Purchase {@link Product} collection */
    private Collection<Product> productCollection;

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

    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }
    
}