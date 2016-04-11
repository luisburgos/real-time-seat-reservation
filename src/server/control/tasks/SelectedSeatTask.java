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
public class SelectedSeatTask extends SeatTask {

    private final static String TAG = "SELECTED SEAT";
    private final static long SLEEP_TIME = 10000;

    public SelectedSeatTask(int eventID, int seatNumber) {
        super(TAG, SLEEP_TIME, eventID, seatNumber);
    }

    public SelectedSeatTask(int eventID, int seatNumber, OnWaitingTimeFinished listener) {
        this(eventID, seatNumber);
        setWaitingFinishListener(listener);
    }

}
