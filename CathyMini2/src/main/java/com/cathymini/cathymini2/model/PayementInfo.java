package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The class {@link Consumer} is an EJB entity representing an user.<br/>
 * It contains two named queries '<code>ConsumerByName</code>' and '<code>ConsumerByMail</code>'.
 * 
 * @see DeliveryAdress
 * @see PayementInfo
 * @author kraiss
 */
@Entity(name="PayementInfo")
@Table(name="PayementInfo")
public class PayementInfo implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="payementInfoID")
    private Long payementInfoID;
    
    @ManyToOne
    private Consumer consumer;
    
    @Column(name="info")
    private String info;

    public Long getCartId() {
        return payementInfoID;
    }

    public void setCartId(Long cartID) {
        this.payementInfoID = cartID;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
