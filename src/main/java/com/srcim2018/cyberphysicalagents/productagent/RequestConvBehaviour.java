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
public class RequestConvBehaviour extends OneShotBehaviour{
    
    @Override
    public void action(){
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology("ProductToConveyor");
         try {
            DFAgentDescription[] Receiver = DFInteraction.SearchInDFByName(((ProductAgent)myAgent).productPos,
                    (ProductAgent)myAgent);
            if(Receiver.length != 0){
                msg.addReceiver(Receiver[0].getName());
                msg.setContent(((ProductAgent)myAgent).desiredPos);
                ((ProductAgent)myAgent).addBehaviour(new FIPAConvInitiator((ProductAgent)myAgent,msg));
            } else{
                //System.out.println("No " + ((ProductAgent)myAgent).productPos + " online");
                ((ProductAgent)myAgent).addBehaviour(new RequestConvBehaviour());
            }
        } catch (FIPAException ex) {
            Logger.getLogger(RequestConvBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
