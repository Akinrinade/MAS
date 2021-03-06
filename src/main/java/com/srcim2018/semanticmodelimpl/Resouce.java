package com.srcim2018.semanticmodelimpl;

import java.net.URI;
import java.util.Collection;
import javax.xml.datatype.XMLGregorianCalendar;

import org.protege.owl.codegeneration.WrappedIndividual;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * 
 * <p>
 * Generated by Protege (http://protege.stanford.edu). <br>
 * Source Class: Resouce <br>
 * @version generated on Wed Apr 03 18:34:49 WEST 2019 by root
 */

public interface Resouce extends WrappedIndividual {

    /* ***************************************************
     * Property http://www.semanticweb.org/srcim2018/ontologies/srcimOntology/executes
     */
     
    /**
     * Gets all property values for the executes property.<p>
     * 
     * @returns a collection of values for the executes property.
     */
    Collection<? extends Skill> getExecutes();

    /**
     * Checks if the class has a executes property value.<p>
     * 
     * @return true if there is a executes property value.
     */
    boolean hasExecutes();

    /**
     * Adds a executes property value.<p>
     * 
     * @param newExecutes the executes property value to be added
     */
    void addExecutes(Skill newExecutes);

    /**
     * Removes a executes property value.<p>
     * 
     * @param oldExecutes the executes property value to be removed.
     */
    void removeExecutes(Skill oldExecutes);


    /* ***************************************************
     * Property http://www.semanticweb.org/srcim2018/ontologies/srcimOntology/plugged
     */
     
    /**
     * Gets all property values for the plugged property.<p>
     * 
     * @returns a collection of values for the plugged property.
     */
    Collection<? extends Conveyor> getPlugged();

    /**
     * Checks if the class has a plugged property value.<p>
     * 
     * @return true if there is a plugged property value.
     */
    boolean hasPlugged();

    /**
     * Adds a plugged property value.<p>
     * 
     * @param newPlugged the plugged property value to be added
     */
    void addPlugged(Conveyor newPlugged);

    /**
     * Removes a plugged property value.<p>
     * 
     * @param oldPlugged the plugged property value to be removed.
     */
    void removePlugged(Conveyor oldPlugged);


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
     * Common interfaces
     */

    OWLNamedIndividual getOwlIndividual();

    OWLOntology getOwlOntology();

    void delete();

}
