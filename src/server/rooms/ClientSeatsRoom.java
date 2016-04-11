/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.rooms;

import java.net.*;
import java.io.*;
import java.awt.*;

/**
 *
 * @author luisburgos
 */
public class ClientSeatsRoom extends Frame implements Runnable{

    protected DataInputStream in;
    protected DataOutputStream out;
    protected TextArea areaOutput;
    protected TextField userInput;
    protected Thread listener;

    public ClientSeatsRoom(String title, InputStream in, OutputStream out){
        super(title);

        this.in = new DataInputStream((new BufferedInputStream(in)));
        this.out = new DataOutputStream(new BufferedOutputStream(out));

        initUI();

        listener = new Thread(this);
        listener.start();
    }

    public void run() {
        try {
            while (true) {
                String line = in.readUTF();
                areaOutput.append(line + "\n");
            }
        }catch (IOException io) {
            io.printStackTrace();
        }
        finally {
            listener = null;
            userInput.hide();
            validate();
            try {
                out.close();
            }catch(IOException io) {
                io.printStackTrace();
            }
        }
    }

    public boolean handleEvent(Event event) {
        if((event.target == userInput) && (event.id == Event.ACTION_EVENT)) {
            try {
                out.writeUTF((String)event.arg);
                out.flush();
            }catch (IOException io) {
                io.printStackTrace();
                listener.stop();
            }
            userInput.setText("");
            return true;
        }
        else if((event.target == this) && (event.id == Event.WINDOW_DESTROY) ) {
            if(listener != null) {
                listener.stop();
            }
            hide();
            return true;
        }
        return super.handleEvent(event);
    }

    private void initUI(){
        setLayout(new BorderLayout());
        add("Center", areaOutput = new TextArea());
        areaOutput.setEditable(false);

        add("South", userInput = new TextField());
        pack();

        setVisible(true);
        userInput.requestFocus();
    }

    public static void main(String[] args) {
        if(args.length != 2) {
            throw new RuntimeException(("Sintaxis: ChatClient <host> <puerto>"));
        }

        try {
            Socket s = new Socket(args[0],Integer.parseInt(args[1]));
            new ClientSeatsRoom("Chat" + args[0]+":"+args[1], s.getInputStream(),s.getOutputStream());
        }
        catch (IOException io) {
            io.printStackTrace();
        }

    }

}
