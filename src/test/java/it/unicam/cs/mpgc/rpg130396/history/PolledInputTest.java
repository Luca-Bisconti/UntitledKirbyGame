package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.input.Action;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolledInputTest {
    @Test
    void reportsPressAndReleaseInTheCorrespondingFrames() {
        var input = new PolledInput();
        input.press(Action.LEFT);
        assertTrue(input.isPressed(Action.LEFT));
        input.endFrame();
        assertTrue(input.isDown(Action.LEFT));
        assertFalse(input.isPressed(Action.LEFT));
        input.release(Action.LEFT);
        assertTrue(input.isReleased(Action.LEFT));
    }

    @Test
    void clearsEdgesWithoutLosingHeldActions() {
        var input = new PolledInput();
        input.press(Action.LEFT);
        input.endFrame();
        input.press(Action.LEFT);
        assertTrue(input.isDown(Action.LEFT));
        assertFalse(input.isPressed(Action.LEFT));
        input.releaseAll();
        assertFalse(input.isDown(Action.LEFT));
        assertTrue(input.isReleased(Action.LEFT));
        input.endFrame();
        assertFalse(input.isReleased(Action.LEFT));
    }
}
