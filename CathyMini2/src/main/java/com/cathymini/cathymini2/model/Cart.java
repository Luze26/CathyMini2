package com.cathymini.cathymini2.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The class {@link Cart} is the EJB Entity representing a cart, 
 *   a list of product that a user want to buy.
 * It contains the named query '<code>CartByName</code>'
 * 
 * @author Kraiss
 */
@Entity(name="Cart")
@Table(name="Cart")

@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Cart", discriminatorType=DiscriminatorType.STRING, length=15)


@NamedQueries({
    @NamedQuery(name="CartByName",
        query="select object(c) from Cart c where c.consumer = :consumer and TYPE(c) = Cart"),
    @NamedQuery(name="DeleteCartById",
        query="DELETE FROM Cart c WHERE c.cartID = :id"),
    @NamedQuery(name="CartByID",
        query="select object(c) from Cart c where c.cartID=:id")
})
public class Cart implements Serializable {
    /** Integer primary Key */
    @Id
    @GeneratedValue
    @Column(name="cartID")
    private Long cartID;
    
    /* Cart owner */
    @OneToOne
    private Consumer consumer;
    
    /** Cart {@link CartLine} collection */
    @OneToMany
    @Basic(fetch = FetchType.EAGER)
    private Collection<CartLine> cartLineCollection;

    public Collection<CartLine> getCartLineCollection() {
        return cartLineCollection;
    }

    public void setCartLineCollection(Collection<CartLine> cartLineCollection) {
        this.cartLineCollection = cartLineCollection;
    }

    public Long getCartID() {
        return cartID;
    }

    public void setCartID(Long cartID) {
        this.cartID = cartID;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
    
}
