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
import com.srcim2018.semanticmodelimpl.Skill;
import java.io.File;
import java.io.IOException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 *
 * @author andrerocha
 */
public class GenerateIndividuals {

    /**
     * @param args the command line arguments
     * @throws org.semanticweb.owlapi.model.OWLOntologyCreationException
     * @throws java.io.IOException
     * @throws org.semanticweb.owlapi.model.OWLOntologyStorageException
     */
    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File("topologyOntology.owl");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        MyFactory myFact = new MyFactory(ontology);

        //Skills
        //Entry
        Skill mySkillEntry = myFact.createSkill("Entry");
        mySkillEntry.addID("Entry");
        mySkillEntry.addDescription("The operator adds a new product in the entry point");
        //Exit
        Skill mySkillExit = myFact.createSkill("Exit");
        mySkillExit.addID("Exit");
        mySkillExit.addDescription("The operator removes the product from the exit point");
        //BrushUp
        Skill mySkillBrushUp = myFact.createSkill("BrushUp");
        mySkillBrushUp.addID("BrushUp");
        mySkillBrushUp.addDescription("Brushes the product in up position");
        //BrushDown
        Skill mySkillBrushDown = myFact.createSkill("BrushDown");
        mySkillBrushDown.addID("BrushDown");
        mySkillBrushDown.addDescription("Brushes the product in down position");

        //Conveyors
        //ConveyorA
        Conveyor myConveyorA = myFact.createConveyor("ConveyorA");
        myConveyorA.addID("ConveyorA");
        myConveyorA.addDescription("Conveyor in entry and exit position");
        myConveyorA.addNextConveyor("ConveyorB");
        //ConveyorB
        Conveyor myConveyorB = myFact.createConveyor("ConveyorB");
        myConveyorB.addID("ConveyorB");
        myConveyorB.addDescription("Conveyor in position B");
        myConveyorB.addNextConveyor("ConveyorC");
        //ConveyorC
        Conveyor myConveyorC = myFact.createConveyor("ConveyorC");
        myConveyorC.addID("ConveyorC");
        myConveyorC.addDescription("Conveyor in position C with rotation");
        myConveyorC.addNextConveyor("ConveyorD");
        //ConveyorD
        Conveyor myConveyorD = myFact.createConveyor("ConveyorD");
        myConveyorD.addID("ConveyorD");
        myConveyorD.addDescription("Conveyor in position D");
        myConveyorD.addNextConveyor("ConveyorE");
        //ConveyorE
        Conveyor myConveyorE = myFact.createConveyor("ConveyorE");
        myConveyorE.addID("ConveyorE");
        myConveyorE.addDescription("Conveyor in position E with rotation");
        myConveyorE.addNextConveyor("ConveyorF");
        //ConveyorF
        Conveyor myConveyorF = myFact.createConveyor("ConveyorF");
        myConveyorF.addID("ConveyorF");
        myConveyorF.addDescription("Conveyor in position F");
        myConveyorF.addNextConveyor("ConveyorA");

        //Resources
        //ResourceEntry
        Resouce myResourceEntry = myFact.createResouce("ResourceEntry");
        myResourceEntry.addID("ResourceEntry");
        myResourceEntry.addDescription("Human operator that adds the product in entry position");
        myResourceEntry.addExecutes(mySkillEntry);
        myResourceEntry.addPlugged(myConveyorA);
        //ResourceBrush
        Resouce myResourceBrush = myFact.createResouce("ResourceBrush");
        myResourceBrush.addID("ResourceBrush");
        myResourceBrush.addDescription("Brush station with two positions");
        myResourceBrush.addExecutes(mySkillBrushUp);
        myResourceBrush.addExecutes(mySkillBrushDown);
        myResourceEntry.addPlugged(myConveyorD);
        //ResourceExit
        Resouce myResourceExit = myFact.createResouce("ResourceExit");
        myResourceExit.addID("ResourceExit");
        myResourceExit.addDescription("Human operator that removes the product from exit position");
        myResourceExit.addExecutes(mySkillExit);
        myResourceEntry.addPlugged(myConveyorA);

        //Product Types - All of the products execute entry and exit skills
        //Product Type 1 - with Brush Up
        Product myProductType1 = myFact.createProduct("ProductType1");
        myProductType1.addID("ProductType1");
        myProductType1.addDescription("Type of products which consumes BrushUp skill");
        myProductType1.addConsumes(mySkillEntry);
        myProductType1.addConsumes(mySkillBrushUp);
        myProductType1.addConsumes(mySkillExit);
        //Product Type 2 - with Brush Down
        Product myProductType2 = myFact.createProduct("ProductType2");
        myProductType2.addID("ProductType2");
        myProductType2.addDescription("Type of products which consumes BrushDown skill");
        myProductType2.addConsumes(mySkillEntry);
        myProductType2.addConsumes(mySkillBrushDown);
        myProductType2.addConsumes(mySkillExit);
        //Product Type 3 - with Brush Up and Brush Down
        Product myProductType3 = myFact.createProduct("ProductType3");
        myProductType3.addID("ProductType3");
        myProductType3.addDescription("Type of products which consumes BrushUp and BrushDown skills");
        myProductType3.addConsumes(mySkillEntry);
        myProductType3.addConsumes(mySkillBrushUp);
        myProductType3.addConsumes(mySkillBrushDown);
        myProductType3.addConsumes(mySkillExit);

        //Save ontology
        manager.saveOntology(ontology);
    }

}
