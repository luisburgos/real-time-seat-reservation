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
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.control.SeatsThreadPool;
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
    
    private final int eventID;    
    private final int seatsNumber = 50;
    private HashMap<Integer, String> seats;
    private SeatsThreadPool mPool;
    
    public EventHandler(int eventID) {
        super();
        this.eventID = eventID;
        this.participantClients = new ArrayList<>();
        mPool = new SeatsThreadPool(50, 50);
        initSeats();
        updateSeats();
    }
    
    private void initSeats(){
        seats = new HashMap<>();
        for(int i=1; i <= seatsNumber; i++){
            seats.put(i, ButtonStates.FREE);
        }
    }

    public void selectSeat(int seatNumber) {              
        System.out.println("Selecting seat " + seatNumber + " from event " + eventID);
        Seat seat = new Seat(eventID, ButtonStates.SELECTED, seatNumber);
        try {
            seats.replace(seatNumber, ButtonStates.SELECTED);
            mPool.execute(new SelectedSeatTask(eventID, seatNumber, new SeatTask.OnWaitingTimeFinished() {
                @Override
                public void onSuccessfullyFinish(int eventID, int seatIndex) {
                    //mClientNotifier.notifyAll(eventID, seatIndex);
                    System.out.println("Free seat " + seatNumber + " from event " + eventID);
                    seat.setState(ButtonStates.FREE);
                    seats.replace(seatNumber, ButtonStates.FREE);
                    notifyClients(seatIndex, ButtonStates.FREE);
                }
            }));
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void notifyClients(int seatIndex, String newState){
        
        HashMap newStates = new HashMap<>();
        newStates.put(seatIndex, newState);
        
        for(ClientRemote client : participantClients){
            try {
                client.updateSeatsState(newStates);
            } catch (RemoteException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void registerClient(ClientRemote client) {
        participantClients.add(client);        
    }
    
    public void unregisterClient(ClientRemote client) {
        participantClients.remove(client);
    }
    

    public void freeSeat(int seatNumber) {
        seats.replace(seatNumber, ButtonStates.FREE);
        notifyClients(seatNumber, ButtonStates.FREE);
    }

    public void reserveSeats(int[] seatNumbers) {
        for (int seatNumber : seatNumbers) {
            System.out.println("Reserving seat " + seatNumber);
            seats.replace(seatNumber, ButtonStates.RESERVED);
            notifyClients(seatNumber, ButtonStates.RESERVED);
        }
    }

    public void cancelSeatsReservation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void buySeats(int[] seatNumbers) {
        SeatsRepository rep = new SeatsRepository();       
        for (int seatNumber : seatNumbers) {
            try {
                rep.update(new Seat(eventID, ButtonStates.SOLD, seatNumber));
                seats.replace(seatNumber, ButtonStates.SOLD);
                notifyClients(seatNumber, ButtonStates.SOLD);
            } catch(Exception e){
                System.out.println("No se pudo completar la compra.\n" + e.getMessage());
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
   
}
