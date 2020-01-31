/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.conveyoragent;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

/**
 *
 * @author joao
 */
public class CheckSensorStatus extends OneShotBehaviour{
    
    protected boolean sensorStatus = false;


    @Override
    public void action() {

        while(!sensorStatus){
            ArrayList <String> resp = ((ConveyorAgent)myAgent).HardwareDevices.callOperation(((ConveyorAgent)myAgent).DeviceID, myAgent.getLocalName(), "CheckSensor",((ConveyorAgent)myAgent).myInputs);
            //System.out.println(resp);
            if (resp.get(0).equalsIgnoreCase("true")){
                sensorStatus = true;
                ((ConveyorAgent) myAgent).receivedTime = System.currentTimeMillis();
                ConveyorData.setRTime(System.currentTimeMillis());

                SequentialBehaviour pmBehavior = new SequentialBehaviour((ConveyorAgent) myAgent);
                pmBehavior.addSubBehaviour(new PreprocessBehaviour((ConveyorAgent) myAgent));
                pmBehavior.addSubBehaviour(new DroolsBehaviour());
                pmBehavior.addSubBehaviour(new RuleUpdateBehaviour());


                ACLMessage msg = ((ConveyorAgent)myAgent).msgToPreviousConv;
                msg.setPerformative(ACLMessage.INFORM);
                msg.setContent(myAgent.getLocalName());
                myAgent.send(msg);

            }
            
            
        }
    }
    
    
}
