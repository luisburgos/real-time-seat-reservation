package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ClientRemote extends Remote {
    public void doSomething() throws RemoteException;
    
    public void updateSeatsState(HashMap<Integer, String> newStates) throws RemoteException;
}