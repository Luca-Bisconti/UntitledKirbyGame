package it.unicam.cs.mpgc.rpg130396.javaFX.Input;

import it.unicam.cs.mpgc.rpg130396.core.input.Action;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import javafx.scene.input.KeyCode;

import java.util.EnumMap;
import java.util.Map;

public class JavaFXInputAdapter {

    private final Map<KeyCode, Action> bindings = new EnumMap<>(KeyCode.class);

    public JavaFXInputAdapter(Scene scene, PolledInput input) {
        bindings.put(KeyCode.LEFT,  Action.LEFT);
        bindings.put(KeyCode.RIGHT, Action.RIGHT);
        bindings.put(KeyCode.UP,    Action.UP);
        bindings.put(KeyCode.DOWN,  Action.DOWN);
        bindings.put(KeyCode.Z,     Action.JUMP);
        bindings.put(KeyCode.X,     Action.ATTACK);

        scene.setOnKeyPressed(e -> {
            Action a = bindings.get(e.getCode());
            if (a != null) input.press(a);
        });
        scene.setOnKeyReleased(e -> {
            Action a = bindings.get(e.getCode());
            if (a != null) input.release(a);
        });
    }
}