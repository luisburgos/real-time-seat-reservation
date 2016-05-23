package client.remote;

import client.app.SeatReservationApplication;
import client.controllers.SessionControl;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeatReservationClient extends UnicastRemoteObject implements ClientRemote {
    
    private static final long serialVersionUID = 11L;
    private SeatStateChangeListener mListener;
    private SeatPurchaseListener purchaseListener;
    private static SeatReservationClient instance;
    
    private SeatReservationClient() throws RemoteException {    
        super();
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
    
    public void setSeatPurchaseListener(SeatPurchaseListener listener){
        purchaseListener = listener;
    }
     
    @Override
    public void updateSeatsState(HashMap<Integer, String> newStates) throws RemoteException {
        if(mListener != null){
            mListener.onUpdate(newStates);
        }
    }
    
    @Override
    public void notifyPurchaseSuccesful() throws RemoteException{
        if(purchaseListener != null){
            purchaseListener.onPurchaseSuccess();
        }
    }
    
    public void selectSeat(int seatNumber, int eventID) {
        try {
            SeatReservationApplication.getRemoteRef().selectSeat(seatNumber, eventID);
        } catch (RemoteException ex) {
            Logger.getLogger(SeatReservationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void freeSeat(int seatNumber, int eventID){
        try{
           SeatReservationApplication.getRemoteRef().freeSeat(seatNumber, eventID);
        }catch(RemoteException ex){
           Logger.getLogger(SeatReservationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reserveSeats(int[] seatNumbers, int eventID){
        try {
           
            SeatReservationApplication.getRemoteRef().reserveSeats(seatNumbers, eventID);
        } catch (RemoteException ex) {
            Logger.getLogger(SeatReservationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void joinEventRoom(int eventID) {
        try {
            SeatReservationApplication.getRemoteRef().joinEventRoom(getInstance(), eventID);
        } catch (RemoteException ex) {
            Logger.getLogger(SeatReservationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void requesrPurchase(int[] seatsToPurchase, int event_id){
        try {
            SeatReservationApplication.getRemoteRef().buySeats(seatsToPurchase, event_id);
        } catch (RemoteException ex) {
            Logger.getLogger(SeatReservationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public interface SeatStateChangeListener {
        void onUpdate(HashMap<Integer, String> newStates);
    }
    
    public interface SeatPurchaseListener{
        void onPurchaseSuccess();
        void onPurchaseFailure();
    }
    
}