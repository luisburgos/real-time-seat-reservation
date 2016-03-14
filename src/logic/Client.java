package logic;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

class Client implements ClientRemote {
    public Client() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void doSomething() throws RemoteException {
        System.out.println("Server invoked doSomething()");
    }
}