package server.remote;

import server.control.tasks.SelectedSeatTask;
import server.control.SeatsThreadPool;
import client.remote.ClientRemote;
import client.ui.buttons.ButtonStates;
import server.domain.Event;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
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
import server.data.SeatsRepository;
import server.domain.Seat;
import server.rooms.EventHandler;

public class Server extends UnicastRemoteObject implements ServerRemote {
    
    private static final long serialVersionUID = 11L;
    public static Vector<ClientRemote> clients;
    
    private ClientNotifier mClientNotifier;
    private SeatsThreadPool mPool;
    
    public HashMap<Integer, EventHandler> eventsHandler;

    public Server() throws RemoteException {
        super();
        this.clients = new Vector<>();
        mPool = new SeatsThreadPool(50, 50);
        //UnicastRemoteObject.exportObject(this, 0);
        this.eventsHandler = new HashMap();
    }

    @Override
    public void registerClient(ClientRemote client) throws RemoteException {
        try {
            clients.add(client);
            System.out.println("Register new client from " + getClientHost());
            System.out.println(client);
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void freeSeat(int seatNumber) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void selectSeat(int seatNumber, int eventID) throws RemoteException {   
        eventID = 1;
        eventsHandler.get(eventID).selectSeat(seatNumber);
        /*     
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
                */
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
        return new ArrayList<>(EventsRepositoryEndPoint.loadPersistentEvents().values());
        
        //UNCOMMENT for PRODUCTION
        //return (ArrayList<Event>) new EventsRepository().findAll();
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