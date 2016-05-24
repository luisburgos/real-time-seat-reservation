package client.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import client.ui.buttons.SeatButton;
import client.ui.buttons.ButtonStates;
import java.util.ArrayList;
import java.util.Arrays;

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
    
    private ArrayList<Integer> seatIndexSelection;
    
    private RemoveListener mListener;
    
    private SessionControl(){
        reservedSeats = new SeatButton[MAX_SIZE];
        selectedSeatNumbers = new int[MAX_SIZE];
        seatIndexSelection = new ArrayList<>();
    }
    
    public void selectSeat(int seatNumber){
        System.out.println("Current selection " + seatIndexSelection);        
        if(seatIndexSelection.size() == MAX_SIZE){
            System.out.println("POP OUT FIRST seat...");
            removeSeatFromSelection(0);
        }
        seatIndexSelection.add(new Integer(seatNumber));
        
        /*if(amountSelected == MAX_SIZE){
            System.out.println("Reordering seats...");
            removeSeatFromSelection(0);
        }*/
        //selectedSeatNumbers[amountSelected++] = seatNumber;
        //System.out.println("Add seat " + seatNumber + " on position " +amountSelected);
        System.out.println("Add seat " + seatNumber + " on position " + seatIndexSelection.size());
        System.out.println("New seats selected " + seatIndexSelection);        
    }      
    
    /**
     * Deprecated, not using.
     * @param seat 
     */
    public void reserveSeat(SeatButton seat){
        if(amountReserved >= MAX_SIZE){
            amountReserved = 0;
            reservedSeats[amountReserved].changeState(ButtonStates.FREE);
            reservedSeats[amountReserved++] = seat;
        }else{
            reservedSeats[amountReserved++] = seat;
        }
    }
    
    /**
     * Deprecated, not in use. Responsibility move out of this class.
     */
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
        //return selectedSeatNumbers;
        return convertIntegers(seatIndexSelection);
    }

    public void setFreeSeat(Integer seatNumber) {
        System.out.println("Setting free " + seatNumber + " from " + seatIndexSelection);
        seatIndexSelection.remove(new Integer(seatNumber));
        /*for(int i = 0; i < MAX_SIZE; i++){
            if(selectedSeatNumbers[i] == seatNumber){
                System.out.println("Remove seat "+ seatNumber  + " from position " + i);
                removeSeatFromSelection(i); 
                return;
            }
        }*/
    }

    private void removeSeatFromSelection(int i) {
        System.out.println("Removing " + i + " from " + seatIndexSelection);
        Integer removedIndex = seatIndexSelection.remove(i);
        if(mListener != null){
            mListener.onRemove(removedIndex);
        }        
        /*selectedSeatNumbers[i] = 0;
        amountSelected-=1;
        changeSeatsOrder();*/
    }

    public boolean isSeatNumberReserved(Integer seatNumber) {
        boolean isReserved = false;
        //for(int seat : selectedSeatNumbers){
        for(Integer seat : seatIndexSelection){
            System.out.println("Comparing " + seat + " and " + seatNumber);
            if(seat == seatNumber){
                isReserved = true;
                break;
            }
        }
        return isReserved;
    }

    public void clearSelectedSeats() {
        System.out.println("Clearing " + seatIndexSelection);
        seatIndexSelection.clear();               
        selectedSeatNumbers = new int[MAX_SIZE];        
    }
    
    public static int[] convertIntegers(ArrayList<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
    
    public void setRemoteListener(RemoveListener listener){
        mListener = listener;
    }
    
    public interface RemoveListener {
        void onRemove(int index);
    }
    
}
