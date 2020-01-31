/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.productagent;

import com.srcim2018.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joao
 */
public class FIPAResInitiator extends AchieveREInitiator {
    
    public FIPAResInitiator(Agent a, ACLMessage msg){
        super(a, msg);
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        System.out.println(myAgent.getLocalName() + ": AGREE message received");
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println(myAgent.getLocalName() + ": INFORM message received");

        ((ProductAgent)myAgent).productPos = inform.getContent();
        if(((ProductAgent)myAgent).productPos == null){
            System.out.println("Response is: " + ((ProductAgent)myAgent).productPos);
            ((ProductAgent)myAgent).addBehaviour(new RequestResourceBehaviour());
        }
        else{
            //Disponibilizar o ultimo conveyor
            if(inform.getSender().getLocalName().equalsIgnoreCase("ResourceExit")){
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                try {
                    DFAgentDescription[] Receiver = DFInteraction.SearchInDFByName(((ProductAgent)myAgent).productPos,
                            (ProductAgent)myAgent);
                    if(Receiver.length != 0){
                        msg.addReceiver(Receiver[0].getName());
                        msg.setOntology("Finish");
                        ((ProductAgent)myAgent).addBehaviour(new FIPAConvInitiator((ProductAgent)myAgent,msg));
                    } 
                } catch (FIPAException ex) {
                    Logger.getLogger(RequestConvBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                //Volta ao primeiro estado.
                ((ProductAgent)myAgent).myDescription.removeConsumes(((ProductAgent)myAgent).nextSkill);
                ((ProductAgent)myAgent).addBehaviour(new NextSkillBehaviour()); 
            }
        }  
    }
        
    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println("Failure: " + failure.getContent());
        ((ProductAgent)myAgent).addBehaviour(new RequestResourceBehaviour());
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        System.out.println(refuse.getSender().getLocalName() + " is Occupied");
        ((ProductAgent)myAgent).addBehaviour(new RequestResourceBehaviour());
    }
}
