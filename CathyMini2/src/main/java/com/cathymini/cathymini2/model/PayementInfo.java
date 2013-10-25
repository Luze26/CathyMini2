package com.cathymini.cathymini2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The class {@link Consumer} is an EJB entity representing an user.<br/>
 * It contains two named queries '<code>ConsumerByName</code>' and '<code>ConsumerByMail</code>'.
 * 
 * @see DeliveryAdress
 * @see PayementInfo
 * @author kraiss
 */
@Entity
public class PayementInfo implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="ID")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name="userID")
    private Consumer consumer; // foreign key
    
    @Column(name="info")
    private String info;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
