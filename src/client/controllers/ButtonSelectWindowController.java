package client.controllers;

import client.remote.SeatReservationClient;
import client.remote.SeatReservationClient.SeatStateChangeListener;
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
public class ButtonSelectWindowController implements OnStateChangeListener, SeatStateChangeListener {

    private ButtonSelectWindow selectSeatsWindow;

    public ButtonSelectWindowController(ButtonSelectWindow selectSeatsWindow) {
        this.selectSeatsWindow = selectSeatsWindow;
        try {
            SeatReservationClient.getInstance().setSeatStateChangeListener(this);
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSelect(int seatNumber) {
        System.out.println("On Select");
        try {
            SeatReservationClient.getInstance().selectSeat(seatNumber + 1);
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindowController.class.getName()).log(Level.SEVERE, null, ex);
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

}
