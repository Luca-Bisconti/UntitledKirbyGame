package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.PostUpdatable;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Updatable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SceneTest {
    @Test
    void runsPostUpdateAfterRegularUpdate() {
        var events = new java.util.ArrayList<String>();
        var root = new Node("root") {};
        root.addComponent((PostUpdatable) delta -> events.add("post"));
        root.addComponent((Updatable) delta -> events.add("update"));
        new Scene(root, new Camera("camera", 100, 50), true).update(0.01);
        assertEquals(java.util.List.of("update", "post"), events);
    }
}
