package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.engine.GameLoop;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLoopTest {
    @Test
    void startsAndStopsTheWindowLifecycle() {
        var calls = new java.util.ArrayList<String>();
        var window = (Window) java.lang.reflect.Proxy.newProxyInstance(
                Window.class.getClassLoader(), new Class<?>[] { Window.class },
                (proxy, method, args) -> {
                    calls.add(method.getName());
                    return null;
                });
        var camera = new Camera("camera", 16, 9);
        var target = new RenderTestSupport.Target(16, 9);
        var renderer = new Renderer(new RenderTestSupport.Context(), target, java.util.List.of());
        var loop = new GameLoop(window, renderer, new Scene(new Node("root") {}, camera, true), new PolledInput());
        loop.start();
        loop.stop();
        assertEquals(java.util.List.of("setFrameListener", "open", "close"), calls);
    }
}
