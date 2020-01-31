/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.conveyoragent;

import com.srcim2018.cyberphysicalagents.productagent.RequestConvBehaviour;
import com.srcim2018.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAException;
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
public class FIPAConvresponder extends AchieveREResponder {
        
        public FIPAConvresponder(Agent a, MessageTemplate mt){
            super(a,mt);
        }
        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            System.out.println(myAgent.getLocalName() + ": Processing REQUEST message");        
            ACLMessage msg = request.createReply();
            
            if(request.getOntology().matches("ProductToConveyor")){
                
                msg.setPerformative(ACLMessage.AGREE);


                ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
                msg2.setOntology("ConveyorToConveyor");
                
                ACLMessage msg3 = request.createReply();                
                msg3.setPerformative(ACLMessage.INFORM);
                
                try {
                    DFAgentDescription[] Receiver = DFInteraction.SearchInDFByName(((ConveyorAgent)myAgent).nextConv,(ConveyorAgent)myAgent);
                    while(Receiver.length ==0){
                        System.out.println("Next Conveyor is unvailable");
                        Receiver = DFInteraction.SearchInDFByName(((ConveyorAgent)myAgent).nextConv,(ConveyorAgent)myAgent);
                    }
                        
                    msg2.addReceiver(DFInteraction.SearchInDFByName(((ConveyorAgent)myAgent).nextConv,
                       (ConveyorAgent)myAgent)[0].getName());
                   
                } catch (FIPAException ex) {         
                    Logger.getLogger(RequestConvBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }      

                registerPrepareResultNotification(new FIPAConvInitiator2(myAgent, msg2, msg3));
                
            }
            else if(request.getOntology().matches("ConveyorToConveyor")){
                
                if(((ConveyorAgent)myAgent).Occupied == true){
                    msg.setPerformative(ACLMessage.REFUSE);
                }
                else {
                    ((ConveyorAgent)myAgent).Occupied = true;
                    ArrayList <String> str = ((ConveyorAgent)myAgent).HardwareDevices.callOperation(((ConveyorAgent)myAgent).DeviceID, 
                                myAgent.getLocalName(), ((ConveyorAgent)myAgent).GetProduct, ((ConveyorAgent)myAgent).myInputs); 
                    //System.out.println(str); 
                    if(str == null || str.get(0).isEmpty()){
                        msg.setPerformative(ACLMessage.INFORM);
                        msg.setContent(null);
                        ((ConveyorAgent)myAgent).Occupied = false;
                    }
                    else{
                        ((ConveyorAgent)myAgent).msgToPreviousConv = msg;
                        myAgent.addBehaviour(new CheckSensorStatus());
                        msg.setPerformative(ACLMessage.AGREE);
                    }           
                }
            }
            else if(request.getOntology().equalsIgnoreCase("TakeDown")){
                ((ConveyorAgent)myAgent).doDelete();
                msg.setPerformative(ACLMessage.INFORM);
            }
            else if(request.getOntology().equalsIgnoreCase("Finish")){
                System.out.println(myAgent.getLocalName() + ": Finish Message Received");
                ((ConveyorAgent)myAgent).Occupied = false;
                msg.setOntology("Finish");
                msg.setPerformative(ACLMessage.INFORM);
            }

            return msg;
        }
        
        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException{
            System.out.println(myAgent.getLocalName() + ": Preparing result of REQUEST");
            ACLMessage msg = request.createReply();
            msg.setPerformative(ACLMessage.INFORM);
            msg.setContent(myAgent.getLocalName());

            return msg;
        }
}

