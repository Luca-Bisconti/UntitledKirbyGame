package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTextureLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemoAssetsTest {
    @Test
    void loadsAllProductionTexturesFromTheClasspath() {
        var loader = new JavaFXTextureLoader();
        assertEquals(4096, loader.load("/assets/environment/skySheet.png").width());
        assertEquals(248, loader.load("/assets/environment/bridge.png").width());
        assertEquals(64, loader.load("/assets/environment/moon.png").width());
        assertEquals(640, loader.load("/assets/environment/closeM.png").width());
        assertEquals(260, loader.load("/assets/kirby/walk.png").width());
    }
}
