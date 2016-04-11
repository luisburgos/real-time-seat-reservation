/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author luisburgos
 */
public class SeatsThreadPool {

    private final ExecutorService pool;

    public SeatsThreadPool(int noOfThreads, int maxNoOfTasks) {
        pool = Executors.newFixedThreadPool(maxNoOfTasks);
    }

    public synchronized void execute(Runnable task) {
        pool.execute(task);
    }

}
