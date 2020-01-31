/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.devicefinder;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ws4d.java.authorization.AuthorizationException;
import org.ws4d.java.client.DefaultClient;
import org.ws4d.java.client.SearchManager;
import org.ws4d.java.communication.AutoBindingFactory;
import org.ws4d.java.communication.CommunicationException;
import org.ws4d.java.communication.CommunicationManager;
import org.ws4d.java.communication.CommunicationManagerRegistry;
import org.ws4d.java.communication.DPWSCommunicationManager;
import org.ws4d.java.communication.structures.CommunicationAutoBinding;
import org.ws4d.java.communication.structures.IPAutoInterfaceCommons;
import org.ws4d.java.eventing.ClientSubscription;
import org.ws4d.java.eventing.EventingException;
import org.ws4d.java.schema.Element;
import org.ws4d.java.security.CredentialInfo;
import org.ws4d.java.security.SecurityKey;
import org.ws4d.java.service.DefaultEventSource;
import org.ws4d.java.service.Device;
import org.ws4d.java.service.InvocationException;
import org.ws4d.java.service.Operation;
import org.ws4d.java.service.Service;
import org.ws4d.java.service.parameter.ParameterValue;
import org.ws4d.java.service.parameter.ParameterValueManagement;
import org.ws4d.java.service.parameter.TypedElement;
import org.ws4d.java.service.parameter.TypedModel;
import org.ws4d.java.service.reference.DeviceReference;
import org.ws4d.java.service.reference.ServiceReference;
import org.ws4d.java.structures.DataStructure;
import org.ws4d.java.structures.Iterator;
import org.ws4d.java.types.SearchParameter;
import org.ws4d.java.types.URI;

/**
 *
 * @author BARATA
 */
public class DeviceFinder extends DefaultClient {
    
    private HashMap Devices = new HashMap();
    private HashMap DevicesBuf = new HashMap();
    private ArrayList<CustomerListEventsIvoked> eventssubscriptions = new ArrayList<CustomerListEventsIvoked>();;

    private class CustomerListEventsIvoked {

        String CustomerID = new String();

        String devicekey = new String();
        String ServiceID = new String();
        String EventID = new String();
        ArrayList<String> Inputs = new ArrayList<>();
        ClientSubscription eventsubscription = null;

        private CustomerListEventsIvoked(String CustomerID, String devicekey, String ServiceID, String EventID, ArrayList<String> Inputs, ClientSubscription eventsubscription) {
            this.CustomerID = CustomerID;
            this.devicekey = devicekey;
            this.ServiceID = ServiceID;
            this.EventID = EventID;
            this.Inputs = Inputs;
            this.eventsubscription = eventsubscription;
        }
    }

    private final Timer timer = new Timer();

    public DeviceFinder() {
        SearchManager.searchDevice(null, DeviceFinder.this, null);
    }

