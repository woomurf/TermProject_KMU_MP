package com.kmu.a2018mp_termproject;

import java.util.ArrayList;

public class myGroup {
    public ArrayList<String> child;
    public String groupName;

    myGroup(String name){
        groupName = name;
        child = new ArrayList<String>();
    }

    public int getSize(){
        return child.size();
    }



}
