/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.resourceagent;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

/**
 *
 * @author joao
 */
public class CFPresponder extends ContractNetResponder{
    
        
    public CFPresponder(Agent a,MessageTemplate mt){
        super(a,mt);
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException, FailureException {
        System.out.println(myAgent.getLocalName()+": Processing CFP message");
        ACLMessage msg = cfp.createReply();
        if(((ResourceAgent)myAgent).Occupied == false ){
            msg.setPerformative(ACLMessage.PROPOSE);       
            msg.setContent(myAgent.getLocalName());
        }
        else msg.setPerformative(ACLMessage.REFUSE);
        return msg;
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        System.out.println(myAgent.getLocalName()+": Preparing result of CFP");
        ACLMessage msg = cfp.createReply();
        msg.setPerformative(ACLMessage.INFORM);
        msg.setContent(((ResourceAgent)myAgent).resourcePos);
        return msg;
    }
}
