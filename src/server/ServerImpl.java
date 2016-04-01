package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by luisburgos on 13/03/16.
 */
public class ServerImpl {

    public static void main(String[] args){

        try{
            //Create and get reference to rmi registry
            //Remember to change when is used in distributed computers "get" to "createRegistry"
            Registry registry = LocateRegistry.createRegistry(1099);
            //Instantiate server object
            Server server = new Server();
            //Register server object
            registry.rebind("Server", server);
            System.out.println("Server server is created!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
