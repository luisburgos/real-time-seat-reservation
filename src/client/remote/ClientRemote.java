package client.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ClientRemote extends Remote {
    public void updateSeatsState(HashMap<Integer, String> newStates) throws RemoteException;
    public void notifyPurchaseSuccesful() throws RemoteException;
    public void notifyPurchaseFailure() throws RemoteException;
}