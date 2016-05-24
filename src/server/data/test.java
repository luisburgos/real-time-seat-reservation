/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.data;

import server.domain.Seat;

/**
 *
 * @author User
 */
public class test {
    public static void main(String[] args) {
        Seat seat = new Seat();
        seat.setEventId(1);
        seat.setSeatNumber(1);
        seat.setState("reserved");
        SeatsRepository srep = new SeatsRepository();
        srep.update(seat);
    }
}
