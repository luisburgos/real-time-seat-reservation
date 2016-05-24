/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;

import client.remote.SeatReservationClient;
import client.remote.SeatReservationClient.SeatPurchaseListener;
import client.ui.buttons.ButtonStates;
import client.ui.windows.DetailWindow;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author JoseJulio
 */
public class DetailWindowController implements SeatPurchaseListener {
    
    private DetailWindow detailWindow;
    private int[] reservedSeats;
    private int sizeOfReservation = 0;
    
    public DetailWindowController(DetailWindow detailWindow) {
        this.detailWindow = detailWindow;
        try {
            SeatReservationClient.getInstance().setSeatPurchaseListener(this);
        } catch (RemoteException ex) {
            Logger.getLogger(DetailWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void requestPurchase(int[] reservedSeats, int currentEvent){
        this.reservedSeats = reservedSeats;
        sizeOfReservation = this.reservedSeats.length;
        try {
            SeatReservationClient.getInstance().requesrPurchase(reservedSeats, currentEvent);
        } catch (RemoteException ex) {
            Logger.getLogger(DetailWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void onPurchaseSuccess() {
        if(sizeOfReservation == 1){
            SessionControl.getInstance().clearSelectedSeats();
            detailWindow.closeWithSuccess();        
        } else {
            sizeOfReservation--;
        }                
    }

    @Override
    public void onPurchaseFailure() {
        if(sizeOfReservation == 1){
            SessionControl.getInstance().clearSelectedSeats();        
            detailWindow.closeWithFailure();
        } else {
            sizeOfReservation--;
        }            
    }

    public void cancelReservedSeats(int[] reservedSeats, int eventId) {        
        this.reservedSeats = reservedSeats;
        try {
            SessionControl.getInstance().clearSelectedSeats();
            SeatReservationClient
                    .getInstance()
                    .cancelReservations(reservedSeats, eventId);           
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
