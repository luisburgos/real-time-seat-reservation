/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.domain;

import java.io.Serializable;

/**
 *
 * @author luisburgos
 */
public class Seat implements Serializable{
    
    private int eventId;
    private int seatNumber;
    private String state;

    public Seat(){
        
    }

    public Seat(int eventID, String state, int seatNumber) {
        this.eventId = eventID;
        this.seatNumber = seatNumber;
        this.state = state;
    }
    
    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getEventId() {
        return eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }    
    
}
