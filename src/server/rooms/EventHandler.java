/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.rooms;

import client.remote.ClientRemote;
import client.ui.buttons.ButtonStates;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.control.SeatsThreadPool;
import server.control.tasks.ReservedSeatTask;
import server.control.tasks.SeatTask;
import server.control.tasks.SelectedSeatTask;
import server.data.SeatsRepository;
import server.domain.Seat;
import server.remote.Server;

/**
 *
 * @author User
 */

public class EventHandler {
        
    private final ArrayList<ClientRemote> participantClients;
    private final HashMap<Integer, Future> runningThreads;
    private final int eventID;    
    private final int seatsNumber = 50;
    private HashMap<Integer, String> seats;
    private SeatsThreadPool mPool;
    
    public EventHandler(int eventID) {
        super();
        this.eventID = eventID;
        this.participantClients = new ArrayList();
        mPool = new SeatsThreadPool(50, 50);
        initSeats();
        runningThreads = new HashMap<>();
        updateSeats();
    }
    
    private void initSeats(){
        seats = new HashMap<>();
        for(int i=1; i <= seatsNumber; i++){
            seats.put(i, ButtonStates.FREE);
        }
    }     

    public void registerClient(ClientRemote client) {
        participantClients.add(client);   
        //Update clients when one register.
        for (Map.Entry<Integer, String> entry : seats.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            notifyClients(key, value);
        }        
    }
    
    public void unregisterClient(ClientRemote client) {
        participantClients.remove(client);
    }
    
    public void selectSeat(int seatNumber) {              
        System.out.println("Selecting seat " + seatNumber + " from event " + eventID);
        notifyClients(seatNumber, ButtonStates.SELECTED);
        Seat seat = new Seat(eventID, ButtonStates.SELECTED, seatNumber);
        try {
            seats.replace(seatNumber, ButtonStates.SELECTED);           
            Future future = mPool.submit(new SelectedSeatTask(eventID, seatNumber, new SeatTask.OnWaitingTimeFinished() {
                @Override
                public void onSuccessfullyFinish(int eventID, int seatIndex) {
                    //mClientNotifier.notifyAll(eventID, seatIndex);
                    System.out.println("Free seat " + seatNumber + " from event " + eventID);
                    seat.setState(ButtonStates.FREE);
                    seats.replace(seatNumber, ButtonStates.FREE);
                    notifyClients(seatIndex, ButtonStates.FREE);
                }
            }));  
            runningThreads.put(seatNumber, future);
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public void freeSeat(int seatNumber) {
        if(seatNumber > 0){
            seats.replace(seatNumber, ButtonStates.FREE);
            notifyClients(seatNumber, ButtonStates.FREE);
        }        
    }

    public void reserveSeats(int[] seatNumbers, ClientRemote client) {
        for (int seatNumber : seatNumbers) {
            if(seatNumber > 0){
                /*System.out.println("Reserving seat " + seatNumber);
                seats.replace(seatNumber, ButtonStates.RESERVED);
                notifyClients(seatNumber, ButtonStates.RESERVED);*/
                reserveSeat(seatNumber, client);
            }
        }
    }    
    
    private void reserveSeat(int seatNumber, ClientRemote client) {
        System.out.println("Reserving seat " + seatNumber + " from event " + eventID);
        notifyClients(seatNumber, ButtonStates.RESERVED);
        Seat seat = new Seat(eventID, ButtonStates.RESERVED, seatNumber);
        try {
            seats.replace(seatNumber, ButtonStates.RESERVED);           
            Future future = mPool.submit(new ReservedSeatTask(eventID, seatNumber, new SeatTask.OnWaitingTimeFinished() {
                @Override
                public void onSuccessfullyFinish(int eventID, int seatIndex) {
                    //mClientNotifier.notifyAll(eventID, seatIndex);
                    System.out.println("Free seat " + seatNumber + " from event " + eventID);
                    seat.setState(ButtonStates.FREE);
                    seats.replace(seatNumber, ButtonStates.FREE);
                    try {                                       
                        client.notifyPurchaseFailure();
                    } catch(RemoteException e){
                        e.printStackTrace();
                        System.out.println("No se pudo completar la compra. " + e.getMessage());
                    }
                    notifyClients(seatIndex, ButtonStates.FREE);
                }
            }));                      
            runningThreads.put(seatNumber, future);
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buySeats(int[] seatNumbers, ClientRemote client) {
        cancelReservation(seatNumbers);
        SeatsRepository rep = new SeatsRepository();       
        for (int seatNumber : seatNumbers) {
            if(seatNumber > 0){
                rep.update(new Seat(eventID, ButtonStates.SOLD, seatNumber));
                seats.replace(seatNumber, ButtonStates.SOLD);        
                try {                                       
                    client.notifyPurchaseSuccesful();                   
                } catch(RemoteException e){
                    e.printStackTrace();
                    System.out.println("No se pudo completar la compra. " + e.getMessage());
                }
                notifyClients(seatNumber, ButtonStates.SOLD);
            }            
        }
    }
    
    private void notifyClients(int seatIndex, String newState){        
        HashMap newStates = new HashMap<>();
        newStates.put(seatIndex, newState);
        
        for(ClientRemote client : participantClients){
            try {
                System.out.println("Notifying client " + client);
                client.updateSeatsState(newStates);
            } catch (RemoteException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void updateSeats() {
        List soldSeats = new SeatsRepository().findByName(eventID);
        for (Object o : soldSeats) {
            Seat seat = (Seat)o;
            seats.replace(seat.getSeatNumber(), seat.getState());
            notifyClients(seat.getSeatNumber(), seat.getState());
        }
    }
    
    public void cancelSelection(int[] seatNumbers) {
        Future current;
        for(int seatNumber : seatNumbers){
            if(seatNumber > 0){
                System.out.println("Cancelling seat selection of " + seatNumber);
                current = runningThreads.get(seatNumber);
                current.cancel(true);
            }            
        }
    }

    public void cancelReservation(int[] seatNumbers) {
        Future current;        
        for(int seatNumber : seatNumbers){
            if(seatNumber > 0){
                System.out.println("Cancelling seat reservation of " + seatNumber);
                current = runningThreads.get(seatNumber);
                current.cancel(true);
            }
        }
    }
   
}
