/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controllers;

import client.app.SeatReservationApplication;
import client.ui.windows.EventsWindow;
import client.utils.AppConstants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.domain.Event;

/**
 *
 * @author luisburgos
 */
public class EventsWindowCotroller {

    private final EventsWindow mView;
    
    public EventsWindowCotroller(EventsWindow view) {
        mView = view;
    }
    
    public void openEvent(Event selectedEvent){
        if(selectedEvent != null){
            mView.showSeatSelectionWindow(selectedEvent);
        }
    }

    public void loadEvents() {        
        try {
            ArrayList<Event> events = SeatReservationApplication.getRemoteRef().getAllEvents();            
            if(events != null && !events.isEmpty()){
                mView.showEvents(events);
            } else {
                mView.showErrorMessage(AppConstants.EMPTY_EVENTS);
            }
        } catch (RemoteException ex) {
            System.err.println(ex);
            mView.showErrorMessage(AppConstants.FETCH_EVENTS_ERROR_MESSAGE);
            Logger.getLogger(EventsWindowCotroller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
