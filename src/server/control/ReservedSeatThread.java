/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.control;

import server.control.tasks.SeatTask;

/**
 *
 * @author luisburgos
 */
public class ReservedSeatThread extends SeatTask {

    private final static String TAG = "ReservedSeatThread";
    private final static long SLEEP_TIME = 50000;

    public ReservedSeatThread(int eventID, int seatNumber) {
        super(TAG, SLEEP_TIME, eventID, seatNumber);
    }

}
