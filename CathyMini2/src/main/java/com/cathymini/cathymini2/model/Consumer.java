package com.cathymini.cathymini2.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author kraiss
 */

@Entity
@NamedQueries({
    @NamedQuery(name="ConsumerByName",
        query="select object(c) from Consumer c where c.username = :username"),
    @NamedQuery(name="ConsumerByMail",
        query="select object(c) from Consumer c where c.mail = :mail")
})
public class Consumer implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="userID")
    private Integer userID;
    
    @Column(name="username")
    private String username;
    @Column(name="pwd")
    private String pwd;
    @Column(name="mail")
    private String mail;
    
    @OneToMany(mappedBy="consumer") 
    private Collection<DeliveryAdress> deliveryCollection;
    @OneToMany(mappedBy="consumer") 
    private Collection<PayementInfo> paymentInfoCollection;

    public Collection<DeliveryAdress> getDeliveryCollection() {
        return deliveryCollection;
    }

    public void setDeliveryCollection(Collection<DeliveryAdress> deliveryCollection) {
        this.deliveryCollection = deliveryCollection;
    }

    public Collection<PayementInfo> getPaymentenInfoCollection() {
        return  paymentInfoCollection;
    }

    public void setPaymentenInfoCollection(Collection<PayementInfo>  paymentInfoCollection) {
        this. paymentInfoCollection =  paymentInfoCollection;
    }

    public Integer getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPwd() {
        return pwd;
    }

    public String getMail() {
        return mail;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
