package logic;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface ClientRemote extends Remote {
    public void doSomething() throws RemoteException;
}