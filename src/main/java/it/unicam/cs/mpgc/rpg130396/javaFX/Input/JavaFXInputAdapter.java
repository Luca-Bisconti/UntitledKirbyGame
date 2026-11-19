package it.unicam.cs.mpgc.rpg130396.javaFX.Input;

import it.unicam.cs.mpgc.rpg130396.core.input.Action;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.EnumMap;
import java.util.Map;

public class JavaFXInputAdapter {

    private final Map<KeyCode, Action> bindings =
            new EnumMap<>(KeyCode.class);

    public JavaFXInputAdapter(Scene scene, PolledInput input) {

        bindings.put(KeyCode.LEFT, Action.LEFT);
        bindings.put(KeyCode.RIGHT, Action.RIGHT);
        bindings.put(KeyCode.UP, Action.UP);
        bindings.put(KeyCode.DOWN, Action.DOWN);
        bindings.put(KeyCode.Z, Action.JUMP);
        bindings.put(KeyCode.X, Action.ATTACK);

        scene.setOnKeyPressed(e -> {
            Action action = bindings.get(e.getCode());

            if (action != null)
                input.press(action);
        });

        scene.setOnKeyReleased(e -> {
            Action action = bindings.get(e.getCode());

            if (action != null)
                input.release(action);
        });
    }
}