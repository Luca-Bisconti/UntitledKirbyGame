
package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.input.InputHandler;
import it.unicam.cs.mpgc.rpg130396.core.input.InputState;
import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class Node {

    private String name;
    private Node parent;

    private final List<Node> children = new ArrayList<>();
    private final List<Component> components = new ArrayList<>();


    public final Transform2D local = new Transform2D();

    private final Transform2D world = new Transform2D();
    private boolean worldDirty = true;


    public boolean visible = true;
    public boolean active = true;
    public int layer = 0;
    public int zIndex = 0;

    public Node(String name) {
        this.name = Objects.requireNonNull(name);
        this.parent = null;
    }

    public String getName() {
        return name;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }





    public void addChild(Node child) {

        Objects.requireNonNull(child, "Child cannot be null");

        if (child == this)
            throw new IllegalArgumentException("A node cannot be its own child");

        if (child.parent != null)
            throw new IllegalStateException("Node already has a parent");


        Node ancestor = this;

        while (ancestor != null) {

            if (ancestor == child)
                throw new IllegalArgumentException("Adding this child would create a cycle");

            ancestor = ancestor.parent;
        }

        child.parent = this;
        children.add(child);

        child.markDirty();
    }

    private void markDirty() {

        if (worldDirty)
            return;

        worldDirty = true;

        for (Node node : children) {
            node.markDirty();
        }
    }





    public Transform2D getWorld() {

        if (worldDirty) {

            if (parent == null)
                world.set(local);
            else
                Transform2D.compose(parent.getWorld(), local, world);

            worldDirty = false;
        }

        return world;
    }





    public void addComponent(Component component) {
        components.add(Objects.requireNonNull(component));
    }

    public <T> List<T> getComp(Class<T> componentClass) {

        return components.stream()
                .filter(componentClass::isInstance)
                .map(componentClass::cast)
                .collect(Collectors.toList());
    }

    protected <T> void forEachComp(Class<T> type, Consumer<T> action) {

        getComp(type).forEach(action);
    }





    public void processInput(InputState input) {

        if (!active)
            return;

        forEachComp(InputHandler.class, c -> c.handleInput(input));



        List<Node> childrenSnapshot = new ArrayList<>(children);

        for (Node child : childrenSnapshot) {
            child.processInput(input);
        }
    }

    public void update() {

        if (!active)
            return;

        forEachComp(Updatable.class, Updatable::update);

        List<Node> childrenSnapshot = new ArrayList<>(children);

        for (Node child : childrenSnapshot) {
            child.update();
        }
    }
}
