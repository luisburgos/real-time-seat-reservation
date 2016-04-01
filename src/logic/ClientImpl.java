package logic;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by luisburgos on 13/03/16.
 */
public class ClientImpl {

    public static void main(String[] args) {
        try {

            Client client = new Client();
            Registry registry = LocateRegistry.getRegistry("192.168.229.231");

            ServerRemote server = (ServerRemote) registry.lookup("Server");
            server.registerClient(client);
            server.doSomethingOnClient();

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

}
