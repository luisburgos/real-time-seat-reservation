package client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import client.ui.Buttons.SeatButton;
import client.ui.Util.ButtonStates;

/**
 *
 * @author JoseJulio
 */
public class SessionControl {
    
    private final int MAX_SIZE = 5;
    private static SessionControl self;
    
    private SeatButton[] reservedSeats;
    private int amountReserved = 0;
    
    private SessionControl(){
        reservedSeats = new SeatButton[MAX_SIZE];
    }
    
    public void reserveSeat(SeatButton seat){
        if(amountReserved >= MAX_SIZE){
            amountReserved = 0;
            reservedSeats[amountReserved].changeState(ButtonStates.FREE);
            reservedSeats[amountReserved++] = seat;
        }else{
            reservedSeats[amountReserved++] = seat;
        }
    }
    
    public void buySeats(){
        for(int i=0 ; i<amountReserved ; i++){
            reservedSeats[i].changeState(ButtonStates.SOLD);
        }
    }
    
    public static SessionControl getInstance(){
        if(self==null){
            self = new SessionControl();
        }
        return self;
    }
    
}
