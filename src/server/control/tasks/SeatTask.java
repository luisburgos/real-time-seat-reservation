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
public abstract class SeatTask implements Runnable {

    private Thread mThread;
    private String TAG;
    private long SLEEP_TIME;

    private int eventID;
    private int seatIndex;

    private OnWaitingTimeFinished mListener;

    public SeatTask(String tag, long time, int eventID, int seatIndex) {
        TAG = tag;
        SLEEP_TIME = time;
        this.eventID = eventID;
        this.seatIndex = seatIndex;
        System.out.println("Creating a new " + TAG + " task.");
    }

    public void setWaitingFinishListener(OnWaitingTimeFinished listener) {
        mListener = listener;
    }

    public void start() {
        System.out.println("Starting " + TAG);
        if (mThread == null) {
            mThread = new Thread(this, TAG);
            mThread.start();
        }
    }
    
    @Override
    public void run() {
        System.out.println("Running " + TAG);
        startWaiting();
        System.out.println("Thread " + TAG + " exiting.");
    }
    
    public void cancel() { mThread.interrupt(); }

    private void startWaiting() {
        try {
            System.out.println("Sleeping: " + TAG + " for " + SLEEP_TIME);
            Thread.sleep(SLEEP_TIME);
            mListener.onSuccessfullyFinish(eventID, seatIndex);
        } catch (InterruptedException e) {
            System.out.println("Thread " + TAG + " interrupted.");
            mListener.onSuccessfullyFinish(eventID, seatIndex);
        }
    }

    public interface OnWaitingTimeFinished {
        void onSuccessfullyFinish(int eventID, int seatIndex);        
    }

}
