
package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.input.InputHandler;
import it.unicam.cs.mpgc.rpg130396.core.input.InputState;
import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class Node {

    private final String name;
    private Node parent;

    private final List<Node> children = new ArrayList<>();
    private final List<Component> components = new ArrayList<>();


    private Transform2D local = new Transform2D();
    private Transform2D world;


    public boolean visible = true;
    public boolean active = true;
    public int layer = 0;
    public int zIndex = 0;

    public Node(String name) {
        this.name = Objects.requireNonNull(name);
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

        if (child == this) {
            throw new IllegalArgumentException(
                    "A node cannot be its own child"
            );
        }

        if (child.parent != null) {
            throw new IllegalStateException(
                    "Node already has a parent"
            );
        }


        Node ancestor = this;

        while (ancestor != null) {

            if (ancestor == child) {
                throw new IllegalArgumentException(
                        "Adding this child would create a cycle"
                );
            }

            ancestor = ancestor.parent;
        }

        children.add(child);
        child.parent = this;

        child.markDirty();
    }

    public boolean removeChild(Node child) {

        if (child == null || !children.remove(child)) {
            return false;
        }

        child.parent = null;
        child.markDirty();

        return true;
    }





    private void markDirty() {

        world = null;


        for (Node child : children) {
            child.markDirty();
        }
    }

    public final Transform2D getLocal() {
        return local;
    }

    public final void setLocal(Transform2D transform) {
        local = Objects.requireNonNull(transform);
        markDirty();
    }


    public final Transform2D getWorld() {
        if (world == null) {
            world = parent == null ? local : Transform2D.compose(parent.getWorld(), local);
        }
        return world;
    }





    public void addComponent(Component component) {
        components.add(
                Objects.requireNonNull(component)
        );
    }

    public boolean removeComponent(Component component) {
        return components.remove(component);
    }

    public <T> List<T> getComp(Class<T> componentClass) {
        Objects.requireNonNull(componentClass);
        List<T> result = new ArrayList<>();

        for (Component component : components) {

            if (componentClass.isInstance(component)) {
                result.add(componentClass.cast(component));
            }
        }

        return List.copyOf(result);
    }

    protected <T> void forEachComp(
            Class<T> type,
            Consumer<T> action
    ) {

        List<T> snapshot = getComp(type);

        for (T component : snapshot) {
            action.accept(component);
        }
    }





    public void processInput(InputState input) {

        if (!active) {
            return;
        }


        forEachComp(
                InputHandler.class,
                component -> component.handleInput(input)
        );


        List<Node> childrenSnapshot = new ArrayList<>(children);

        for (Node child : childrenSnapshot) {



            if (child.parent == this) {
                child.processInput(input);
            }
        }
    }

    public void update() {

        if (!active) {
            return;
        }


        forEachComp(
                Updatable.class,
                Updatable::update
        );


        List<Node> childrenSnapshot = new ArrayList<>(children);

        for (Node child : childrenSnapshot) {

            if (child.parent == this) {
                child.update();
            }
        }
    }
}
