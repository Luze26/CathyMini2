package com.cathymini.cathymini2.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@NamedQueries({
    @NamedQuery(name="CartByName",
        query="select object(c) from Cart c where c.consumer = :consumer"),
})
public class Cart implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="cartID")
    /** Integer primary Key */
    private Long cartID;
    
    @OneToOne
    /* Cart owner */
    private Consumer consumer;
    
    @OneToMany
    /** Cart {@link Product} collection */
    private Collection<Product> productCollection;

    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
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
