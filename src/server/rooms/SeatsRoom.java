/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.rooms;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author luisburgos
 */
public class SeatsRoom {
    
    public SeatsRoom(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        while (true) {
            Socket client = server.accept();
            System.out.println("Aceptado de" + client.getInetAddress());
            SeatsRoomHandler chatHandler = new SeatsRoomHandler(client);
            chatHandler.start();
        }
    }

    public static void main(String[] args) {

	if(args.length != 1){
            throw new RuntimeException("Sintaxis: ChatServer <port>");
        }
        try {
            new SeatsRoom(Integer.parseInt(args[0]));            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
}
