/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.cyberphysicalagents.productagent;

import com.srcim2018.semanticmodelimpl.Skill;
import com.srcim2018.utilities.DFInteraction;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joao
 */
public class NextSkillBehaviour extends OneShotBehaviour {
    
    private Skill skill;
    private int priority = 0;
    
    @Override
    public void action(){
        
        if(((ProductAgent)myAgent).myDescription.hasConsumes()){
            
            Collection <?> skillsCollection = ((ProductAgent)myAgent).myDescription.getConsumes();
            Iterator it = skillsCollection.iterator();
            
            if(it.hasNext()){
                for(int i=0; i< skillsCollection.size();i++){
                    skill = (Skill) it.next();

                    int skillpriority = skill.getPriority().iterator().next();
                    
                    if(i == 0) priority = skillpriority;
                    
                    if(skillpriority <= priority){
                        priority = skillpriority;
                        ((ProductAgent)myAgent).nextSkill = skill;
                    }
                }
            }
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            try {
                DFAgentDescription[] Receivers = DFInteraction.SearchInDFByName(((ProductAgent)myAgent).nextSkill.getID().iterator().next(),
                        ((ProductAgent)myAgent));
                if(Receivers.length != 0){
                    for(int i=0; i<Receivers.length; i++){
                        msg.addReceiver(Receivers[i].getName());
                    }
                    msg.setContent(((ProductAgent)myAgent).nextSkill.toString());
                    System.out.println(((ProductAgent)myAgent).nextSkill.toString());
                    //((ProductAgent)myAgent).myDescription.removeConsumes(((ProductAgent)myAgent).nextSkill);
                    ((ProductAgent)myAgent).addBehaviour(new CFPinitiator((ProductAgent)myAgent,msg));
                }
                else{
                    //System.out.println("No Resource with the skill " + ((ProductAgent)myAgent).nextSkill.getID().iterator().next());
                    ((ProductAgent)myAgent).addBehaviour( new NextSkillBehaviour());
                }    

            } catch (FIPAException ex) {
                Logger.getLogger(NextSkillBehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            ((ProductAgent)myAgent).nextSkill = null;
            ((ProductAgent)myAgent).doDelete();
        }
    }
    
}
