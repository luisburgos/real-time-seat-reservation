package server;

import server.remote.Server;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.data.SeatsRepository;
import server.domain.Seat;

/**
 * Created by luisburgos on 13/03/16.
 */
public class ServerRunner {

    private final Server mServer;
    private final int DEFAULT_REGISTRY_PORT = 1099;
    
    public ServerRunner() throws RemoteException{
        mServer = new Server();        
        System.out.println("Server is created!");
    }
    
    private void run() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(DEFAULT_REGISTRY_PORT);
        registry.rebind("Server", mServer);        
        System.out.println("Server is running!");
    }
    
    public static void main(String[] args){
        try {               
            new ServerRunner().run();            
        } catch (RemoteException ex) {
            Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**
     * WARNING
     */
    private static void initSeats() {       
        ArrayList<Seat> seats = new ArrayList<>();
        for(int i = 1; i <= 50; i++){
            Seat seat = new Seat();
            seat.setEventId(1);
            seat.setSeatNumber(i);
            seat.setState("FREE");
            seats.add(seat);
        }
        
        SeatsRepository repo = new SeatsRepository();
        for(Seat seat : seats){
            repo.save(seat);
            System.out.println("Saved :" + seat.toString());
        }
    }

}
