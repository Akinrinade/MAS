/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.conveyoragent;

import com.srcim2018.cyberphysicalagents.resourceagent.ResourceAgent;
import com.srcim2018.devicefinder.DeviceFinder;
import com.srcim2018.semanticmodelimpl.Conveyor;
import com.srcim2018.utilities.DFInteraction;

import jade.core.Agent;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import simple.ConveyorReads;
import simple.TransportTime;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andre Rocha
 */
public class ConveyorAgent extends Agent{
    
    private Conveyor myDescription;
    protected ArrayList conveyorStrings = new ArrayList();
    protected Boolean Occupied; 
    protected String nextConv;
    protected ACLMessage msgToPreviousConv;
    protected String DeviceID;
    protected String GetProduct= "GetProduct";
    protected String DeliverProduct = "DeliverProduct";
    protected ArrayList<String> myInputs = new ArrayList<String>();
    protected DeviceFinder HardwareDevices;


    public  Long sentTime;
    public Long receivedTime;
    public  String Cname;
    public Long transportTime;

    public ConveyorReads sent_read;
    private ConveyorReads receive_read;
    private TransportTime myTransportTime = new TransportTime();



    @Override
    protected void setup() {
        Object[] arguments = this.getArguments();
        this.myDescription = (Conveyor) arguments[0];
        this.HardwareDevices = (DeviceFinder) arguments[1];
        
        DeviceID = this.myDescription.getDeviceID().iterator().next();
        
        myInputs.add("");
        
        System.out.println("Conveyor Deployed: " + myDescription.getID().iterator().next());
        System.out.println(this.getLocalName() + " - " + DeviceID);
        
        conveyorStrings.add(getLocalName());
        
        Occupied = false;
        
        nextConv = this.myDescription.getNextConveyor().iterator().next();
        
        
         try {
            //Register in DF
            DFInteraction.RegisterInDF(this, conveyorStrings, "ConveyorAgent");
        } catch (FIPAException ex) {
            Logger.getLogger(ConveyorAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        this.addBehaviour(new FIPAConvresponder(this,MessageTemplate.MatchPerformative(ACLMessage.REQUEST)));
    }

    public void setmyTrransportTime(String conveyorname, Double Ttime, Double Time){
        myTransportTime.setConveyor_name(conveyorname);
        myTransportTime.setT_time(Ttime);
        myTransportTime.setTime(Time);

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
