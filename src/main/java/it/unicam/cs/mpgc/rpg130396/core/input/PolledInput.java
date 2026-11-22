
package it.unicam.cs.mpgc.rpg130396.core.input;

import java.util.EnumSet;
import java.util.Set;

public class PolledInput implements InputState {

    private final Set<Action> down =
            EnumSet.noneOf(Action.class);

    private final Set<Action> pressed =
            EnumSet.noneOf(Action.class);

    private final Set<Action> released =
            EnumSet.noneOf(Action.class);

    public void press(Action action) {

        if (down.add(action)) {
            pressed.add(action);
            released.remove(action);
        }
    }

    public void release(Action action) {

        if (down.remove(action)) {
            released.add(action);
            pressed.remove(action);
        }
    }

    public void endFrame() {
        pressed.clear();
        released.clear();
    }

    public void releaseAll() {

        for (Action action : down) {
            released.add(action);
        }

        down.clear();
        pressed.clear();
    }

    @Override
    public boolean isDown(Action action) {
        return down.contains(action);
    }

    @Override
    public boolean isPressed(Action action) {
        return pressed.contains(action);
    }

    @Override
    public boolean isReleased(Action action) {
        return released.contains(action);
    }
}
