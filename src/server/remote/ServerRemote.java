package server.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import client.remote.ClientRemote;
import server.domain.Event;
import java.util.ArrayList;

public interface ServerRemote extends Remote {
    public void registerClient(ClientRemote client) throws RemoteException;

    public void joinEventRoom() throws RemoteException;
    
    public void freeSeat(int seatNumber) throws RemoteException;
    public void selectSeat(int seatNumber) throws RemoteException;
    public void reserveSeats(int[] seatNumbers) throws RemoteException;
    public void cancelSeatsReservation() throws RemoteException;
    public void buySeats(int[] seatNumbers) throws RemoteException;
    
    public ArrayList<Event> getAllEvents() throws RemoteException;
    public Event getEvent(int eventID) throws RemoteException;
}