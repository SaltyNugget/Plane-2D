package com.company.Objects;

public class Customer implements java.io.Serializable{
    //values
    private String cName;
    private char cType;
    private char classType;
    private char seatType;


    //constructors
    public Customer(String cName, char cType, char classType, char seatType) {
        this.cName = cName;
        this.cType = cType;
        this.classType = classType;
        this.seatType = seatType;
    }


    //getter and setter
    public String getcName() {
        return cName;
    }
    public void setcName(String cName) {
        this.cName = cName;
    }
    public char getcType() {
        return cType;
    }
    public void setcType(char cType) {
        this.cType = cType;
    }
    public char getClassType() {
        return classType;
    }
    public void setClassType(char classType) {
        this.classType = classType;
    }
    public char getSeatType() {
        return seatType;
    }
    public void setSeatType(char seatType) {
        this.seatType = seatType;
    }
    public String[] getInformation(){
        String[] cInformation = new String[4];

        cInformation[0] = cName;

        if(cType == 'A'){cInformation[1] = "Adult";}
        else if (cType == 'C') {cInformation[1] = "Child";}

        if(classType == 'F') {cInformation[2] = "First Class";}
        else if (classType == 'B'){cInformation[2] = "Business Class";}
        else if (classType == 'E'){cInformation[2] = "Poor People Class";}

        if (seatType == 'W'){cInformation[3] = "Window Seat";}
        else if (seatType == 'M'){cInformation[3] = "Middle Seat";}
        else if (seatType == 'A'){cInformation[3] = "Aisle Seat";}

        return cInformation;



    }
}
