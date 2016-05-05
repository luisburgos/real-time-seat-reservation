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
    private int[] selectedSeatNumbers;
    private int amountReserved = 0;
    private int amountSelected = 0;
    
    private SessionControl(){
        reservedSeats = new SeatButton[MAX_SIZE];
        selectedSeatNumbers = new int[MAX_SIZE];
    }
    
    public void selectSeat(int seatNumber){        
        if(amountSelected == MAX_SIZE){
            System.out.println("Reordering seats...");
            removeSeatFromSelection(0);
        }
        selectedSeatNumbers[amountSelected++] = seatNumber;
        System.out.println("Add seat " + seatNumber + " on position " +
                amountSelected);
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

    public int[] getSelectedSeats() {
        return selectedSeatNumbers;
    }

    public void setFreeSeat(Integer seatNumber) {
        for(int i = 0; i < MAX_SIZE; i++){
            if(selectedSeatNumbers[i] == seatNumber){
                System.out.println("Remove seat "+ seatNumber  + " from position " + i);
                removeSeatFromSelection(i); 
                return;
            }
        }
    }

    private void removeSeatFromSelection(int i) {
        selectedSeatNumbers[i] = 0;
        amountSelected-=1;
        changeSeatsOrder();
    }
    
}
