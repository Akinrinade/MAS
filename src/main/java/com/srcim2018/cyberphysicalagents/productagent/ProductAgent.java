/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.productagent;

import com.srcim2018.semanticmodelimpl.Product;
import com.srcim2018.semanticmodelimpl.Skill;
import com.srcim2018.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andre Rocha
 */
public class ProductAgent extends Agent {

    protected Product myDescription;
    protected Skill nextSkill;
    protected ArrayList productStrings = new ArrayList();
    protected String productPos;
    protected String desiredPos;
    //protected AID ResourceRequested;
    
    protected static int prodCounter = 1;

    @Override
    protected void setup() {
        Object[] arguments = this.getArguments();
        this.myDescription = (Product) arguments[0];
        
        System.out.println("Product Deployed: " + this.getLocalName() + " Skills: " + myDescription.getConsumes());
        
        productStrings.add(getLocalName());
        
         try {
            //Register in DF
            DFInteraction.RegisterInDF(this, productStrings, "ProductAgent");
        } catch (FIPAException ex) {
            Logger.getLogger(ProductAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        productPos = myDescription.getInitialPosition().iterator().next();
        
        this.addBehaviour(new NextSkillBehaviour());
    }

    @Override
    protected void takeDown() { 
    }
}
