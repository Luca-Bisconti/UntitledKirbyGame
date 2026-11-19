package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.DrawCollector;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawCollectorTest {
    @Test
    void collectsOnlySpritesInsideTheCamera() {
        var texture = new Texture() {
            public int width() { return 8; }
            public int height() { return 8; }
        };
        var root = new Node("root") {};
        var inside = new Sprite("inside").setRegion(new TextureRegion(texture));
        var outside = new Sprite("outside").setRegion(new TextureRegion(texture));
        outside.setLocal(outside.getLocal().withPosition(200, 0));
        root.addChild(inside);
        root.addChild(outside);
        var camera = new Camera("camera", 100, 50);
        var calls = new DrawCollector().collect(new Scene(root, camera, true)).getCalls();
        assertEquals(1, calls.size());
        assertSame(inside.getRegion(), calls.getFirst().textureRegion());
    }
}
