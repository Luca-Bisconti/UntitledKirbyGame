package it.unicam.cs.mpgc.rpg130396.game;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.nodes.SpriteAnimator;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Compositor;
import it.unicam.cs.mpgc.rpg130396.core.rendering.PassBinding;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAtlas;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureLoader;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureStore;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXRenderTarget;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTextureLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DemoSceneFactory {

    private static final int BRIDGE_SEGMENTS = 6;
    private static final int BRIDGE_WIDTH = 224;
    private static final int BRIDGE_ORIGIN_Y = 226;
    private static final float BRIDGE_Y = 170;
    private static final float BRIDGE_SCALE = 0.55f;
    private static final int BRIDGE_SURFACE_Y = 132;

    private DemoSceneFactory() {}

    public static Scene create(Renderer renderer, RenderTarget screen) {


        int logicalWidth = 320;
        int logicalHeight = 180;


        Camera camera = new Camera(
                "MainCamera",
                logicalWidth,
                logicalHeight
        );


        camera.setAnchor(0, 0);


        Node root = new Node("Root") {};
        Scene scene = new Scene(root, camera, true);


        TextureLoader loader = new JavaFXTextureLoader();
        TextureStore textures = new TextureStore(loader);

        String bridgePath = "/assets/environment/bridge.png";

        textures.load(bridgePath);

        TextureRegion bridgeRegion = new TextureRegion(
                textures.get(bridgePath)
        );

        bridgeRegion.setOrigin(0, BRIDGE_ORIGIN_Y);



        Node bridge = createBridge(
                bridgeRegion,
                BRIDGE_SEGMENTS,
                BRIDGE_SCALE
        );

        bridge.setLocal(
                bridge.getLocal().withPosition(0, BRIDGE_Y)
        );

        root.addChild(bridge);

        String kirbyPath = "/assets/kirby/walk.png";
        textures.load(kirbyPath);
        Sprite kirby = createKirby(textures.get(kirbyPath));
        root.addChild(kirby);


        RenderTarget bridgeTarget =
                new JavaFXRenderTarget(640, 360);
        RenderTarget kirbyTarget =
                new JavaFXRenderTarget(320, 180);


        renderer.configureLayers(
                Map.of(
                        0, bridgeTarget,
                        1, kirbyTarget
                ),
                screen,
                logicalWidth,
                logicalHeight,
                List.of(
                        new PassBinding(
                                new Compositor(),
                                bridgeTarget,
                                screen,
                                true
                        ),
                        new PassBinding(
                                new Compositor(),
                                kirbyTarget,
                                screen,
                                false
                        )
                )
        );

        return scene;
    }


    private static Sprite createKirby(Texture texture) {
        SpriteAtlas atlas = new SpriteAtlas(texture, 26, 23, 13, 23);
        List<TextureRegion> frames = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            frames.add(atlas.frame(i));
        }

        TextureRegion firstFrame = frames.getFirst();
        SpriteAnimation walk = new SpriteAnimation(frames, 0.12, true);
        SpriteAnimation idle = new SpriteAnimation(List.of(firstFrame), 0.12, false);

        float groundY = BRIDGE_Y + (BRIDGE_SURFACE_Y - BRIDGE_ORIGIN_Y) * BRIDGE_SCALE;
        float bridgeWidth = BRIDGE_SEGMENTS * BRIDGE_WIDTH * BRIDGE_SCALE;
        float minX = firstFrame.originX();
        float maxX = bridgeWidth - (firstFrame.width() - firstFrame.originX());

        Sprite kirby = new Sprite("Kirby");
        kirby.setLayer(1);
        kirby.setLocal(kirby.getLocal().withPosition(40, groundY));

        SpriteAnimator animator = new SpriteAnimator(kirby);
        kirby.addComponent(new PlayerController(kirby, animator, idle, walk, null,
                minX, maxX, groundY, true));
        kirby.addComponent(animator);
        return kirby;
    }

    private static Node createBridge(
            TextureRegion region,
            int segments,
            float scale
    ) {
        Node bridge = new Node("Bridge") {};

        float segmentWidth = region.width();

        for (int i = 0; i < segments; i++) {

            Sprite segment = new Sprite("Bridge_" + i)
                    .setRegion(region);

            segment.setLayer(0);

            segment.setLocal(
                    segment.getLocal()
                            .withPosition(i * segmentWidth, 0)
            );

            bridge.addChild(segment);
        }

        bridge.setLocal(
                bridge.getLocal().withScale(scale)
        );

        return bridge;
    }


}
