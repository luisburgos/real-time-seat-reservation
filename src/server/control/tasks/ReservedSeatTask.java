/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.control.tasks;

/**
 *
 * @author luisburgos
 */
public class ReservedSeatTask extends SeatTask {

    private final static String TAG = "RESERVED SEAT";
    private final static long SLEEP_TIME = 100000;

    public ReservedSeatTask(int eventID, int seatNumber) {
        super(TAG, SLEEP_TIME, eventID, seatNumber);
    }

    public ReservedSeatTask(int eventID, int seatNumber, OnWaitingTimeFinished listener) {
        this(eventID, seatNumber);
        setWaitingFinishListener(listener);
    }

}
