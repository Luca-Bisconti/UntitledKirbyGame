package it.unicam.cs.mpgc.rpg130396.core.nodes;

public interface Updatable extends Component {


    void update(double dt);

    static void requireValidDeltaTime(double dt) {
        if (!Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException("Delta time must be finite and non-negative");
        }
    }

}
