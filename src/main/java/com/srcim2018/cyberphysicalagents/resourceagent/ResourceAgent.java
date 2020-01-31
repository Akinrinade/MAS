/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.resourceagent;



import com.srcim2018.devicefinder.DeviceFinder;
import com.srcim2018.semanticmodelimpl.Resouce;
import com.srcim2018.semanticmodelimpl.Skill;
import com.srcim2018.utilities.DFInteraction;
import jade.core.Agent;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andre Rocha
 */
public class ResourceAgent extends Agent{
    
    private Resouce myDescription;
    private Skill nextConsume;
    private ArrayList skillStrings = new ArrayList();
    protected String resourcePos;
    protected String nextExecute;
    protected String DeviceID;
    protected Boolean Occupied;
    protected ArrayList<String> myInputs = new ArrayList<String>();
    protected DeviceFinder HardwareDevices;


    @Override
    protected void setup() {
        Object[] arguments = this.getArguments();
        this.myDescription = (Resouce) arguments[0];
        
        DeviceID = this.myDescription.getDeviceID().iterator().next();
        if(!DeviceID.isEmpty()){
           this.HardwareDevices = (DeviceFinder) arguments[1];
        }
        
        myInputs.add("");
        
        Occupied = false;
        
        System.out.println("Resource Deployed: " + myDescription.getID().iterator().next() + " Executes: " + myDescription.getExecutes());
        System.out.println(this.getLocalName() + " - " + DeviceID );
        
        try {
            //Register in DF
            if(this.myDescription.hasExecutes()){
                Collection<?> executesList = this.myDescription.getExecutes();
                resourcePos = this.myDescription.getPlugged().iterator().next().getID().iterator().next();

                Iterator <?> it = executesList.iterator();

                while(it.hasNext()){
                    nextConsume = (Skill) it.next();
                    skillStrings.add(nextConsume.getID().iterator().next());
                }
                    
                DFInteraction.RegisterInDF(this, skillStrings, "Skill");
            }
        } catch (FIPAException ex) {
                Logger.getLogger(ResourceAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.addBehaviour(new CFPresponder(this,MessageTemplate.MatchPerformative(ACLMessage.CFP)));
        this.addBehaviour(new FIPAresponder(this,MessageTemplate.MatchPerformative(ACLMessage.REQUEST)));
        
    }
    
    @Override
    protected void takeDown() {
        try {
            DFInteraction.DeregisterFromDF(this);
        } catch (FIPAException ex) {
            Logger.getLogger(ResourceAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(this.getLocalName() + " is offline");
    }
    
}
