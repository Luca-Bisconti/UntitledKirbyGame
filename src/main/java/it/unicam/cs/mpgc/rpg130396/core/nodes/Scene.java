package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.input.InputState;

public class Scene {

    private final Node root;
    private final Camera camera;
    public boolean isActive;

    public Scene(Node root, Camera camera, boolean isActive) {
        this.root = root;
        this.camera = camera;
        this.isActive = isActive;
    }

    public Node root(){
        return root;
    }

    public Camera camera(){
        return camera;
    }

    public void processInput(InputState input) { root.processInput(input); }
    public void update()                       { root.update(); }
}