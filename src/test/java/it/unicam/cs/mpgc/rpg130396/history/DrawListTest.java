package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawCall;
import it.unicam.cs.mpgc.rpg130396.core.nodes.DrawList;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawListTest {
    @Test
    void sortsSpritesByLayerAndThenDepth() {
        var region = new TextureRegion(new Texture() {
            public int width() { return 8; }
            public int height() { return 8; }
        });
        var list = new DrawList();
        var foreground = new DrawCall(region, new Transform2D(), 2, 0);
        var background = new DrawCall(region, new Transform2D(), -1, 1);
        var detail = new DrawCall(region, new Transform2D(), -1, 0);
        list.add(foreground);
        list.add(background);
        list.add(detail);
        list.sort();
        assertEquals(java.util.List.of(detail, background, foreground), list.getCalls());
    }
}
