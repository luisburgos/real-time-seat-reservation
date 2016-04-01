package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import client.ClientRemote;

public interface ServerRemote extends Remote {
    public void registerClient(ClientRemote client) throws RemoteException;
    public void doSomethingOnClient() throws RemoteException;
}