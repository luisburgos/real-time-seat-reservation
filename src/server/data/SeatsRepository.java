/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import server.domain.Seat;
import java.util.List;

/**
 *
 * @author luisburgos
 */
public class SeatsRepository extends Repository<Seat> {

    @Override
    public int save(Seat entity) {
         int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "INSERT INTO asiento (estado, event_id, number) values(?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, entity.getState());
            pstmt.setInt(2, entity.getEventId());                       
            pstmt.setInt(3, entity.getSeatNumber());          
            
            iRet = pstmt.executeUpdate();            
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }

    /**
     * Actualiza en la base de datos el estado de un asiento.
     * @param seat Objeto tipo asiento
     * @return Entero indicando el resultado de la ejecución de la sentencia SQL.
     */
    @Override
    public int update(Seat seat) {
        int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "UPDATE asiento SET estado=? WHERE event_id=? AND number=?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, seat.getState());
            pstmt.setInt(2, seat.getEventId());
            pstmt.setInt(3, seat.getSeatNumber());
            
            iRet = pstmt.executeUpdate();
            
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }

    @Override
    public int delete(Seat entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * 
     * @param event_id Identificador del evento que se quiere obtener la lista de asientos
     * @return Lista de asiento correspondientes a un evento.
     */
    @Override
    public List findByName(int event_id) {
        ArrayList seatList = new ArrayList();
        
        try {
            String query = "SELECT * FROM asiento WHERE event_id=? ORDER BY number";
            Connection con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, event_id);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()){
                Seat seat = new Seat();
                seat.setSeatNumber(rs.getInt("number"));
                seat.setState(rs.getString("estado"));
                seatList.add(seat);
            }
            
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return seatList;
    }

    @Override
    public Seat findByID(int event_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
