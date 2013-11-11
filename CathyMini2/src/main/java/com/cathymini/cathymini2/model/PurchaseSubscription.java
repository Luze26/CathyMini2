/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A purchase represents a {@link Product} list that a user wants to buy every month by a {@link Consumer}.
 * @author Kraiss
 */
@Entity(name="Subscription")
@DiscriminatorValue("Subscription")
public class PurchaseSubscription extends Purchase {
    
}
