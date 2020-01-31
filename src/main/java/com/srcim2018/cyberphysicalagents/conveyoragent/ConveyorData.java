package com.srcim2018.cyberphysicalagents.conveyoragent;

import java.util.HashMap;

public class ConveyorData {

    private static Long sentTime;
    private static Long receivedTime;
    private static String Cname;
    private static Long transportTime;
    public ConveyorData(Long rtime, Long stime){
        receivedTime = rtime;
        sentTime = stime;
    }



    public static Long getSTime()
    {
        return sentTime;
    }
    public static Long getRTime()
    {
        return receivedTime;
    }
    public static String getCname()
    {
        return Cname;
    }
    public static Long gettransportTime() {
        return transportTime;
    }

    public static void settransportTime(Long ttime) {
        transportTime = ttime;
    }

    public static void setSTime(Long stime)
    {
        sentTime = stime;
    }
    public  static void setRTime(Long rtime){
        receivedTime=rtime;
    }
    public static void setCname(String cname ){
        Cname=cname;
    }
    public static void cleanup(){
        Cname = null;
        sentTime = null;
        receivedTime = null;
        transportTime = null;
    }
}
