package client.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import client.ui.buttons.SeatButton;
import client.ui.buttons.ButtonStates;

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
    
    public void selectSeat(SeatButton seat){
        if(amountReserved == MAX_SIZE){
            reservedSeats[0].changeState(ButtonStates.FREE);
            amountReserved-=1;
            changeSeatsOrder();
        }
        reservedSeats[amountReserved++] = seat;       
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

    private void changeSeatsOrder() {
        for(int i = 0; i < amountReserved; i++){
            SeatButton current = reservedSeats[i+1];
            reservedSeats[i] = current;
        }
    }

    public SeatButton[] getSelectedSeats() {
        return reservedSeats;
    }
    
}
