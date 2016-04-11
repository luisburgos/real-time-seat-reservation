/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.buttons;

import client.controllers.SessionControl;
import client.ui.windows.ButtonSelectWindow;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import client.ui.windows.SeatActionsWindow;
import javax.swing.JOptionPane;

/**
 *
 * @author JoseJulio
 */
public class SeatButton extends JButton implements ActionListener{
    
    private String currentState;
    private int seatNumber;
    private OnStateChangeListener listener;
  
    public SeatButton(int seatNumber){
        this.seatNumber = seatNumber;
        changeState(ButtonStates.FREE);
        this.addActionListener(this);
    }
    
    public void changeState(String state){
        currentState = state;
        updateButton();
    }
    
    private void updateButton(){
        switch(currentState){
            case ButtonStates.FREE: {
                OnButtonFree();
                break;
            }
            case ButtonStates.RESERVED: {
                OnButtonReserved();
                break;
            }
            case ButtonStates.SELECTED: {
                OnButtonSelected();                
                break;
            }
            case ButtonStates.SOLD:{
                OnButtonSold();
                break;
            }
        }
    }
    
    private void OnButtonSelected(){
        System.out.println("Button "+ seatNumber + " was selected");
        super.setBackground(Color.GRAY);
    }
    
    private void OnButtonFree(){
        System.out.println("Button "+ seatNumber + "  was set free");
        super.setBackground(Color.GREEN);        
    }
    
    private void OnButtonSold(){
        System.out.println("Button "+ seatNumber + "  was sold");
        super.setBackground(Color.red);      
    }
    
    private void OnButtonReserved(){
        System.out.println("Button  "+ seatNumber + " was reserved");
        super.setBackground(Color.BLUE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Execute Action Performed when button is " + currentState);
        if(currentState.equals(ButtonStates.FREE)){
            changeState(ButtonStates.SELECTED);            
            if(listener != null){
                listener.onSelect(seatNumber);
            }
        } else if (currentState.equals(ButtonStates.SELECTED)){
            JOptionPane.showMessageDialog(this, "El asiento "+ seatNumber
                    + " no esta disponible");
        } else {
            System.out.println("Else " + currentState);
        }
    }
    
    public String getCurrentState() {
        return currentState;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setStateListener(OnStateChangeListener listener) {
        this.listener = listener;
    }
    
    public interface OnStateChangeListener {
        void onSelect(int seatNumber);
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
        updateButton();
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    
}
