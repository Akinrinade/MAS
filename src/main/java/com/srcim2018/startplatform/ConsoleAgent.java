package com.srcim2018.startplatform;

import com.srcim2018.cyberphysicalagents.productagent.ProductAgent;
import com.srcim2018.cyberphysicalagents.resourceagent.ResourceAgent;
import com.srcim2018.devicefinder.DeviceFinder;
import com.srcim2018.generateindividuals.GetIndividuals;
import com.srcim2018.semanticmodelimpl.Conveyor;
import com.srcim2018.semanticmodelimpl.Product;
import jade.core.Agent;

import com.srcim2018.semanticmodelimpl.Resouce;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.ws4d.java.CoreFramework;
import org.ws4d.java.communication.DPWSProtocolVersion;
import org.ws4d.java.configuration.DPWSProperties;
import org.ws4d.java.util.Log;
import simple.ConveyorReads;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author André Dionisio Rocha
 */
public class ConsoleAgent extends Agent implements frameToAgentCom {

    int prodCounter = 0;
    
    ConsoleFrame myFrame;
    
    DeviceFinder myDevices;
    HashMap <String, String> HardwareDevices = new HashMap();
    ArrayList <String> PluggedDevices = new ArrayList();


    @Override
    protected void setup() {
        try {
            myFrame = new ConsoleFrame(this);

            myFrame.setVisible(true);

            //Launch Cyber-Physical Agents
            launchConveyors();
            launchResources();
            
        } catch (OWLOntologyCreationException | StaleProxyException ex) {
            Logger.getLogger(ConsoleAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log.setLogLevel(Log.DEBUG_LEVEL_NO_LOGGING);
        CoreFramework.start(null);
        DPWSProperties.getInstance().addSupportedDPWSVersion(DPWSProtocolVersion.DPWS_VERSION_2006);
        DPWSProperties.getInstance().removeSupportedDPWSVersion(DPWSProtocolVersion.DPWS_VERSION_2009);
        myDevices = new DeviceFinder();

        this.addBehaviour(new GetPluggedDevicesBehaviour(this, 1000));
    }
    

    private void launchResources() throws OWLOntologyCreationException, StaleProxyException {
        Collection<? extends Resouce> resources = GetIndividuals.getResources();
        Iterator<? extends Resouce> iterator = resources.iterator();
        while (iterator.hasNext()) {
            Resouce next = iterator.next();
            if(next.getID().iterator().next().equals("ResourceEntry") || next.getID().iterator().next().equals("ResourceExit")){
                ResourceAgent newResource = new ResourceAgent();
                newResource.setArguments(new Object[]{next});
                AgentController agent = this.getContainerController().acceptNewAgent(next.getID().iterator().next(), newResource);
                agent.start();
            }
            if(!next.getDeviceID().iterator().next().equalsIgnoreCase("")){
                //PluggedDevices.add(next.getDeviceID().iterator().next());
                HardwareDevices.put(next.getDeviceID().iterator().next(), next.getExecutes().iterator().next().getID().iterator().next());
            }
//            ResourceAgent newResource = new ResourceAgent();
//            newResource.setArguments(new Object[]{next});
//            AgentController agent = this.getContainerController().acceptNewAgent(next.getID().iterator().next(), newResource);
//            agent.start();
        }
    }

    private void launchConveyors() throws OWLOntologyCreationException, StaleProxyException {
        Collection<? extends Conveyor> conveyors = GetIndividuals.getConveyors();
        Iterator<? extends Conveyor> iterator = conveyors.iterator();
        while (iterator.hasNext()) {
            Conveyor next = iterator.next();
            if(!next.getDeviceID().iterator().next().equalsIgnoreCase("")){
                //PluggedDevices.add(next.getDeviceID().iterator().next());
                HardwareDevices.put(next.getDeviceID().iterator().next(), next.getID().iterator().next());
            }
//            ConveyorAgent newConveyor = new ConveyorAgent();
//            newConveyor.setArguments(new Object[]{next});
//            AgentController agent = this.getContainerController().acceptNewAgent(next.getID().iterator().next(), newConveyor);
//            agent.start();
        }
    }

    private void launchProduct(String prodType) throws OWLOntologyCreationException, StaleProxyException {
        Product productType = GetIndividuals.getProductType(prodType);
        ProductAgent newProduct = new ProductAgent();
        newProduct.setArguments(new Object[]{productType});
        AgentController agent = this.getContainerController().acceptNewAgent(productType.getID().iterator().next() + "_" + ++this.prodCounter, newProduct);
        agent.start();
    }

    @Override
    public void startNewProduct(String productType) {
        try {
            launchProduct(productType);
        } catch (OWLOntologyCreationException | StaleProxyException ex) {
            Logger.getLogger(ConsoleAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
