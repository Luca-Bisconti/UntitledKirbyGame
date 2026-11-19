package it.unicam.cs.mpgc.rpg130396.core.input;

import java.util.EnumSet;
import java.util.Set;


public class PolledInput implements InputState {

    private final Set<Action> live = EnumSet.noneOf(Action.class);     
    private Set<Action> current = EnumSet.noneOf(Action.class);        
    private Set<Action> previous = EnumSet.noneOf(Action.class);


    public void press(Action a)   { live.add(a); }
    public void release(Action a) { live.remove(a); }


    public void poll() {
        previous = current;
        current = EnumSet.copyOf(live);
    }

    @Override public boolean isDown(Action a)     { return current.contains(a); }
    @Override public boolean isPressed(Action a)  { return current.contains(a) && !previous.contains(a); }
    @Override public boolean isReleased(Action a) { return !current.contains(a) && previous.contains(a); }
}