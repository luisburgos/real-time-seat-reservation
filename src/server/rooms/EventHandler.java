/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.rooms;

import client.remote.ClientRemote;
import client.ui.buttons.ButtonStates;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.control.SeatsThreadPool;
import server.control.tasks.SeatTask;
import server.control.tasks.SelectedSeatTask;
import server.data.SeatsRepository;
import server.domain.Event;
import server.domain.Seat;
import server.remote.Server;
import static server.remote.Server.clients;
import server.remote.ServerRemote;
import server.utils.ClientNotifier;

/**
 *
 * @author User
 */

public class EventHandler {
    
    private static final long serialVersionUID = 11L;
    private int event_id;
    public static Vector<ClientRemote> clients;
    
    private ClientNotifier mClientNotifier;
    private SeatsThreadPool mPool;
    
    public EventHandler(int event_id) throws RemoteException {
        super();
        this.event_id = event_id;
        this.clients = new Vector<>();
        mPool = new SeatsThreadPool(50, 50);
        //UnicastRemoteObject.exportObject(this, 0);
    }

    public void selectSeat(int seatNumber) {
        int eventID = 1;              
        System.out.println("Selecting seat " + seatNumber + " from event " + eventID);
        Seat seat = new Seat(eventID, ButtonStates.SELECTED, seatNumber);
        try {
            new SeatsRepository().update(seat);
            mPool.execute(new SelectedSeatTask(eventID, seatNumber, new SeatTask.OnWaitingTimeFinished() {
                @Override
                public void onSuccessfullyFinish(int eventID, int seatIndex) {
                    //mClientNotifier.notifyAll(eventID, seatIndex);
                    System.out.println("Free seat " + seatNumber + " from event " + eventID);
                    seat.setState(ButtonStates.FREE);
                    new SeatsRepository().update(seat);                    
                    notifyClients(seatIndex, "FREE");
                }
            }));
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void notifyClients(int seatIndex, String newState){
        
        HashMap newStates = new HashMap<>();
        newStates.put(seatIndex, newState);
        
        for(ClientRemote client : clients){
            try {
                client.updateSeatsState(newStates);
            } catch (RemoteException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void registerClient(ClientRemote client) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void freeSeat(int seatNumber) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void selectSeat(int seatNumber, int event_id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void reserveSeats(int[] seatNumbers) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void cancelSeatsReservation() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void buySeats(int[] seatNumbers) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Event getEvent(int eventID) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
