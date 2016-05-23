/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.app;

import client.remote.SeatReservationClient;
import client.utils.AppConstants;
import client.ui.windows.EventsWindow;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import server.remote.ServerRemote;

/**
 *
 * @author luisburgos
 */
public class SeatReservationApplication {        
    
    private static SeatReservationClient mClient;
    public static ServerRemote mServer;
    
    private final EventsWindow eventsWindow;
    
    public SeatReservationApplication() throws RemoteException, NotBoundException   {
        
        mClient = SeatReservationClient.getInstance();
        Registry registry = LocateRegistry.getRegistry(AppConstants.REGISTRY_IP);

        mServer = (ServerRemote) registry.lookup(AppConstants.LOOKUP_SERVER_REMOTE);
        mServer.registerClient(mClient);          
        
        eventsWindow = new EventsWindow();
    }
    
    public static ServerRemote getRemoteRef(){
        return mServer;
    }
    
    private void start() {
        eventsWindow.setVisible(true);
    }
    
    public static void main(String[] args) {      
        try {
            new SeatReservationApplication().start();       
        } catch (RemoteException e) {
            treatRemoteError(e);
        } catch (NotBoundException ex) {
            treatNotBoundError(ex);
        }        
    }

    private static void treatRemoteError(RemoteException e) {
        System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,
                    AppConstants.CONNECTION_ERROR_MESSAGE,
                    AppConstants.CONNECTION_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE
            );
            finish();
    }
   
    private static void treatNotBoundError(NotBoundException e) {
        System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null,
                    AppConstants.BOUND_ERROR_MESSAGE,
                    AppConstants.BOUND_ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE
            );
            finish();
    }
    
    public static void finish() {
        if(mServer != null){
            try {
                mServer.unregisterClient(mClient);
                System.exit(0);
            } catch (RemoteException ex) {
                System.exit(1);
                Logger.getLogger(SeatReservationApplication.class.getName()).log(Level.SEVERE, null, ex);                
            }
        }
        System.exit(0);
    }
}
