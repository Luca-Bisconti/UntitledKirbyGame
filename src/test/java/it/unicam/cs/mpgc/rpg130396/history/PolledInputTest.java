package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.input.Action;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolledInputTest {
    @Test
    void reportsPressAndReleaseOnTheCorrespondingPolls() {
        var input = new PolledInput();
        input.press(Action.LEFT);
        input.poll();
        assertTrue(input.isPressed(Action.LEFT));
        input.poll();
        assertTrue(input.isDown(Action.LEFT));
        assertFalse(input.isPressed(Action.LEFT));
        input.release(Action.LEFT);
        input.poll();
        assertTrue(input.isReleased(Action.LEFT));
    }
}
