package server;

import client.ClientRemote;
import com.sun.corba.se.spi.activation.Repository;
import server.domain.Event;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Vector;
import server.data.EventsRepository;

public class Server implements ServerRemote {
    
    public static Vector<ClientRemote> clients;

    public Server() throws RemoteException {
        this.clients = new Vector<>();
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void registerClient(ClientRemote client) throws RemoteException {
        clients.add(client);
        System.out.println("Register new client");
        System.out.println(clients);
    }

    @Override
    public void doSomethingOnClient() throws RemoteException {
        for (ClientRemote clientRemote : clients){
            System.out.println("Doing something on " + clientRemote);
            clientRemote.doSomething();
        }
    }

    @Override
    public void freeSeat(int seatNumber) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void selectSeat(int seatNumber) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        System.out.println("Fetch all events");
        return (ArrayList<Event>) new EventsRepository().findAll();
    }

    @Override
    public Event getEvent(int eventID) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}