package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.game.DemoSceneFactory;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXDrawContext;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXRenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemoSceneFactoryTest {
    @Test
    void placesThePlayerAndParallaxLayersInTheirInitialPoses() {
        var screen = new JavaFXRenderTarget(320, 180);
        var renderer = new Renderer(new JavaFXDrawContext(screen), screen, java.util.List.of());
        var scene = DemoSceneFactory.create(renderer, screen);
        var children = scene.root().getChildren();
        var player = (Sprite) children.get(2);
        var moon = children.get(3);
        var mountains = children.get(4);
        assertEquals(128f, player.getLocal().posY());
        assertEquals(8 * 26, player.getRegion().posX());
        assertEquals(200f, moon.getLocal().posX());
        assertEquals(15f, moon.getLocal().posY());
        assertEquals(1f, mountains.getLocal().scale());

        player.setLocal(player.getLocal().withPosition(250, 128));
        scene.update(0);
        assertEquals(90f, scene.camera().getWorld().posX(), 0.0001f);
        assertEquals(200f, scene.camera().toScreen(moon.getWorld()).posX(), 0.0001f);
        assertEquals(-18f, scene.camera().toScreen(mountains.getWorld()).posX(), 0.0001f);
    }
}
