package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The class {@link PaymentInfo} is an EJB Entity representing a {@link Consumer} {@link PaymentInfo}.
 * 
 * @author kraiss
 */
@Entity(name="PaymentInfo")
@Table(name="PaymentInfo")
public class PaymentInfos implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="ID")
    /** Integer primary key */
    private Long id;
    
    @ManyToOne
    /** Owner of the {@link PayementInfo} */
    private Consumer consumer;
    
    @Column(name="info")
    private String info;

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
