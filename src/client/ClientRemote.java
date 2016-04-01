package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemote extends Remote {
    public void doSomething() throws RemoteException;
}