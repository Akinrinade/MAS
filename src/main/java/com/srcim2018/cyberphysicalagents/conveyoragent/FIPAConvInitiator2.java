/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.conveyoragent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import simple.ConveyorReads;

import java.util.ArrayList;

/**
 *
 * @author joao
 */
public class FIPAConvInitiator2 extends AchieveREInitiator{
    
    ACLMessage informMessage;
    //add new variables to hold values of new reads
    public static ConveyorReads sent_read;
    public static ConveyorReads receive_read;

    public FIPAConvInitiator2(Agent a, ACLMessage msg , ACLMessage msg2){
        super(a, msg);
        informMessage = msg2;
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        System.out.println(myAgent.getLocalName() + ": AGREE message received");
        //update the values to ConveyorReads object when objects
        ((ConveyorAgent) myAgent).sentTime = System.currentTimeMillis();
        sent_read.setName(myAgent.getLocalName());
        sent_read.setAction("sent");
        sent_read.setTime(System.currentTimeMillis());

        System.out.println(sent_read);
        System.out.println(sent_read);  ConveyorData.setSTime(System.currentTimeMillis());
        ArrayList <String> str = ((ConveyorAgent)myAgent).HardwareDevices.callOperation(((ConveyorAgent)myAgent).DeviceID,
                               myAgent.getLocalName(), ((ConveyorAgent)myAgent).DeliverProduct, ((ConveyorAgent)myAgent).myInputs);
        //System.out.println(str);
        while(str==null || str.get(0).isEmpty()){
            str = ((ConveyorAgent)myAgent).HardwareDevices.callOperation(((ConveyorAgent)myAgent).DeviceID, 
                               myAgent.getLocalName(), ((ConveyorAgent)myAgent).DeliverProduct, ((ConveyorAgent)myAgent).myInputs);
            //System.out.println(str);
        }
        if(str.get(0).equalsIgnoreCase(myAgent.getLocalName() + "WITHOUT ANY PRODUCT")){
            while(str.get(0).equalsIgnoreCase(myAgent.getLocalName() + "WITHOUT ANY PRODUCT")){
                str = ((ConveyorAgent)myAgent).HardwareDevices.callOperation(((ConveyorAgent)myAgent).DeviceID, 
                           myAgent.getLocalName(), ((ConveyorAgent)myAgent).DeliverProduct, ((ConveyorAgent)myAgent).myInputs);
                //System.out.println(str);
            }
        }
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        System.out.println(myAgent.getLocalName() + ": REFUSE message received");

        informMessage.setContent(myAgent.getLocalName());
        myAgent.send(informMessage);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println(myAgent.getLocalName() + ": INFORM message received");

        if(inform.getContent() != null){
            ArrayList <String> str = ((ConveyorAgent)myAgent).HardwareDevices.callOperation(((ConveyorAgent)myAgent).DeviceID, 
                            myAgent.getLocalName(), "StopConveyor", ((ConveyorAgent)myAgent).myInputs); 
            //System.out.println(str);
            while(str==null){
                str = ((ConveyorAgent)myAgent).HardwareDevices.callOperation(((ConveyorAgent)myAgent).DeviceID, 
                                   myAgent.getLocalName(), "StopConveyor", ((ConveyorAgent)myAgent).myInputs);
                //System.out.println(str);
            }
            
        }
        
        ((ConveyorAgent)myAgent).Occupied = false;
        informMessage.setContent(inform.getContent());
        myAgent.send(informMessage);

    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology("ConveyorToConveyor");
        
        ((ConveyorAgent)myAgent).addBehaviour(new FIPAConvInitiator2(myAgent, msg, this.informMessage));
    }
}