    public DeviceFinder(String libraryName, Object[] parameters) {
        try {
            //String className = libraryName;
            Class cls = Class.forName(libraryName);
            Object instance;
            instance = cls.newInstance();
            
            SearchManager.searchDevice(null, DeviceFinder.this, null);
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap getConfigs(String devicekey) {

        HashMap conf = new HashMap();
        Device dev = (Device) Devices.get(devicekey);

        if (dev != null) {
            Iterator AddrIt = dev.getTransportXAddressInfos();
            while (AddrIt.hasNext()) {
                org.ws4d.java.types.XAddressInfo meAddr = (org.ws4d.java.types.XAddressInfo) AddrIt.next();
                if (meAddr.getHost().startsWith("[")) {
                    conf.put(Constants.MAC, meAddr.getHost());
                } else {
                    conf.put(Constants.IP, meAddr.getHost());
                }
                conf.put(Constants.PORT, meAddr.getPort());
            }
            conf.put(Constants.MANUFACTURER, dev.getManufacturer("en"));
            return conf;
        }
        return null;
    }

    public ArrayList<String> getDevicesIDs() {

        ArrayList<String> id = new ArrayList<>();
        id.addAll(Devices.keySet());
        return id;
    }
    
    public ArrayList<String> getServicesfromDevice(String devicekey) {

        ArrayList<String> services = new ArrayList<>();
        Device dev = (Device) Devices.get(devicekey);

        if (dev != null) {
            Iterator ServIt = dev.getServiceReferences(SecurityKey.EMPTY_KEY);
            while (ServIt.hasNext()) {
                org.ws4d.java.dispatch.DefaultServiceReference meServ = (org.ws4d.java.dispatch.DefaultServiceReference) ServIt.next();
                services.add(meServ.getServiceId().toString());
            }
            return services;
        }
        return null;
    }
    
    public ArrayList<String> getOperationsfromDeviceService(String devicekey, String ServiceID) {

        ArrayList<String> operations = new ArrayList<>();
        Device dev = (Device) Devices.get(devicekey);

        if (dev != null) {
            Iterator servRefs = dev.getServiceReferences(SecurityKey.EMPTY_KEY);
            while (servRefs.hasNext()) {
                ServiceReference servRef = (ServiceReference) servRefs.next();
                if (servRef.getServiceId().toString().equals(ServiceID)) {
                    try {
                        Service serv = servRef.getService();
                        Iterator operationsIt = serv.getAllOperations();
                        while (operationsIt.hasNext()) {
                            Operation operation = (Operation) operationsIt.next();
                            operations.add(operation.getName());
                        }
                        return operations;
                    } catch (CommunicationException ex) {
                        Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }
        
    public ArrayList<String> getEnventsfromDeviceService(String devicekey, String ServiceID) {

        ArrayList<String> events = new ArrayList<>();
        Device dev = (Device) Devices.get(devicekey);

        if (dev != null) {
            Iterator servRefs = dev.getServiceReferences(SecurityKey.EMPTY_KEY);
            while (servRefs.hasNext()) {
                ServiceReference servRef = (ServiceReference) servRefs.next();
                if (servRef.getServiceId().toString().equals(ServiceID)) {
                    try {
                        Service serv = servRef.getService();
                        Iterator eventsIt = serv.getAllEventSources();
                        while (eventsIt.hasNext()) {
                            DefaultEventSource event = (DefaultEventSource) eventsIt.next();
                            events.add(event.getName());
                        }
                        return events;
                    } catch (CommunicationException ex) {
                        Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }
    
    public ArrayList<String> getInputs(String devicekey, String ServiceID, String Operation_Event) {

        DefaultEventSource event = getEvent(devicekey, ServiceID, Operation_Event);
        if(event != null){
            return getArrayFromElement(event.getInput());
        }else{
            Operation operation = getOperation(devicekey, ServiceID, Operation_Event);
            if(operation != null){
                return getArrayFromElement(operation.getInput());
            }
        }
        return null;
    }
    
    public ArrayList<String> getOutputs(String devicekey, String ServiceID, String Operation_Event) {

        DefaultEventSource event = getEvent(devicekey, ServiceID, Operation_Event);
        if(event != null){
            return getArrayFromElement(event.getOutput());
        }else{
            Operation operation = getOperation(devicekey, ServiceID, Operation_Event);
            if(operation != null){
                return getArrayFromElement(operation.getOutput());
            }
        }
        return null;
    }
        
    public ArrayList<String> callOperation(String devicekey, String ServiceID, String Operation, ArrayList<String> Inputs) {

        Operation operation = getOperation(devicekey, ServiceID, Operation);
        if(operation != null){
            try {
                ParameterValue paramValuein = BuildInputMessage(operation.getInput(), Inputs);
                ParameterValue out = operation.invoke(paramValuein, CredentialInfo.EMPTY_CREDENTIAL_INFO);
                return RetrieveOutputMessage(getOutputs(devicekey, ServiceID, Operation), out);
            } catch (InvocationException | CommunicationException | AuthorizationException ex ) {
                Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }       
    
    public int EventSubscribe(String CustomerID, String devicekey, String ServiceID, String EventID, ArrayList<String> Inputs) {
        
        DefaultEventSource event = getEvent(devicekey, ServiceID, EventID);
        
        if(event != null){
            try {
                for (CustomerListEventsIvoked element : eventssubscriptions) {
                    if (element.CustomerID.equals(CustomerID)
                            && element.devicekey.equals(devicekey)
                            && element.ServiceID.equals(ServiceID)
                            && element.EventID.equals(EventID)) {
                        eventssubscriptions.remove(element);
                        element.Inputs = Inputs;
                        eventssubscriptions.add(element);
                        return Constants.EVENT_INPUTS_UPDATE;
                    }
                }
                
                // communication manager
                CommunicationManager manager = CommunicationManagerRegistry.getCommunicationManager(DPWSCommunicationManager.COMMUNICATION_MANAGER_ID);
                // autobinding factory
                AutoBindingFactory abf = manager.getAutoBindingFactory();
                // add binding
                DataStructure bindings = new org.ws4d.java.structures.ArrayList();
                CommunicationAutoBinding communicationBinding = abf.createCommunicationAutobinding(new String[]{"eth3", "lo"}, new String[]{IPAutoInterfaceCommons.IPADDRESS_FAMILY_IPV4, IPAutoInterfaceCommons.IPADDRESS_FAMILY_IPV6}, false, false, "", 0);
                bindings.add(communicationBinding);
                
                eventssubscriptions.add(new CustomerListEventsIvoked(CustomerID, devicekey, ServiceID, EventID, Inputs, event.subscribe(this, 0, bindings, CredentialInfo.EMPTY_CREDENTIAL_INFO)));                
                return Constants.EVENT_SUBSCRIBED;
            } catch (EventingException | IOException | CommunicationException ex) {
                Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Constants.EVENT_FAILED_TO_SUBSCRIBE;
    }
    
    public boolean EventUnsubscribe(String CustomerID, String devicekey, String ServiceID, String EventID) {

        for (CustomerListEventsIvoked element : eventssubscriptions) {

            if (element.CustomerID.equals(CustomerID)
                    && element.devicekey.equals(devicekey)
                    && element.ServiceID.equals(ServiceID)
                    && element.EventID.equals(EventID)) {
                try {
                    element.eventsubscription.unsubscribe();
                    eventssubscriptions.remove(element);
                    return true;
                } catch (EventingException | IOException | CommunicationException ex) {
                    Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    
    public ParameterValue BuildInputMessage(Element input, ArrayList<String> Inputs) {

        if (input != null) {
            int index = 0;
            TypedElement inputTypedElement = TypedElement.create(input);
            if (inputTypedElement.getModels().hasNext()) {
                Iterator inpIt = inputTypedElement.getModels();
                while (inpIt.hasNext()) {
                    TypedModel model = (TypedModel) inpIt.next();
                    Iterator contIt = model.content();
                    while (contIt.hasNext()) {
                        TypedElement element = (TypedElement) contIt.next();
                        ParameterValue inup = element.getValue();
                        ParameterValueManagement.setString(inup, element.getDisplayName(), Inputs.get(index++));
                        element.setValue(inup);
                    }
                }
            } else {
                ParameterValue inup = inputTypedElement.getValue();
                ParameterValueManagement.setString(inup, inputTypedElement.getDisplayName(), Inputs.get(index++));
                inputTypedElement.setValue(inup);
            }
            return inputTypedElement.getValue();
        }
        return new ParameterValue();
    }
    
    public ArrayList<String> RetrieveOutputMessage(ArrayList<String> OutputsNames, ParameterValue msg) {

        ArrayList<String> Outputs = new ArrayList<>();

        if (!OutputsNames.isEmpty()) {
            for (String Names : OutputsNames) {
                Outputs.add(ParameterValueManagement.getString(msg, Names));
            }
        }
        return Outputs;
    }
    
    
    public DefaultEventSource getEvent(String devicekey, String ServiceID, String EventID){
        
        Device dev = (Device) Devices.get(devicekey);

        if (dev != null) {
            Iterator servRefs = dev.getServiceReferences(SecurityKey.EMPTY_KEY);
            while (servRefs.hasNext()) {
                ServiceReference servRef = (ServiceReference) servRefs.next();
                if (servRef.getServiceId().toString().equals(ServiceID)) {
                    try {
                        Service serv = servRef.getService();
                        Iterator eventsIt = serv.getAllEventSources();
                        while (eventsIt.hasNext()) {
                            DefaultEventSource event = (DefaultEventSource) eventsIt.next();
                            if (event.getName().equals(EventID)) {
                                return event;
                            }
                        }                        
                    } catch (CommunicationException ex) {
                        Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }
    
    public Operation getOperation(String devicekey, String ServiceID, String Operation){
        
        Device dev = (Device) Devices.get(devicekey);

        if (dev != null) {
            Iterator servRefs = dev.getServiceReferences(SecurityKey.EMPTY_KEY);
            while (servRefs.hasNext()) {
                ServiceReference servRef = (ServiceReference) servRefs.next();
                if (servRef.getServiceId().toString().equals(ServiceID)) {
                    try {
                        Service serv = servRef.getService();
                        Iterator operations = serv.getAllOperations();
                        while (operations.hasNext()) {
                            Operation operation = (Operation) operations.next();
                            if (operation.getName().equals(Operation)) {
                                return operation;
                            }
                        }
                    } catch (CommunicationException | AuthorizationException ex) {
                        Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }
    
    public ArrayList<String> getArrayFromElement(Element element){
                
        ArrayList<String> array = new ArrayList<>();
        
        if (element != null) {
            TypedElement typedElement = TypedElement.create(element);
            if (typedElement.getModels().hasNext()) {
                Iterator inpIt = typedElement.getModels();
                while (inpIt.hasNext()) {
                    TypedModel model = (TypedModel) inpIt.next();
                    Iterator contIt = model.content();
                    while (contIt.hasNext()) {
                        TypedElement element2 = (TypedElement) contIt.next();
                        array.add(element2.getDisplayName());   
                    }
                }
            } else {
                array.add(typedElement.getDisplayName());
            }                
        }
        return array;
    }
    
     
    public ArrayList<String> getOperationInputs(String devicekey, String ServiceID, String Operation) {

        Operation operation = getOperation(devicekey, ServiceID, Operation);
        
        if(operation != null){          
            return getArrayFromElement(operation.getInput());
        }
        return null;
    }

    public ArrayList<String> getOperationOutputs(String devicekey, String ServiceID, String Operation) {

        Operation operation = getOperation(devicekey, ServiceID, Operation);
        
        if(operation != null){          
            return getArrayFromElement(operation.getOutput());
        }
        return null;
    }
        
    public ArrayList<String> getEventInputs(String devicekey, String ServiceID, String Event) {        
        
        DefaultEventSource event = getEvent(devicekey, ServiceID, Event);
        
        if(event != null){          
            return getArrayFromElement(event.getInput());
        }
        return null;
    }

    public ArrayList<String> getEventOutputs(String devicekey, String ServiceID, String Event) {

        DefaultEventSource event = getEvent(devicekey, ServiceID, Event);
        
        if(event != null){  
            return getArrayFromElement(event.getOutput());
        }
        return null;
    }
    
   
    /***********************************************************
     *                      TO Improve
     ***********************************************************/
   
    /**********************************************************
                                TO TEST
    ***********************************************************/
    
    /**********************************************************
                            Being Tested
    ***********************************************************/
    
    /**
     * *********************************************************
     ***********************************************************
     */
    
    @Override
    public void deviceFound(DeviceReference devRef, SearchParameter search, String comManId) {

        if (!DevicesBuf.containsKey(devRef.getEndpointReference())) {
            try {
                Iterator AddrIt = devRef.getDevice().getTransportXAddressInfos();
                while (AddrIt.hasNext()) {
                    org.ws4d.java.types.XAddressInfo meAddr = (org.ws4d.java.types.XAddressInfo) AddrIt.next();

                    if (Constants.filter) {
                        if (meAddr.getHost().startsWith(Constants.DEVICE_SEARCH_HOST_FILTER)) {
                            DevicesBuf.put(devRef.getEndpointReference().toString(), devRef.getDevice());
                            System.out.println("Got it " + DevicesBuf.size() + "     : " + devRef);
                            break;
                        }
                    } else {
                        DevicesBuf.put(devRef.getEndpointReference().toString(), devRef.getDevice());
                        System.out.println("Got it " + DevicesBuf.size() + "     : " + devRef);
                        break;
                    }
                }
            } catch (CommunicationException ex) {
//                Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
                devRef.removeListener(this);
            }
            return;
        }
        devRef.removeListener(this);
    }

    @Override
    public void finishedSearching(int i, boolean bln, SearchParameter sp) {

        //System.out.println("*******************  Search Finished Begin  *******************");

        Devices = new HashMap();
        Devices.putAll(DevicesBuf);
        DevicesBuf = new HashMap();

        
        
        //printAll();
        //System.out.println("*******************  Search Finished Done  *******************");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer Reached");
                SearchManager.searchDevice(null, DeviceFinder.this, null);
            }
        }, Constants.TIMER_SEARCH_REFRESH);
    }
    
    @Override
    public ParameterValue eventReceived(ClientSubscription cs, URI uri, ParameterValue pv) {

        System.out.println("EVENT RECEIVED");

        for (CustomerListEventsIvoked element : eventssubscriptions) {
            if (element.eventsubscription.equals(cs)) {
                
                ArrayList<String> response = RetrieveOutputMessage(getOutputs(element.devicekey, element.ServiceID, element.EventID), pv);
                
                printEventReceived(element, pv, response);
                
                //callback
                //myCallback.EventReceived(response);
                
                DefaultEventSource event = getEvent(element.devicekey, element.ServiceID, element.EventID);                
                if(event != null){
                    if (event.getInput() != null) {
                        ParameterValue pvResponse = BuildInputMessage(event.getInput(), element.Inputs);
                        return pvResponse;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * *********************************************************************
     ***********************************************************************
     */
    
    private void printAll() {

        System.out.println("\nDevices Show: " + Devices.size());

        String MAC = new String();
        String IP = new String();

        Set set = Devices.entrySet();    // Get an iterator
        java.util.Iterator iter = (java.util.Iterator) set.iterator();
        while (iter.hasNext()) {
            Map.Entry me = (Map.Entry) iter.next();
            Device dev = (Device) Devices.get(me.getKey());

            Iterator AddrIt = dev.getTransportXAddressInfos();
            while (AddrIt.hasNext()) {
                org.ws4d.java.types.XAddressInfo meAddr = (org.ws4d.java.types.XAddressInfo) AddrIt.next();
                if (meAddr.getHost().startsWith("[")) {
                    MAC = meAddr.getHost();
                } else {
                    IP = meAddr.getHost();
                }
            }
            System.out.println(" -MAC: " + MAC + "; IP: " + IP + "; Name: " + dev.getFriendlyName("en"));

//****************************************************************************************
            Iterator servRefs = dev.getServiceReferences(SecurityKey.EMPTY_KEY);
            while (servRefs.hasNext()) {
                ServiceReference servRef = (ServiceReference) servRefs.next();
                try {
                    Service serv = servRef.getService();
                    System.out.println("    Service ID:  " + serv.getServiceId());

//****************************************************************************************  
                    Iterator operations = serv.getAllOperations();
                    while (operations.hasNext()) {
                        Operation op = (Operation) operations.next();
                        System.out.println("       Operation: " + op.getName());

//****************************************************************************************                        
                        Element input = op.getInput();
                        if (input != null) {
                            TypedElement inputTypedElement = TypedElement.create(input);
                            System.out.println("            InputName: " + inputTypedElement.getDisplayName() + " Type: " + inputTypedElement.getDisplayType());
                            Iterator inpIt = inputTypedElement.getModels();
                            while (inpIt.hasNext()) {
                                TypedModel model = (TypedModel) inpIt.next();
                                System.out.println("                Model: " + model.getDisplayName());
                                Iterator contIt = model.content();
                                while (contIt.hasNext()) {
                                    TypedElement element = (TypedElement) contIt.next();
                                    System.out.println("                    InputName: " + element.getDisplayName() + " Type: " + element.getDisplayType());
                                }
                            }
                        }

                        Element output = op.getOutput();
                        if (output != null) {
                            TypedElement outputTypedElement = TypedElement.create(output);
                            System.out.println("            OutputName: " + outputTypedElement.getDisplayName() + " Type: " + outputTypedElement.getDisplayType());
                            Iterator outIt = outputTypedElement.getModels();
                            while (outIt.hasNext()) {
                                TypedModel model = (TypedModel) outIt.next();
                                System.out.println("                Model: " + model.getDisplayName());
                                Iterator contIt = model.content();
                                while (contIt.hasNext()) {
                                    TypedElement element = (TypedElement) contIt.next();
                                    System.out.println("                    OutputName: " + element.getDisplayName() + " Type: " + element.getDisplayType());
                                }
                            }
                        }
                    }
                    Iterator eventsIt = serv.getAllEventSources();
                    while (eventsIt.hasNext()) {
                        DefaultEventSource ev = (DefaultEventSource) eventsIt.next();
                        System.out.println("       Event: " + ev.getName());

                        Element input = ev.getInput();
                        if (input != null) {
                            TypedElement inputTypedElement = TypedElement.create(input);
                            System.out.println("            InputName: " + inputTypedElement.getDisplayName() + " Type: " + inputTypedElement.getDisplayType());
                            Iterator inpIt = inputTypedElement.getModels();
                            while (inpIt.hasNext()) {
                                TypedModel model = (TypedModel) inpIt.next();
                                System.out.println("                Model: " + model.getDisplayName());
                                Iterator contIt = model.content();
                                while (contIt.hasNext()) {
                                    TypedElement element = (TypedElement) contIt.next();
                                    System.out.println("                    InputName: " + element.getDisplayName() + " Type: " + element.getDisplayType());
                                }
                            }
                        }

                        Element output = ev.getOutput();
                        if (output != null) {
                            TypedElement outputTypedElement = TypedElement.create(output);
                            System.out.println("            OutputName: " + outputTypedElement.getDisplayName() + " Type: " + outputTypedElement.getDisplayType());
                            Iterator outIt = outputTypedElement.getModels();
                            while (outIt.hasNext()) {
                                TypedModel model = (TypedModel) outIt.next();
                                System.out.println("                Model: " + model.getDisplayName());
                                Iterator contIt = model.content();
                                while (contIt.hasNext()) {
                                    TypedElement element = (TypedElement) contIt.next();
                                    System.out.println("                    OutputName: " + element.getDisplayName() + " Type: " + element.getDisplayType());
                                }
                            }
                        }
                    }
                } catch (CommunicationException ex) {
                    Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Devices Done: " + Devices.size());
    }

    private void printDevices() {

        System.out.println("\nDevices Show: " + Devices.size());

        String MAC = new String();
        String IP = new String();

        Set set = Devices.entrySet();    // Get an iterator
        java.util.Iterator iter = (java.util.Iterator) set.iterator();
        while (iter.hasNext()) {
            Map.Entry me = (Map.Entry) iter.next();
            Device dev = (Device) Devices.get(me.getKey());

            Iterator AddrIt = dev.getTransportXAddressInfos();
            while (AddrIt.hasNext()) {
                org.ws4d.java.types.XAddressInfo meAddr = (org.ws4d.java.types.XAddressInfo) AddrIt.next();
                if (meAddr.getHost().startsWith("[")) {
                    MAC = meAddr.getHost();
                } else {
                    IP = meAddr.getHost();
                }
            }
            System.out.println(" -MAC: " + MAC + "; IP: " + IP + "; Name: " + dev.getFriendlyName("en"));
        }
        System.out.println("Devices Done: " + Devices.size());
    }

    private void printDeviceServices(Device dev) {

        Iterator servRefs = dev.getServiceReferences(SecurityKey.EMPTY_KEY);
        while (servRefs.hasNext()) {
            ServiceReference servRef = (ServiceReference) servRefs.next();
            try {
                Service serv = servRef.getService();
                System.out.println("    Service ID:  " + serv.getServiceId());
            } catch (CommunicationException ex) {
                Logger.getLogger(DeviceFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void printServiceOperations(Service serv) {

        Iterator operations = serv.getAllOperations();
        while (operations.hasNext()) {
            Operation op = (Operation) operations.next();
            System.out.println("       Operation: " + op.getName());
        }
    }

    private void printOperationInputs(Operation op) {

        Element input = op.getInput();
        if (input != null) {
            TypedElement inputTypedElement = TypedElement.create(input);
            System.out.println("            InputName: " + inputTypedElement.getDisplayName() + " Type: " + inputTypedElement.getDisplayType());
            Iterator inpIt = inputTypedElement.getModels();
            while (inpIt.hasNext()) {
                TypedModel model = (TypedModel) inpIt.next();
                System.out.println("                Model: " + model.getDisplayName());
                Iterator contIt = model.content();
                while (contIt.hasNext()) {
                    TypedElement element = (TypedElement) contIt.next();
                    System.out.println("                    InputName: " + element.getDisplayName() + " Type: " + element.getDisplayType());
                }
            }
        }
    }

    private void printOperationOutputs(Operation op) {

        Element output = op.getOutput();
        if (output != null) {
            TypedElement outputTypedElement = TypedElement.create(output);
            System.out.println("            OutputName: " + outputTypedElement.getDisplayName() + " Type: " + outputTypedElement.getDisplayType());
            Iterator inpIt = outputTypedElement.getModels();
            while (inpIt.hasNext()) {
                TypedModel model = (TypedModel) inpIt.next();
                System.out.println("                Model: " + model.getDisplayName());
                Iterator contIt = model.content();
                while (contIt.hasNext()) {
                    TypedElement element = (TypedElement) contIt.next();
                    System.out.println("                    OutputName: " + element.getDisplayName() + " Type: " + element.getDisplayType());
                }
            }
        }
    }

    private void printEventReceived(CustomerListEventsIvoked element, ParameterValue pv, ArrayList<String> response){
        
        System.out.println("    For: " + element.CustomerID);
        System.out.println("    From:");
        System.out.println("        Device: " + element.devicekey);
        System.out.println("        Service: " + element.ServiceID);
        System.out.println("        Event: " + element.EventID);

        System.out.println("        Response: " + pv);
        
        if (response.isEmpty()) {
            System.out.println("            Empty");
        } else {
            for (String value : response) {
                System.out.println("            Value: " + value);
            }
        }
    }
    
}