/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.rooms;

import java.io.*;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;
/**
 *
 * @author luisburgos
 */
public class SeatsRoomHandler extends Thread {
 
     public static Vector handlers = new Vector();

    protected Socket socket;
    protected DataInputStream input;
    protected DataOutputStream output;

    public SeatsRoomHandler(Socket socket) throws IOException {
        this.socket = socket;
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void run(){
        try {
            handlers.addElement(this);
            while (true){
                String message = input.readUTF();
                broadcast(message);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            handlers.removeElement(this);
            try {
                socket.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    protected static void broadcast(String message) {
        synchronized (handlers) {
            Enumeration handlersElements = handlers.elements();
            while (handlersElements.hasMoreElements()){
                SeatsRoomHandler chatHandler = (SeatsRoomHandler) handlersElements.nextElement();
                try {
                    synchronized (chatHandler.output){
                        chatHandler.output.writeUTF(message);
                    }
                    chatHandler.output.flush();
                } catch (IOException e) {
                    chatHandler.stop();
                }
            }
        }
    }
    
}
