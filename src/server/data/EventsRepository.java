/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.data;

import server.domain.Event;
import java.sql.*;
import java.util.*;
import server.data.dao.EventDAO;

public class EventsRepository extends Repository<Event>{   

    @Override
    public int save(Event entity) {
        int iRet = -1;
        /*try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "INSERT INTO Province (Id, ShortName, Name) values(?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, p.getId());
            pstmt.setInt(2, p.getShortName());
            pstmt.setInt(3, p.getName());
            
            iRet = pstmt.executeUpdate();
            
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }*/
        return iRet;
    }

    @Override
    public int update(Event entity) {
        int iRet = -1;
        /*try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "UPDATE Province SET ShortName=?, Name=? WHERE Id=?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(3, p.getId());
            pstmt.setInt(1, p.getShortName());
            pstmt.setInt(2, p.getName());
            
            iRet = pstmt.executeUpdate();
            
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }*/
        return iRet;
    }

    @Override
    public int delete(Event entity) {
        int iRet = -1;
        /*try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "DELETE FROM Province WHERE Id=?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, p.getId());
            
            iRet = pstmt.executeUpdate();
            
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }*/
        return iRet;
    }


    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public java.util.List findByName(int event_id) {
        ArrayList eventList = new ArrayList();
        
        try {
            String query = "SELECT * FROM evento WHERE event_id=?";
            Connection con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, event_id);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()){
                Event event = new Event();
                event.setId(event_id);
                event.setName(rs.getString("name"));
                event.setSite(rs.getString("site"));
                event.setDate(rs.getDate("date"));
                event.setDescription(rs.getString("description"));
                eventList.add(event);
            }
            
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return eventList;
    }

    @Override
    public java.util.List findAll() {        
        EventDAO dao = new EventDAO();
        ArrayList eventList = dao.getList();        
        return eventList;
    }

    @Override
    public Event findByID(int event_id) {
        EventDAO dao = new EventDAO();
        Event event = dao.get(event_id);        
        return event;
    }
}
