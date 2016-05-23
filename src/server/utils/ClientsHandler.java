/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utils;

import client.remote.ClientRemote;
import java.util.HashMap;

/**
 *
 * @author luisburgos
 */
public class ClientsHandler {

    public HashMap<String, ClientRemote> clientsMap;
    
    public ClientsHandler(){
        clientsMap = new HashMap();
    }
    
    public void notifyAll(int eventID, int seatIndex) {
        
        
        
    }

    public void register(String newClientKey, ClientRemote client) {
        clientsMap.put(newClientKey, client);
    }

    public void unregister(String clientKey, ClientRemote client) {
        clientsMap.remove(clientKey, client);                                       
    }
    
}
