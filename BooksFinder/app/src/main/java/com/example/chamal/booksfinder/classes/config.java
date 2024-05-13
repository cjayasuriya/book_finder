package com.example.chamal.booksfinder.classes;

/**
 * Created by chamal on 8/6/17.
 */

public class config {
    public static String baseURL = "http://192.168.1.30:8080/bookFinder";

    private static String configuid, configtype, configsid, configemail, configfname,configlname, configmobile;

    public static void setUid (String uid,String type, String sid, String email,String fname, String lname, String mobile){
        configuid  = uid;
        configtype = type;
        configsid = sid;
        configemail = email;
        configlname = lname;
        configfname = fname;
        configmobile = mobile;
    }

    public static String getId(){
        return configuid;
    }
    public static String getType(){
        return configtype;
    }
    public static String getSid(){
        return configsid;
    }
    public static String getEmail(){
        return configemail;
    }
    public static String getFName(){
        return configfname;
    }
    public static String getMobile(){
        return configmobile;
    }
    public static String getLName(){
        return configlname;
    }

}
