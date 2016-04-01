package server;

import client.ClientRemote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

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
        doSomethingOnClient();
    }

    @Override
    public void doSomethingOnClient() throws RemoteException {
        for (ClientRemote clientRemote : clients){
            System.out.println("Doing something on " + clientRemote);
            clientRemote.doSomething();
        }
    }
}