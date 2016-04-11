package client.remote;

import client.app.SeatReservationApplication;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeatReservationClient implements ClientRemote {
    
    private SeatStateChangeListener mListener;
    private static SeatReservationClient instance;
    
    private SeatReservationClient() throws RemoteException {    
        UnicastRemoteObject.exportObject(this, 0);
    }
    
    public static SeatReservationClient getInstance() throws RemoteException {
        if(instance == null){
            instance = new SeatReservationClient();
        }
        return instance;
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

    public void selectSeat(int seatNumber) {
        try {
            SeatReservationApplication.getRemoteRef().selectSeat(seatNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(SeatReservationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public interface SeatStateChangeListener {
        void onUpdate(HashMap<Integer, String> newStates);
    }
    
}