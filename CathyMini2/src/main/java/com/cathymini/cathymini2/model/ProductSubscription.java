/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Kraiss
 */
@Entity(name="Subscription")
@DiscriminatorValue("Subscription")
public class ProductSubscription extends Purchase {
    
}
