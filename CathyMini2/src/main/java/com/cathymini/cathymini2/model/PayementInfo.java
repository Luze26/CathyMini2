package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The class {@link PayementInfo} is an EJB Entity representing a {@link Consumer} {@link PayementInfo}.
 * 
 * @author kraiss
 */
@Entity(name="PayementInfo")
@Table(name="PayementInfo")
public class PayementInfo implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="payementInfoID")
    /** Integer primary key */
    private Long payementInfoID;
    
    @ManyToOne
    /** Owner of the {@link PayementInfo} */
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
