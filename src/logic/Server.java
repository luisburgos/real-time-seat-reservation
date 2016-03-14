package logic;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

class Server implements ServerRemote {
    private volatile Vector<ClientRemote> clients = new Vector<>();

    public Server() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void registerClient(ClientRemote client) throws RemoteException {
        clients.add(client);
    }

    @Override
    public void doSomethingOnClient() throws RemoteException {
        for (ClientRemote clientRemote : clients){
            clientRemote.doSomething();
        }
    }
}