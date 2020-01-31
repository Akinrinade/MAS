package com.srcim2018.cyberphysicalagents.conveyoragent;
import jade.core.behaviours.OneShotBehaviour;
import simple.Simple;
import simple.creatrules;

import java.util.HashMap;

public class RuleUpdateBehaviour extends OneShotBehaviour {
    @Override
    public void action() {
        HashMap thresholds = Simple.getThresholds();
        try {
            creatrules.Drl_Creator.create_drl(thresholds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
