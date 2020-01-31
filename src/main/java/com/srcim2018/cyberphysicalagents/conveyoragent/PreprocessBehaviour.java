package com.srcim2018.cyberphysicalagents.conveyoragent;


import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;


public class PreprocessBehaviour extends OneShotBehaviour{
//       private  ConveyorAgent myAgent;

    public PreprocessBehaviour(ConveyorAgent a) {

        super(a);
    }

    @Override
    public void action() {
        Long sentTime = ((ConveyorAgent) myAgent).receivedTime;
        Long transportTime;
//        myAgent.sentTime =ConveyorData.getSTime();
        Long receivedTime = ((ConveyorAgent) myAgent).receivedTime;
//        Long sentTime = ConveyorData.getSTime();
//        Long receivedTime = ConveyorData.getRTime();
        /*
        perform simple data processing in this case simple timedelta
         */
        transportTime = sentTime - receivedTime;

        ConveyorData.settransportTime(transportTime);
        ((ConveyorAgent) myAgent).transportTime = transportTime;
    }


}
