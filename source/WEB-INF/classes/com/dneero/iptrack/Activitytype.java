package com.dneero.iptrack;

import com.dneero.dao.Creditcard;

import java.util.TreeMap;

/**
 * User: Joe Reger Jr
 * Date: Dec 3, 2008
 * Time: 8:08:57 PM
 */
public class Activitytype {

    public static int LOGIN = 1;
    public static int PERSISTENTLOGIN = 2;
    public static int SIGNUP = 3;
    public static int CONVOJOIN = 4;

    public static String getDescription(int activitytypeid){
        if (activitytypeid==LOGIN){
            return "Login";
        } else if (activitytypeid==PERSISTENTLOGIN){
            return "PersistentLogin";
        } else if (activitytypeid==SIGNUP){
            return "SignUp";
        } else if (activitytypeid==CONVOJOIN){
            return "ConvoJoin";
        }
        return "UnknownActivityTypeid="+activitytypeid;
    }

    public static TreeMap<String, String> getTypes(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put(String.valueOf(LOGIN), "Login");
        out.put(String.valueOf(PERSISTENTLOGIN), "PersistentLogin");
        out.put(String.valueOf(SIGNUP), "SignUp");
        out.put(String.valueOf(CONVOJOIN), "ConvoJoin");
        return out;
    }

}
