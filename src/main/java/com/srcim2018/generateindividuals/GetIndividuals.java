/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.generateindividuals;

import com.srcim2018.semanticmodelimpl.Conveyor;
import com.srcim2018.semanticmodelimpl.MyFactory;
import com.srcim2018.semanticmodelimpl.Product;
import com.srcim2018.semanticmodelimpl.Resouce;
import java.io.File;
import java.util.Collection;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 * @author Andre Rocha
 */
public class GetIndividuals {
    
    public static Collection<? extends Conveyor> getConveyors() throws OWLOntologyCreationException{
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File("topologyOntology.owl");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        MyFactory myFact = new MyFactory(ontology);

        String myIRI = "http://www.semanticweb.org/srcim2018/ontologies/";
        
        return myFact.getAllConveyorInstances();
    }
    
    public static Collection<? extends Resouce> getResources() throws OWLOntologyCreationException{
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File("topologyOntology.owl");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        MyFactory myFact = new MyFactory(ontology);

        String myIRI = "http://www.semanticweb.org/srcim2018/ontologies/";
        
        return myFact.getAllResouceInstances();
    }
    
    public static Collection<? extends Product> getProductTypes() throws OWLOntologyCreationException{
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File("topologyOntology.owl");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        MyFactory myFact = new MyFactory(ontology);

        String myIRI = "http://www.semanticweb.org/srcim2018/ontologies/";
        
        return myFact.getAllProductInstances();
    }

    public static Product getProductType(String prodType) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File("topologyOntology.owl");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        MyFactory myFact = new MyFactory(ontology);
        
        String myIRI = "http://www.semanticweb.org/srcim2018/ontologies/";
        
        return myFact.getProduct(myIRI + prodType);
    }
}
