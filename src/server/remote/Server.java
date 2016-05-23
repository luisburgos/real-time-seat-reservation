package server.remote;

import client.remote.ClientRemote;
import server.domain.Event;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.utils.ClientsHandler;
import server.data.EventsRepository;
import server.rooms.EventHandler;

public class Server extends UnicastRemoteObject implements ServerRemote {
    
    private static final long serialVersionUID = 11L;
        
    private final HashMap<Integer, EventHandler> mEventsRoomHandler;                    
    private final ClientsHandler mClientHandler;       

    public Server() throws RemoteException {
        super();
        mClientHandler = new ClientsHandler();        
        mEventsRoomHandler = new HashMap();                      
    }

    @Override
    public void registerClient(ClientRemote client) throws RemoteException {
        try {                
            String newClientKey = getClientHost();                     
            mClientHandler.register(newClientKey, client);                                       
            System.out.println("Register new client with key: " + newClientKey);            
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void unregisterClient(ClientRemote client) throws RemoteException {
        try {                
            String clientKey = getClientHost();                     
            mClientHandler.unregister(clientKey, client);
            System.out.println("Unregister client with key: " + clientKey);            
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     @Override
    public ArrayList<Event> getAllEvents() throws RemoteException {       
        ArrayList<Event> allEvents = null;
        try {
            System.out.println(getClientHost() + " is Fetching all events");
            //For DEBUG
            //return new ArrayList<>(EventsRepositoryEndPoint.loadPersistentEvents().values());
            
            //UNCOMMENT for PRODUCTION
            allEvents = (ArrayList<Event>) new EventsRepository().findAll();
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allEvents;
    }

    @Override
    public Event getEvent(int eventID) throws RemoteException {
        Event event = null;
        try {
            System.out.println(getClientHost() + " is getting event by id: " + eventID);            
            event = (Event) new EventsRepository().findByID(eventID);        
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return event;
    }
    
    @Override
    public void joinEventRoom(ClientRemote client, int eventID) throws RemoteException {      
        try {            
            if(!mEventsRoomHandler.containsKey(eventID)){
                mEventsRoomHandler.put(eventID, new EventHandler(eventID));
            }
            mEventsRoomHandler.get(eventID).registerClient(client);
            System.out.println("Joining room: " + getClientHost()+ " in event " + eventID);
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void leaveEventRoom(ClientRemote client, int eventID) throws RemoteException {
        if(mEventsRoomHandler.containsKey(eventID)){
            mEventsRoomHandler.get(eventID).unregisterClient(client);
        } else {
            System.out.println("El evento no esta disponible");
        }
    }
    
    @Override
    public void freeSeat(int seatNumber, int eventID) throws RemoteException {
        if(mEventsRoomHandler.containsKey(eventID)){
            mEventsRoomHandler.get(eventID).freeSeat(seatNumber);
        } else {
            System.out.println("El evento no esta disponible");
        }
    }

    @Override
    public void selectSeat(int seatNumber, int eventID) throws RemoteException {          
        if(mEventsRoomHandler.containsKey(eventID)){
            mEventsRoomHandler.get(eventID).selectSeat(seatNumber);
        } else {
            System.out.println("El evento no esta disponible");
        }                                
    }   

    @Override
    public void reserveSeats(int[] seatNumbers, int eventID) throws RemoteException {
        if(mEventsRoomHandler.containsKey(eventID)){
            mEventsRoomHandler.get(eventID).reserveSeats(seatNumbers);
        } else {
            System.out.println("El evento no esta disponible");
        }                     
    }    

    @Override
    public void buySeats(int[] seatNumbers, int eventID) throws RemoteException {
        if(mEventsRoomHandler.containsKey(eventID)){
            mEventsRoomHandler.get(eventID).buySeats(seatNumbers);
        } else {
            System.out.println("El evento no esta disponible");
        }
    }   

    @Override
    public void cancelSeatsSelection() throws RemoteException {        
        try {
            String hostIP = getClientHost();
            System.out.println(hostIP + " is canceling selection");
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }       
    
    @Override
    public void cancelSeatsReservation() throws RemoteException {
        try {
            String hostIP = getClientHost();
            System.out.println(hostIP + " is canceling reservation");
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void notifyClients(int seatIndex, String newState){
        
        HashMap newStates = new HashMap<>();
        newStates.put(seatIndex, newState);
        
        /*for(ClientRemote client : clients){
            try {
                client.updateSeatsState(newStates);
            } catch (RemoteException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/   
    }

}