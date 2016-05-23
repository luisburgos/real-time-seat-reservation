package client.controllers;

import client.remote.SeatReservationClient;
import client.remote.SeatReservationClient.SeatStateChangeListener;
import client.ui.buttons.SeatButton;
import client.ui.buttons.SeatButton.OnStateChangeListener;
import client.ui.windows.ButtonSelectWindow;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luisburgos
 */
public class ButtonSelectWindowController implements SeatStateChangeListener {

    private ButtonSelectWindow selectSeatsWindow;
    private int selectedSeats[];
    private int seatCount;
    
    public ButtonSelectWindowController(ButtonSelectWindow selectSeatsWindow) {
        this.selectSeatsWindow = selectSeatsWindow;
        try {
            SeatReservationClient.getInstance().setSeatStateChangeListener(this);
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        selectedSeats = new int[5];
        seatCount = 0;
        
    }
    
    public void onSeatSelected(int selectedSeat, int eventID) throws RemoteException{
        System.out.println("SeatCount: "+ seatCount);
        if(seatCount >= 5){
            int firstSeat = selectedSeats[0];
            SeatReservationClient.getInstance().freeSeat(firstSeat, eventID);
            reorderSeats();
            selectedSeats[seatCount] = selectedSeat;
        }
        else{
            selectedSeats[seatCount] = selectedSeat;
            seatCount++;
        }
    }
    
    public boolean canSelectMoreSeats(){
        return seatCount < 5;
    }
    
    public void onSeatUnselected(){
        if(seatCount > 0){
            seatCount--;
        }
    }
    
    @Override
    public void onUpdate(HashMap<Integer, String> newStates) {        

        for (HashMap.Entry<Integer, String> entry : newStates.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            selectSeatsWindow.updateSeat(key, value);
        }
    }
    
    private void reorderSeats(){
        for(int i=0; i<4 ;i++){
            selectedSeats[i] = selectedSeats[i+1];
        }
    }
    
}
