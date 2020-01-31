/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.productagent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import java.util.Vector;

/**
 *
 * @author joao
 */
public class CFPinitiator extends ContractNetInitiator{
    /* PROTOCOLO FIPA CONTRACTNET */
          
            
    public CFPinitiator(Agent a, ACLMessage msg){
        super(a,msg);
    }

    @Override
    protected void handleInform(ACLMessage inform){
        System.out.println(myAgent.getLocalName()+ ": INFORM message received");
        //((ProductAgent)myAgent).ResourceRequested = inform.getSender();
        ((ProductAgent)myAgent).desiredPos = inform.getContent();
        
        String ss1 = ((ProductAgent)myAgent).desiredPos;
        String ss2 = ((ProductAgent)myAgent).productPos;
        if(ss1.equals(ss2)){
            //Passa para o estado em que manda Request ao Resource
            ((ProductAgent)myAgent).addBehaviour(new RequestResourceBehaviour());
        }
        else { //Passa para o estado em que manda Request ao Conveyor
            ((ProductAgent)myAgent).addBehaviour(new RequestConvBehaviour());
        }       
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances){
        System.out.println(myAgent.getLocalName()+ ": All PROPOSALS received");
        
        if(!responses.isEmpty()){
            ACLMessage auxMsg = (ACLMessage)responses.get(0);
          
            if(auxMsg.getPerformative() != ACLMessage.PROPOSE){
                int i=1;
                while(auxMsg.getPerformative() != ACLMessage.PROPOSE){
                    auxMsg = (ACLMessage)responses.get(i);
                    i++;
                } 
            }
            ACLMessage reply = auxMsg.createReply();
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            acceptances.add(reply);
            
        }else{
            System.out.println("Requested Resources are Occupied");
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            msg.setContent(((ProductAgent)myAgent).nextSkill.toString());
            ((ProductAgent)myAgent).addBehaviour(new CFPinitiator((ProductAgent)myAgent,msg));
        }
    }
}
