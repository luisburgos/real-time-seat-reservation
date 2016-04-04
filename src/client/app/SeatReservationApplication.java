/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.app;

import client.SeatReservationClient;
import client.ui.EventsWindow;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import server.ServerRemote;
import server.domain.Event;

/**
 *
 * @author luisburgos
 */
public class SeatReservationApplication {
    
    private SeatReservationClient mClient;
    private ServerRemote mServer;
    
    private EventsWindow eventsWindow;
    
    public SeatReservationApplication() throws RemoteException, NotBoundException{
        
        mClient = new SeatReservationClient();
        Registry registry = LocateRegistry.getRegistry(AppConstants.REGISTRY_IP);

        mServer = (ServerRemote) registry.lookup(AppConstants.LOOKUP_SERVER_REMOTE);
        mServer.registerClient(mClient);
        
        ArrayList<Event> events = mServer.getAllEvents();
        
        eventsWindow = new EventsWindow(events);
    }
    
    private void start() {
        eventsWindow.setVisible(true);
    }
    
    public static void main(String[] args) {      
        try {
            new SeatReservationApplication().start();       
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }        
    }

   
    
}
