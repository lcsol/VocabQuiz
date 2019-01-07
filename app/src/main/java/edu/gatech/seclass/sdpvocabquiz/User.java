package edu.gatech.seclass.sdpvocabquiz;

import java.io.Serializable;

public class User implements Serializable{

    public static String userName;
    public static String realName;
    public static String userMajor;
    public static String userEmail;
    public static String seniorityLevel;

    public User(String userName, String realName, String userMajor, String userEmail, String seniorityLevel ) {

        this.userName = userName;
        this.realName = realName;
        this.userMajor = userMajor;
        this.userEmail = userEmail;
        this.seniorityLevel = seniorityLevel;

    }

    public String getUserName() {

        return userName;
    }


}
