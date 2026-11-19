package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.input.InputHandler;
import it.unicam.cs.mpgc.rpg130396.core.input.InputState;
import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class Node{

    String name;
    private Node parent;
    private final List<Node> children = new ArrayList<>();


    public final Transform2D local = new Transform2D();
    private Transform2D world = new Transform2D();   
    private boolean worldDirty = true;
    List<Component> components;


    public boolean visible = true;
    public boolean active = true;        
    public int layer = 0;                
    public int zIndex = 0;

    public Node(String name){
        Objects.requireNonNull(name);
        this.name = name;
        parent = null;


    }

    public Node getParent() { return parent; }
    public List<Node> getChildren() { return Collections.unmodifiableList(children); }

    public void addChild(Node child) {
        child.parent = this;
        children.add(child);
        markDirty();
    }

    private void markDirty(){

        if(worldDirty)
            return;

        worldDirty = true;

        for(Node node : children){
            node.markDirty();
        }

    }


    public Transform2D getWorld() {
        if (worldDirty) {
            if (parent == null) world.set(local);
            else Transform2D.compose(parent.getWorld(), local, world);
            worldDirty = false;
        }
        return world;                 
    }

    public void addComponent(Component component){
        components.add(component);
    }

    public <T> List<T> getComp(Class<T> ComponentClass) {
        return components.stream()
                .filter(ComponentClass::isInstance)
                .map(ComponentClass::cast)
                .collect(Collectors.toList());
    }



    protected <T> void forEachComp(Class<T> type, Consumer<T> action) {
        getComp(type).forEach(action);
    }

    public void processInput(InputState input) {
        forEachComp(InputHandler.class, c -> c.handleInput(input));
    }

    public void update() {
        forEachComp(Updatable.class, Updatable::update);
    }

}
