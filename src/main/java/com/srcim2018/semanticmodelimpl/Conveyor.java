package com.srcim2018.semanticmodelimpl;

import java.util.Collection;

import org.protege.owl.codegeneration.WrappedIndividual;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 * <p>
 * Generated by Protege (http://protege.stanford.edu). <br>
 * Source Class: Conveyor <br>
 * @version generated on Wed Apr 03 18:34:49 WEST 2019 by root
 */

public interface Conveyor extends WrappedIndividual {

    /* ***************************************************
     * Property http://www.semanticweb.org/srcim2018/ontologies/srcimOntology#DeviceID
     */
     
    /**
     * Gets all property values for the DeviceID property.<p>
     * 
     * @returns a collection of values for the DeviceID property.
     */
    Collection<? extends String> getDeviceID();

    /**
     * Checks if the class has a DeviceID property value.<p>
     * 
     * @return true if there is a DeviceID property value.
     */
    boolean hasDeviceID();

    /**
     * Adds a DeviceID property value.<p>
     * 
     * @param newDeviceID the DeviceID property value to be added
     */
    void addDeviceID(String newDeviceID);

    /**
     * Removes a DeviceID property value.<p>
     * 
     * @param oldDeviceID the DeviceID property value to be removed.
     */
    void removeDeviceID(String oldDeviceID);



    /* ***************************************************
     * Property http://www.semanticweb.org/srcim2018/ontologies/srcimOntology/Description
     */
     
    /**
     * Gets all property values for the Description property.<p>
     * 
     * @returns a collection of values for the Description property.
     */
    Collection<? extends String> getDescription();

    /**
     * Checks if the class has a Description property value.<p>
     * 
     * @return true if there is a Description property value.
     */
    boolean hasDescription();

    /**
     * Adds a Description property value.<p>
     * 
     * @param newDescription the Description property value to be added
     */
    void addDescription(String newDescription);

    /**
     * Removes a Description property value.<p>
     * 
     * @param oldDescription the Description property value to be removed.
     */
    void removeDescription(String oldDescription);



    /* ***************************************************
     * Property http://www.semanticweb.org/srcim2018/ontologies/srcimOntology/ID
     */
     
    /**
     * Gets all property values for the ID property.<p>
     * 
     * @returns a collection of values for the ID property.
     */
    Collection<? extends String> getID();

    /**
     * Checks if the class has a ID property value.<p>
     * 
     * @return true if there is a ID property value.
     */
    boolean hasID();

    /**
     * Adds a ID property value.<p>
     * 
     * @param newID the ID property value to be added
     */
    void addID(String newID);

    /**
     * Removes a ID property value.<p>
     * 
     * @param oldID the ID property value to be removed.
     */
    void removeID(String oldID);



    /* ***************************************************
     * Property http://www.semanticweb.org/srcim2018/ontologies/srcimOntology/NextConveyor
     */
     
    /**
     * Gets all property values for the NextConveyor property.<p>
     * 
     * @returns a collection of values for the NextConveyor property.
     */
    Collection<? extends String> getNextConveyor();

    /**
     * Checks if the class has a NextConveyor property value.<p>
     * 
     * @return true if there is a NextConveyor property value.
     */
    boolean hasNextConveyor();

    /**
     * Adds a NextConveyor property value.<p>
     * 
     * @param newNextConveyor the NextConveyor property value to be added
     */
    void addNextConveyor(String newNextConveyor);

    /**
     * Removes a NextConveyor property value.<p>
     * 
     * @param oldNextConveyor the NextConveyor property value to be removed.
     */
    void removeNextConveyor(String oldNextConveyor);



    /* ***************************************************
     * Common interfaces
     */

    OWLNamedIndividual getOwlIndividual();

    OWLOntology getOwlOntology();

    void delete();

}
