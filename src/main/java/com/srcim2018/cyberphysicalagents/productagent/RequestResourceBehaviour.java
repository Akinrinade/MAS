/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.productagent;

import com.srcim2018.utilities.DFInteraction;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joao
 */
public class RequestResourceBehaviour extends OneShotBehaviour{
 
    @Override
    public void action(){
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        try {
            DFAgentDescription[] Receiver = DFInteraction.SearchInDFByName(((ProductAgent)myAgent).nextSkill.getID().iterator().next(),
                    (ProductAgent)myAgent);
            if(Receiver.length != 0){
                //((ProductAgent)myAgent).ResourceRequested = Receiver[0].getName();
                //msg.addReceiver(((ProductAgent)myAgent).ResourceRequested);
                msg.addReceiver(Receiver[0].getName());
                msg.setContent(((ProductAgent)myAgent).nextSkill.getID().iterator().next());
                msg.setOntology("ProductToResource");
                ((ProductAgent)myAgent).addBehaviour(new FIPAResInitiator((ProductAgent)myAgent,msg));
            }
            else{
                //System.out.println("No " + ((ProductAgent)myAgent).ResourceRequested.getLocalName() + " online");
                ((ProductAgent)myAgent).addBehaviour(new RequestResourceBehaviour());
            }
        } catch (FIPAException ex) {
            Logger.getLogger(RequestResourceBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
