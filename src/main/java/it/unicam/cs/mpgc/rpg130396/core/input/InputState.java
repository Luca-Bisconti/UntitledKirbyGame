package it.unicam.cs.mpgc.rpg130396.core.input;


public interface InputState {
    boolean isDown(Action a);      
    boolean isPressed(Action a);   
    boolean isReleased(Action a);  
}