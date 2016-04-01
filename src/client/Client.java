package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Client implements ClientRemote {
    
    private SeatStateChangeListener mListener;
    
    public Client() throws RemoteException {    
        UnicastRemoteObject.exportObject(this, 0);
    }

    public void setSeatStateChangeListener(SeatStateChangeListener listener){
        mListener = listener;
    }
    
    @Override
    public void doSomething() throws RemoteException {
        System.out.println("Server invoked doSomething()");
    }

    @Override
    public void updateSeatsState(HashMap<Integer, String> newStates) throws RemoteException {
        if(mListener != null){
            mListener.onUpdate(newStates);
        }
    }
    
    public interface SeatStateChangeListener {
        void onUpdate(HashMap<Integer, String> newStates);
    }
    
}