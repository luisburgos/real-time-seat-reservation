package server.remote;

import server.control.tasks.SelectedSeatTask;
import server.control.SeatsThreadPool;
import client.remote.ClientRemote;
import server.domain.Event;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.utils.ClientNotifier;
import server.data.EventsRepository;
import server.data.EventsRepositoryEndPoint;
import server.control.tasks.SeatTask;

public class Server implements ServerRemote {
    
    public static Vector<ClientRemote> clients;
    
    private ClientNotifier mClientNotifier;
    private SeatsThreadPool mPool;

    public Server() throws RemoteException {
        this.clients = new Vector<>();
        mPool = new SeatsThreadPool(50, 50);
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void registerClient(ClientRemote client) throws RemoteException {
        clients.add(client);
        System.out.println("Register new client");
        System.out.println(client);
    }

    @Override
    public void freeSeat(int seatNumber) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void selectSeat(int seatNumber) throws RemoteException {
        int eventID = 1;
        
        try {
            mPool.execute(new SelectedSeatTask(eventID, seatNumber, new SeatTask.OnWaitingTimeFinished() {
                @Override
                public void onSuccessfullyFinish(int eventID, int seatIndex) {
                    //mClientNotifier.notifyAll(eventID, seatIndex);
                    notifyClients(seatIndex, "Free");
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

    @Override
    public void reserveSeats(int[] seatNumbers) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelSeatsReservation() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void buySeats(int[] seatNumbers) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Event> getAllEvents() throws RemoteException {
        System.out.println("Fetching all events");
        
        //For DEBUG 
        //return new ArrayList<>(EventsRepositoryEndPoint.loadPersistentEvents().values());
        
        //UNCOMMENT for PRODUCTION
        return (ArrayList<Event>) new EventsRepository().findAll();
    }

    @Override
    public Event getEvent(int eventID) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void joinEventRoom() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}