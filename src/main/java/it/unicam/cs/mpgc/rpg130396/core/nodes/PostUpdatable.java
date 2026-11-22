package it.unicam.cs.mpgc.rpg130396.core.nodes;

@FunctionalInterface
public interface PostUpdatable extends Component {

    void postUpdate(double dt);
}
