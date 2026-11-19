package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    @Test
    void preventsCyclesAndDetachesChildren() {
        var parent = new Node("parent") {};
        var child = new Node("child") {};
        parent.addChild(child);
        assertSame(parent, child.getParent());
        assertThrows(IllegalArgumentException.class, () -> child.addChild(parent));
        assertTrue(parent.removeChild(child));
        assertNull(child.getParent());
        assertTrue(parent.getChildren().isEmpty());
    }
}
