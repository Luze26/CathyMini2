package com.cathymini.cathymini2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author yuzel
 */
@Entity(name="Tampon")
@DiscriminatorValue("Tampon")
public class Tampon extends Product {

}
