package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.ParallaxComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParallaxComponentTest {
    @Test
    void offsetsTheBackgroundByAFractionOfCameraMovement() {
        var camera = new Camera("camera", 100, 50);
        var background = new Node("background") {};
        background.setLocal(background.getLocal().withPosition(10, 4));
        var parallax = new ParallaxComponent(background, camera, 0.25f);
        camera.setLocal(camera.getLocal().withPosition(80, 0));
        parallax.updateFromCamera(0);
        assertEquals(70f, background.getWorld().posX());
        assertEquals(4f, background.getWorld().posY());
    }
}
