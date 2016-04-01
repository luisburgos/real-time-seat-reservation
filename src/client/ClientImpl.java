package client;

import domain.Event;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import server.ServerRemote;

/**
 * Created by luisburgos on 13/03/16.
 */
public class ClientImpl {

    public static void main(String[] args) {
        try {

            Client client = new Client();
            Registry registry = LocateRegistry.getRegistry("127.0.0.1");

            ServerRemote server = (ServerRemote) registry.lookup("Server");
            server.registerClient(client);
            server.doSomethingOnClient();
            ArrayList<Event> events = server.getAllEvents();

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

}
