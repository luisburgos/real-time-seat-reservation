package client.controllers;

import client.controllers.SessionControl.RemoveListener;
import client.remote.SeatReservationClient;
import client.remote.SeatReservationClient.SeatStateChangeListener;
import client.ui.buttons.SeatButton;
import client.ui.buttons.SeatButton.OnStateChangeListener;
import client.ui.windows.ButtonSelectWindow;
import client.ui.windows.DetailWindow;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.domain.Event;

/**
 *
 * @author luisburgos
 */
public class ButtonSelectWindowController implements SeatStateChangeListener, RemoveListener {

    private ButtonSelectWindow mView;
    private int selectedSeats[];
    private int seatCount;   
    
    private final Event mEvent;
    
    public ButtonSelectWindowController(ButtonSelectWindow selectSeatsWindow, Event event) {
        mEvent = event;
        this.mView = selectSeatsWindow;
        try {
            SessionControl.getInstance().setRemoteListener(this);
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
            mView.updateSeat(key, value);
        }
    }
    
    private void reorderSeats(){
        for(int i=0; i<4 ;i++){
            selectedSeats[i] = selectedSeats[i+1];
        }
    }

    public void joinEventRoom(Event event) {
        try {            
            SeatReservationClient.getInstance().joinEventRoom(event.getId());
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doReservation(Event event) {
                
        try {           
            int[] seats = SessionControl.getInstance().getSelectedSeats();            
            SeatReservationClient
                    .getInstance()
                    .reserveSeats(seats, event.getId());
            mView.openDetailReservationWindow(seats, event.getId());
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
               
    }

    public void cancelSelectedSeats(Event event) {
        int[] selectedSeats = SessionControl.getInstance().getSelectedSeats();
        try {
            SeatReservationClient
                    .getInstance()
                    .cancelSelections(selectedSeats, event.getId());
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void leaveRoom(Event event) {
        try {            
            SeatReservationClient.getInstance().leaveEventRoom(event.getId());
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onRemove(int index) {
        try {
            System.out.println("On Remove fromo Controller removing " + index);
            SeatReservationClient
                    .getInstance()
                    .cancelSelections(new int[]{index}, mEvent.getId());
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
