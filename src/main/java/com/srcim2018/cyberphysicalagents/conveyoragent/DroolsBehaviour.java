package com.srcim2018.cyberphysicalagents.conveyoragent;

import jade.core.behaviours.OneShotBehaviour;
import simple.Simple;

public class DroolsBehaviour extends OneShotBehaviour {

    @Override
    public void action() {

//        Simple.main(ConveyorData.getCname(), ConveyorData.gettransportTime(), ConveyorData.getSTime());
        Simple.main(ConveyorData.getCname(), ((ConveyorAgent) myAgent).transportTime, ((ConveyorAgent) myAgent).sentTime);

    }
}
