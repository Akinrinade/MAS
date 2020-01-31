/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.productagent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

/**
 *
 * @author joao
 */
public class FIPAConvInitiator extends AchieveREInitiator{
        
    public FIPAConvInitiator(Agent a, ACLMessage msg){
        super(a, msg);
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        System.out.println(myAgent.getLocalName() + ": AGREE message received");
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println(myAgent.getLocalName() + ": INFORM message received");
     
        if(inform.getOntology().equalsIgnoreCase("Finish")){
            //Volta ao primeiro estado.
            ((ProductAgent)myAgent).myDescription.removeConsumes(((ProductAgent)myAgent).nextSkill);
            ((ProductAgent)myAgent).addBehaviour(new NextSkillBehaviour()); 
        }
        else{
             if(inform.getContent() == null){
                ((ProductAgent)myAgent).addBehaviour(new RequestConvBehaviour());
            }
            else{
                ((ProductAgent)myAgent).productPos = inform.getContent();
                if(!((ProductAgent)myAgent).productPos.equals(((ProductAgent)myAgent).desiredPos)){
                    ((ProductAgent)myAgent).addBehaviour(new RequestConvBehaviour());
                }
                else if(((ProductAgent)myAgent).productPos.equals(((ProductAgent)myAgent).desiredPos)){
                    //Passa para o próximo estado em que faz Request do Resource.
                    ((ProductAgent)myAgent).addBehaviour(new RequestResourceBehaviour());
                }
            }
        }
     
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println("Failure: " + failure.getContent());
        ((ProductAgent)myAgent).addBehaviour(new RequestConvBehaviour());
    }
}
