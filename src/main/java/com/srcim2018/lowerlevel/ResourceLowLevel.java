/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srcim2018.lowerlevel;

/**
 *
 * @author Andre Rocha
 */
public class ResourceLowLevel {
    
    public static boolean executeSkill(String ResourceName, String SkillName, String prodID) throws InterruptedException {
            switch (SkillName) {
                case "Entry":
                    System.out.println(ResourceName + " is waiting for the human operator");
                    boolean executedEntry = executeEntryConsole();
                    return executedEntry;
                case "Exit":
                    System.out.println(ResourceName + " is waiting for the human operator");
                    boolean executedExit = executeExitConsole();
                    return executedExit;
                default: return false;
            }
    }

    private static boolean executeEntryConsole() throws InterruptedException {
        /*boolean skillExecuted = false;
        EntryForm myEntryForm = new EntryForm();
        myEntryForm.setVisible(true);
        while (!myEntryForm.executed()) {
            skillExecuted = myEntryForm.executed();
            Thread.sleep(100);
        }
        return skillExecuted;*/
        return true;
    }

    private static boolean executeExitConsole() throws InterruptedException {
        boolean skillExecuted = false;
        ExitForm myExitForm = new ExitForm();
        myExitForm.setVisible(true);
        while (!myExitForm.executed()) {
            skillExecuted = myExitForm.executed();
            Thread.sleep(100);
        }

        return skillExecuted;
    }

}
