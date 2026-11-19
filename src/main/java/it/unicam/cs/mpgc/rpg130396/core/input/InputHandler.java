package it.unicam.cs.mpgc.rpg130396.core.input;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Component;

public interface InputHandler extends Component {
    void handleInput(InputState input);
}
