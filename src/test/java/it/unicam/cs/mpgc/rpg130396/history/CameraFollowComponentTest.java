package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.CameraFollowComponent;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CameraFollowComponentTest {
    @Test
    void clampsTheViewAtTheEndOfTheLevel() {
        var camera = new Camera("camera", 100, 50);
        camera.setAnchor(0, 0);
        var player = new Node("player") {};
        player.setLocal(player.getLocal().withPosition(500, 0));
        var follow = new CameraFollowComponent(camera, player, 0, 0, 300);
        follow.postUpdate(0);
        assertEquals(200f, camera.viewRect(0).x());
    }
}
