package it.unicam.cs.mpgc.rpg130396.core.engine;

import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.nodes.DrawList;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;

import java.util.Objects;


public final class GameLoop {

    private final Window window;
    private final Renderer renderer;
    private final Scene scene;
    private final PolledInput input;

    public GameLoop(Window window, Renderer renderer, Scene scene, PolledInput input) {
        this.window = Objects.requireNonNull(window);
        this.renderer = Objects.requireNonNull(renderer);
        this.scene = Objects.requireNonNull(scene);
        this.input = Objects.requireNonNull(input);
    }

    public void start() {
        window.setFrameListener(this::onFrame);
        window.open();
    }

    public void stop() {
        window.close();
    }

    private void onFrame(double dt) {
        try {
            scene.processInput(input);
            scene.update(dt);
            DrawList drawList = scene.collectDrawList();
            renderer.render(drawList);
        } finally {
            input.endFrame();
        }
    }
}
