package com.example.monoblos.myapplication00;

import android.util.Log;

/**
 * Created by MoNoblos on 10/20/2015.
 * Singleton class From slide OOAD Lecture6-DesignPatterns
 */
public class SingletonID {

    private static volatile SingletonID instance = null;
    private String stdId = null;
    private String id = null;
    private String firstName = null;
    private String lastName = null;
    private int status=0;//0 std , 1 teacher
    private String sessionId = null;
    private String userName = null;

    public SingletonID( ){
    }

    public static SingletonID getInstance(){
        if(instance==null) {
            synchronized (SingletonID.class) {
               if(instance==null) instance = new SingletonID();
            }
        }
        return instance;
    }

    public void setId(String id){
        this.id = id;
        Log.i("SingletonID","set id "+ this.id + " : " + id);
    }
    public String getId(){
        return id;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return this.firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return this.lastName;
    }

    public void setStdId(String stdId){
        this.stdId = stdId;
    }
    public String getStdId(){
        return this.stdId;
    }

    public void setStatus(int status){
        if(status>=1 || status<0) this.status = 1;
    }
    public int getStatus(){return this.status;}

    public void setSessionId(String sessionId){this.sessionId = sessionId;}
    public String getSessionId(){return this.sessionId;}

    public void setUserName(String userName){this.userName = userName;}
    public String getUserName(){return this.userName;}

    public void clear(){
        this.stdId = null;
        this.id = null;
        this.firstName = null;
        this.lastName = null;
        this.status = 0;
        this.sessionId = null;
    }
}
