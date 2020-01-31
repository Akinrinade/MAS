/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.startplatform;

import com.srcim2018.cyberphysicalagents.conveyoragent.ConveyorAgent;
import com.srcim2018.cyberphysicalagents.productagent.RequestConvBehaviour;
import com.srcim2018.cyberphysicalagents.resourceagent.ResourceAgent;
import com.srcim2018.generateindividuals.GetIndividuals;
import com.srcim2018.semanticmodelimpl.Conveyor;
import com.srcim2018.semanticmodelimpl.Resouce;
import com.srcim2018.utilities.DFInteraction;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 *
 * @author joao
 */
public class GetPluggedDevicesBehaviour extends TickerBehaviour{
    
    private ArrayList<String> DevicesIDs = new ArrayList<>();
    
    public GetPluggedDevicesBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        try {
            checkHardware();
        } catch (StaleProxyException | OWLOntologyCreationException ex) {
            Logger.getLogger(GetPluggedDevicesBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void checkHardware() throws StaleProxyException, OWLOntologyCreationException{
        String Content;
        String Content2;
        DevicesIDs = ((ConsoleAgent)myAgent).myDevices.getDevicesIDs();
        for(int i=0; i<((ConsoleAgent)myAgent).PluggedDevices.size();i++){
            Content = ((ConsoleAgent)myAgent).PluggedDevices.get(i);
            if(!DevicesIDs.contains(Content)){
                System.out.println(((ConsoleAgent)myAgent).HardwareDevices.get(Content) + " to kill");
                UnplugDetected(((ConsoleAgent)myAgent).HardwareDevices.get(Content));  
                ((ConsoleAgent)myAgent).PluggedDevices.remove(Content);
            } 
            
        }
        for(int i=0;i<DevicesIDs.size();i++){
            Content2 = DevicesIDs.get(i);
            if(!((ConsoleAgent)myAgent).PluggedDevices.contains(Content2)){
                System.out.println(((ConsoleAgent)myAgent).HardwareDevices.get(Content2) + " to launch");
                PlugDetected(Content2);
                ((ConsoleAgent)myAgent).PluggedDevices.add(Content2);
            }
        }
        
    }
    
    protected void PlugDetected(String DeviceID) throws StaleProxyException, OWLOntologyCreationException{
        String AgentName = ((ConsoleAgent)myAgent).HardwareDevices.get(DeviceID);
        if(AgentName.contains("Conveyor")){
            Collection<? extends Conveyor> conveyors = GetIndividuals.getConveyors();
            Iterator<? extends Conveyor> ConveyorsIterator = conveyors.iterator();
            while (ConveyorsIterator.hasNext()) {
                Conveyor next = ConveyorsIterator.next();
                if(next.getDeviceID().iterator().next().equalsIgnoreCase(DeviceID)){
                    ConveyorAgent newConveyor = new ConveyorAgent();
                    //newConveyor.setArguments(new Object[]{next});
                    newConveyor.setArguments(new Object[]{next, ((ConsoleAgent)myAgent).myDevices});
                    AgentController agent = ((ConsoleAgent)myAgent).getContainerController().acceptNewAgent(next.getID().iterator().next(), newConveyor);
                    agent.start();
                }
            }
        }    
        else{
            Collection<? extends Resouce> resources = GetIndividuals.getResources();
            Iterator<? extends Resouce> ResourceIterator = resources.iterator();
            while (ResourceIterator.hasNext()) {
                Resouce next = ResourceIterator.next();
                if(next.getDeviceID().iterator().next().equalsIgnoreCase(DeviceID)){
                    ResourceAgent newResource = new ResourceAgent();
                    //newResource.setArguments(new Object[]{next});
                    newResource.setArguments(new Object[]{next, ((ConsoleAgent)myAgent).myDevices});
                    AgentController agent = ((ConsoleAgent)myAgent).getContainerController().acceptNewAgent(next.getID().iterator().next(), newResource);

                    agent.start();
                }  
            }
        }
            
    }
    
    protected void UnplugDetected(String AgentName){
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        try {
            msg.addReceiver(DFInteraction.SearchInDFByName(AgentName,(ConsoleAgent)myAgent)[0].getName());
        } catch (FIPAException ex) {
            Logger.getLogger(RequestConvBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        msg.setOntology("TakeDown");
        ((ConsoleAgent)myAgent).addBehaviour(new UnplugDetectedInit((ConsoleAgent)myAgent,msg));
    }
    
}
