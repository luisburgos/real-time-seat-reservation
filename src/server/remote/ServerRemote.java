package server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import client.remote.ClientRemote;
import client.remote.SeatReservationClient;
import server.domain.Event;
import java.util.ArrayList;

public interface ServerRemote extends Remote {
    
    public void registerClient(ClientRemote client) throws RemoteException;
    public void unregisterClient(ClientRemote mClient) throws RemoteException;
      
    public ArrayList<Event> getAllEvents() throws RemoteException;
    public Event getEvent(int eventID) throws RemoteException;

    public void joinEventRoom(ClientRemote client, int event_id) throws RemoteException;
    public void leaveEventRoom(ClientRemote client, int event_id) throws RemoteException;
        
    public void freeSeat(int seatNumber, int event_id) throws RemoteException;
    public void selectSeat(int seatNumber, int event_id) throws RemoteException;
    public void reserveSeats(int[] seatNumbers, int event_id) throws RemoteException;    
    public void buySeats(int[] seatNumbers, int event_id) throws RemoteException;
    
    public void cancelSeatsSelection(int[] seatNumbers, int eventID) throws RemoteException;
    public void cancelSeatsReservation(int[] seatNumbers, int eventID) throws RemoteException;       
    
}