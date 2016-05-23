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
    private int[] purchasedSeats;
    
    public DetailWindowController(DetailWindow detailWindow) {
        detailWindow = detailWindow;
        try {
            SeatReservationClient.getInstance().setSeatPurchaseListener(this);
        } catch (RemoteException ex) {
            Logger.getLogger(DetailWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void requestPurchase(int[] pruchasedSeats, int currentEvent){
        this.purchasedSeats = purchasedSeats;
        try {
            SeatReservationClient.getInstance().requesrPurchase(pruchasedSeats, currentEvent);
        } catch (RemoteException ex) {
            Logger.getLogger(DetailWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void onPurchaseSuccess() {
        detailWindow.setVisible(false);
        JOptionPane.showMessageDialog(detailWindow, "La compra ha sido exitosa");
    }

    @Override
    public void onPurchaseFailure() {
       JOptionPane.showMessageDialog(detailWindow,"Ha ocurrido un error con la compra");
    }
    
    
    
}
