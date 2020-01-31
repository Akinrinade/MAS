/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.resourceagent;

import com.srcim2018.lowerlevel.ResourceLowLevel;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joao
 */
public class FIPAresponder extends AchieveREResponder {
        
        public FIPAresponder(Agent a, MessageTemplate mt){
            super(a,mt);
        }
        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            System.out.println(myAgent.getLocalName() + ": Processing REQUEST message");         
            ACLMessage msg = request.createReply();
            
            
            if(request.getOntology().equalsIgnoreCase("TakeDown")){
                msg.setPerformative(ACLMessage.INFORM);
                ((ResourceAgent)myAgent).doDelete();
            }
            else if(((ResourceAgent)myAgent).Occupied == false){
                msg.setPerformative(ACLMessage.AGREE);
                ((ResourceAgent)myAgent).Occupied = true;
            }
            else if(((ResourceAgent)myAgent).Occupied == true){
                msg.setPerformative(ACLMessage.REFUSE);
            }
            
            return msg;
        }
        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException{
            System.out.println(myAgent.getLocalName() + ": Preparing result of REQUEST");
            ACLMessage msg = request.createReply();
            msg.setPerformative(ACLMessage.INFORM);
            
            
            if (request.getOntology().equalsIgnoreCase("ProductToResource")){
                String productID = request.getSender().getLocalName();

                ((ResourceAgent)myAgent).nextExecute = request.getContent();
                    //Chamar a camada low level para executar a skill
                    if(((ResourceAgent)myAgent).nextExecute.equals("Entry") || ((ResourceAgent)myAgent).nextExecute.equals("Exit")){
                    try {
                        ResourceLowLevel.executeSkill("",((ResourceAgent)myAgent).nextExecute,productID);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FIPAresponder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        msg.setContent(((ResourceAgent)myAgent).resourcePos);
                    }
                    else{
                        ArrayList <String> str = ((ResourceAgent)myAgent).HardwareDevices.callOperation(((ResourceAgent)myAgent).DeviceID, myAgent.getLocalName(), 
                                ((ResourceAgent)myAgent).nextExecute, ((ResourceAgent)myAgent).myInputs);
                        System.out.println(str);
                        if(str == null || str.get(0).isEmpty()){
                           msg.setContent(null);
                        }
                        else{
                            msg.setContent(((ResourceAgent)myAgent).resourcePos);
                        }      
                    }           
  
            }
            
            ((ResourceAgent)myAgent).Occupied = false;
            return msg;
            
        }
}

