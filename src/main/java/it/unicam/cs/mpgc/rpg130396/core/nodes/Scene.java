
package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.input.InputState;

import java.util.Objects;

public class Scene {

    private final Node root;
    private final Camera camera;
    private final DrawCollector drawCollector;

    public boolean isActive;

    public Scene(Node root, Camera camera, boolean isActive) {
        this.root = Objects.requireNonNull(root);
        this.camera = Objects.requireNonNull(camera);
        this.isActive = isActive;
        this.drawCollector = new DrawCollector();
    }

    public Node root() {
        return root;
    }

    public Camera camera() {
        return camera;
    }

    public DrawList collectDrawList() {
        if (!isActive) {
            return new DrawList();
        }

        return drawCollector.collect(this);
    }

    public void processInput(InputState input) {
        if (isActive) {
            root.processInput(input);
        }
    }

    public void update(double dt) {
        Updatable.requireValidDeltaTime(dt);
        if (isActive) {
            root.update(dt);
            root.postUpdate(dt);
        }
    }
}
