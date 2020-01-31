/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.srcim2018.devicefinder;

/**
 *
 * @author BARATA
 */
public class Constants {
    public static final String IP = "IP";
    public static final String MAC = "MAC";
    public static final String PORT = "PORT";
    public static final String MANUFACTURER = "MANUFACTURER";    
    public static final String DEVICE_SEARCH_HOST_FILTER = "[FE80::250:C2FF:";
    
    public static final int EVENT_INPUTS_UPDATE = 2;
    public static final int EVENT_SUBSCRIBED = 1;
    public static final int EVENT_FAILED_TO_SUBSCRIBE = 0;
    
    public static int TIMER_SEARCH_REFRESH = 1*1000;
    public static  boolean filter = true;
}
