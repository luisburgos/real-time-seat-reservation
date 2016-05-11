/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.data.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import server.domain.Event;

/**
 *
 * @author luisburgos
 */
public class EventDAO extends DAO<Event>{

    @Override
    public void add(Event event) throws SQLException {
        saveEntity(event);
    }

    @Override
    public void delete(Event event) {
        deleteEntity(event);
    }

    @Override
    public void update(Event event) {
        updateEntity(event);
    }

    @Override
    public Event get(int objectId) {
        Event event = null;

        try {
            openSession();
            event = (Event) session.get(Event.class, objectId);
        } finally {
            session.close();
        }
        
        return event;
    }

    @Override
    public ArrayList<Event> getList() {       
        ArrayList eventList = null;

        try {
            openSession();
            
            //eventList = (ArrayList) session.createCriteria(Event.class).list();
            eventList = (ArrayList<Event>) session
                    .createSQLQuery("SELECT * FROM evento")
                    .addEntity(Event.class)
                    .list();
        } finally {
            session.close();
        }

        return eventList;
    }
    
}
