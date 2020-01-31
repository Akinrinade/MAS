/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.generateindividuals;

import java.io.IOException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 *
 * @author andrerocha
 */
public class TestIndividuals {

    /**
     * @param args the command line arguments
     * @throws org.semanticweb.owlapi.model.OWLOntologyCreationException
     * @throws java.io.IOException
     * @throws org.semanticweb.owlapi.model.OWLOntologyStorageException
     */
    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        System.err.println(GetIndividuals.getConveyors());
        System.err.println(GetIndividuals.getResources());
        System.err.println(GetIndividuals.getProductTypes());
        System.err.println(GetIndividuals.getProductTypes().iterator().next().getConsumes().iterator().next().getPriority().iterator().next());

    }

}
