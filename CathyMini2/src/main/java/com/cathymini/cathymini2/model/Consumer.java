package com.cathymini.cathymini2.model;

import com.cathymini.cathymini2.webservices.secure.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The class {@link Consumer} is an EJB Entity representing an user.<br/>
 * It contains two named queries '<code>ConsumerByName</code>' and '<code>ConsumerByMail</code>'.
 * 
 * @see DeliveryAdress
 * @see PayementInfo
 * @author kraiss
 */
@Entity(name="Consumer")
@Table(name = "Consumer",
        uniqueConstraints={@UniqueConstraint(columnNames={"username", "mail"})}
)
@NamedQueries({
    @NamedQuery(name="ConsumerByName",
        query="select object(c) from Consumer c where c.username = :username"),
    @NamedQuery(name="ConsumerByMail",
        query="select object(c) from Consumer c where c.mail = :mail"),
    @NamedQuery(name="ConsumerById",
        query="select object(c) from Consumer c where c.userID = :userID")
})
public class Consumer implements Serializable {

    /**
     * Integer primary Key
     */
    @Id
    @GeneratedValue
    @Column(name="userID")
    private Long userID;

    /**
     * Consumer username
     */
    @Column(name="username")
    private String username;
    
    @Column(name="pwd")
    /** Consumer password */
    private String pwd;
    
    @Column(name="mail")
    /** Consumer mail address */
    private String mail;

    /**
     * Consumer role
     */
    @Column(name="role")
    private Role role;

    /**
     * Consumer token, used to reset password
     */
    @Column(name = "token")
    private String token;

    @OneToMany(mappedBy = "consumer")
    @Basic(fetch = FetchType.EAGER)
    /** Consumer {@link DeliveryAddress} collection */
    private Collection<DeliveryAddress> deliveryCollection;
    
    @OneToMany(mappedBy = "consumer")
    @Basic(fetch = FetchType.EAGER)
    /** Consumer {@link PaymentInfo} collection */
    private Collection<PaymentInfos> paymentInfoCollection;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Collection<PaymentInfos> getPaymentInfoCollection() {
        return paymentInfoCollection;
    }

    public void setPaymentInfoCollection(Collection<PaymentInfos> paymentInfoCollection) {
        this.paymentInfoCollection = paymentInfoCollection;
    }

    public Collection<DeliveryAddress> getDeliveryCollection() {
        return deliveryCollection;
    }

    public void setDeliveryCollection(Collection<DeliveryAddress> deliveryCollection) {
        this.deliveryCollection = deliveryCollection;
    }

    public void addDelivery(DeliveryAddress address) {
        if (deliveryCollection == null) {
            deliveryCollection = new ArrayList<DeliveryAddress>();
        }
        deliveryCollection.add(address);
    }
    
     public void deleteDelivery(DeliveryAddress address) {
        if (deliveryCollection.contains(address)) {
            deliveryCollection.remove(address);
        }
    }

    public Long getUserID() {
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

    public String getToken() {
        return token;
    }

    public void setUserID(Long userID) {
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

    public void setToken(String token) {
        this.token = token;
    }
}
