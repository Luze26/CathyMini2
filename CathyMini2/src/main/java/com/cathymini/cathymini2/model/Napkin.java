package com.cathymini.cathymini2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author yuzel
 */
@Entity(name="Napkin")
@DiscriminatorValue("Napkin")
public class Napkin extends Product {
    
}