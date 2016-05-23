/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.windows;

import client.controllers.ButtonSelectWindowController;
import client.controllers.SessionControl;
import client.remote.SeatReservationClient;
import client.ui.buttons.ButtonStates;
import java.awt.GridLayout;
import client.ui.buttons.SeatButton;
import java.awt.Component;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import server.domain.Event;

/**
 *
 * @author JoseJulio
 */
public class ButtonSelectWindow extends javax.swing.JFrame implements SeatButton.OnStateChangeListener {

    private ButtonSelectWindowController mController;
    private Event mCurrentEvent;
    
    /**
     * Creates new form ButtonSelectWindow
     */
    public ButtonSelectWindow(Event selectedEvent) {
        initComponents();        
        initSeatGrid();
        mCurrentEvent = selectedEvent;
        mController = new ButtonSelectWindowController(this);
        mController.joinEventRoom(mCurrentEvent);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonPanel = new javax.swing.JPanel();
        exitButton = new javax.swing.JButton();
        reserveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        buttonPanel.setLayout(new java.awt.GridLayout(1, 0));

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        reserveButton.setText("Buy");
        reserveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reserveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
                        .addComponent(reserveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(reserveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
       this.setVisible(false);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void reserveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserveButtonActionPerformed
       mController.doReservation(mCurrentEvent);
    }//GEN-LAST:event_reserveButtonActionPerformed
    
    private void initSeatGrid(){
        GridLayout layout = new GridLayout(10,5);
        this.buttonPanel.setLayout(layout);
        for(int i = 1; i<= 50 ; i++){
            SeatButton button = new SeatButton(i);
            button.setStateListener(this);
            this.buttonPanel.add(button);            
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton reserveButton;
    // End of variables declaration//GEN-END:variables

    public void openDetailReservationWindow(int[] selectedSeats, int id) {
        new DetailWindow(selectedSeats , id).setVisible(true);
    }
    
    @Override
    public void onSelect(int seatNumber) {
        System.out.println("On Select seat " + (seatNumber));
        try {
            SessionControl
                    .getInstance()
                    .selectSeat(seatNumber);
            mController.onSeatSelected(seatNumber,mCurrentEvent.getId());
            SeatReservationClient
                    .getInstance()
                    .selectSeat(seatNumber, mCurrentEvent.getId());
        } catch (RemoteException ex) {
            Logger.getLogger(ButtonSelectWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateSeat(Integer seatNumber, String newState) {
        if(newState.equalsIgnoreCase(ButtonStates.FREE)){
            SessionControl.getInstance().setFreeSeat(seatNumber);
        }
        System.out.println("Update seat " + seatNumber + " with value " + newState);                
        Component component = buttonPanel.getComponent(seatNumber-1);
        if(component instanceof SeatButton){                      
            SeatButton button = (SeatButton) component;    
            
            //Si estaba seleccionado y se va a deseleccionar
            if(newState.equalsIgnoreCase(ButtonStates.FREE)){
                mController.onSeatUnselected();
            }
            
            System.out.println("Before change state was " + button.getCurrentState());
            button.changeState(newState);
            System.out.println("After change state was " + button.getCurrentState());          
            this.revalidate();
            this.repaint();            
            System.out.println("Update UI");
        }               
    }
    
    public void onPurchaseSuccess(){
        JOptionPane.showMessageDialog(this, "La compra ha sido exitosa");
        this.setVisible(false);
    }
    
    private boolean seatIsSetFree(String currentState, String newState){
        return newState.equalsIgnoreCase(ButtonStates.FREE) && (
                    currentState.equalsIgnoreCase(ButtonStates.SELECTED) ||
                    currentState.equalsIgnoreCase(ButtonStates.RESERVED) ||
                    currentState.equalsIgnoreCase(ButtonStates.SOLD)
                );
    }
    
}
