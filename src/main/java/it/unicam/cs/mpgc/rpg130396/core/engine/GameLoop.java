package it.unicam.cs.mpgc.rpg130396.core.engine;

import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.nodes.DrawList;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;

import java.util.Objects;

public final class GameLoop {

    private static final double MAX_DELTA_SECONDS = 0.05;

    private boolean hasTimestamp;
    private long previousTimestamp;
    private final Renderer renderer;
    private final Scene scene;
    private final PolledInput input;

    public GameLoop(Renderer renderer, Scene scene, PolledInput input) {
        this.renderer = Objects.requireNonNull(renderer);
        this.scene = Objects.requireNonNull(scene);
        this.input = Objects.requireNonNull(input);
    }

    public void update(long now) {
        if (!hasTimestamp) {
            previousTimestamp = now;
            hasTimestamp = true;
            return;
        }
        double dt = Math.clamp((now - previousTimestamp) / 1_000_000_000.0, 0, MAX_DELTA_SECONDS);
        previousTimestamp = now;
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
