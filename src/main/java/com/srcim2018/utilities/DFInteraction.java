/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.utilities;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.ArrayList;
/**
 *
 * @author André Dionisio Rocha
 */
public class DFInteraction {

    //Regista o serviço com o nome name e do tipo type relativo ao myAgent
    public static void RegisterInDF(Agent myAgent, ArrayList name, String type) throws FIPAException {

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());
        
        for(int i=0; i < name.size(); i++){
            ServiceDescription sd = new ServiceDescription();
            sd.setType(type);
            sd.setName(name.get(i).toString());
            dfd.addServices(sd);
        }

        DFService.register(myAgent, dfd);

    }
    
    //Desregista o serviço relativo ao myAgent
    public static void DeregisterFromDF(Agent myAgent) throws FIPAException{
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());
        
        DFService.deregister(myAgent, dfd);
    }

    //Procura no DF por serviços do nome name
    //Retorno: Vector com os registos encontrados
    public static DFAgentDescription[] SearchInDFByName(String name, Agent myAgent) throws FIPAException {

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setName(name);
        dfd.addServices(sd);

        DFAgentDescription[] resultado = DFService.search(myAgent, dfd);

        return resultado;
    }
    
    //Procura no DF por serviços do tipo type
    //Retorno: Vector com os registos encontrados
    public static DFAgentDescription[] SearchInDFByType(String type, Agent myAgent) throws FIPAException {

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        dfd.addServices(sd);

        DFAgentDescription[] resultado = DFService.search(myAgent, dfd);

        return resultado;
    }
    
}
    
