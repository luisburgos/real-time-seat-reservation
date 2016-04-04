/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import server.domain.Event;

/**
 *
 * @author luisburgos
 */
public class EventsRepositoryEndPoint {

    static {
        DATA = new HashMap(4);
        addEvent(1, "Event 1", "Descrtiption 1", "Site 1", new Date());
        addEvent(2, "Event 2", "Descrtiption 2", "Site 2", new Date());
        addEvent(3, "Event 3", "Descrtiption 3", "Site 3", new Date());
        addEvent(4, "Event 4", "Descrtiption 4", "Site 4", new Date());
        addEvent(5, "Event 5", "Descrtiption 5", "Site 5", new Date());
    }

    private final static HashMap<Integer, Event> DATA;

    private static void addEvent(int id, String name, String description, String site, Date date) {
        Event newEvent = new Event();
        
        newEvent.setId(id);
        newEvent.setName(name);
        newEvent.setDescription(description);
        newEvent.setSite(site);
        newEvent.setDate(date);
        
        DATA.put(id, newEvent);
    }

    public static HashMap<Integer, Event> loadPersistentEvents() {
        return DATA;
    }  
    
}
