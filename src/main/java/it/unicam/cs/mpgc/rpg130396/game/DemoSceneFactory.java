package it.unicam.cs.mpgc.rpg130396.game;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.CameraFollowComponent;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.ParallaxComponent;
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
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXNativeBloomPass;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXRenderTarget;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTextureLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DemoSceneFactory {

    private static final int SKY_LAYER = -3;
    private static final int MOON_LAYER = -2;
    private static final int CLOSE_MOUNTAINS_LAYER = -1;
    private static final int BRIDGE_SEGMENTS = 6;
    private static final int BRIDGE_WIDTH = 224;
    private static final int BRIDGE_ORIGIN_Y = 226;
    private static final float BRIDGE_Y = 170;
    private static final float BRIDGE_SCALE = 0.5f;
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

        String skyPath = "/assets/environment/skySheet.png";
        textures.load(skyPath);
        Sprite sky = createBackdrop(textures.get(skyPath));
        root.addChild(sky);

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

        float levelWidth = BRIDGE_SEGMENTS * BRIDGE_WIDTH * BRIDGE_SCALE;
        CameraFollowComponent cameraFollow = new CameraFollowComponent(
                camera,
                kirby,
                -logicalWidth / 2f,
                0f,
                levelWidth
        );
        cameraFollow.addParallax(new ParallaxComponent(sky, camera, 0f));
        String moonPath = "/assets/environment/moon.png";
        textures.load(moonPath);
        addMoon(root, textures.get(moonPath), camera, cameraFollow);
        String mountainsPath = "/assets/environment/closeM.png";
        textures.load(mountainsPath);
        addCloseMountains(root, textures.get(mountainsPath), camera, cameraFollow);
        root.addComponent(cameraFollow);

        RenderTarget skyTarget =
                new JavaFXRenderTarget(640, 360);
        RenderTarget skyBloomTarget =
                new JavaFXRenderTarget(640, 360);
        RenderTarget moonTarget =
                new JavaFXRenderTarget(640, 360);
        RenderTarget moonBloomTarget =
                new JavaFXRenderTarget(640, 360);
        RenderTarget closeMountainsTarget =
                new JavaFXRenderTarget(640, 360);
        RenderTarget bridgeTarget =
                new JavaFXRenderTarget(640, 360);
        RenderTarget kirbyTarget =
                new JavaFXRenderTarget(320, 180);

        double skyBloomThreshold = 0.65;
        double skyBloomIntensity = 2;
        JavaFXNativeBloomPass skyBloom = new JavaFXNativeBloomPass(skyBloomThreshold, skyBloomIntensity);
        double moonBloomThreshold = 0.8;
        double moonBloomIntensity = 1.1f;
        JavaFXNativeBloomPass moonBloom = new JavaFXNativeBloomPass(moonBloomThreshold, moonBloomIntensity);

        renderer.configureLayers(
                Map.of(
                        SKY_LAYER, skyTarget,
                        MOON_LAYER, moonTarget,
                        CLOSE_MOUNTAINS_LAYER, closeMountainsTarget,
                        0, bridgeTarget,
                        1, kirbyTarget
                ),
                screen,
                logicalWidth,
                logicalHeight,
                List.of(
                        new PassBinding(
                                skyBloom,
                                skyTarget,
                                skyBloomTarget,
                                true
                        ),
                        new PassBinding(
                                new Compositor(),
                                skyBloomTarget,
                                screen,
                                true
                        ),
                        new PassBinding(
                                moonBloom,
                                moonTarget,
                                moonBloomTarget,
                                true
                        ),
                        new PassBinding(
                                new Compositor(),
                                moonBloomTarget,
                                screen,
                                false
                        ),
                        new PassBinding(
                                new Compositor(),
                                closeMountainsTarget,
                                screen,
                                false
                        ),
                        new PassBinding(
                                new Compositor(),
                                bridgeTarget,
                                screen,
                                false
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


    private static Sprite createBackdrop(Texture texture) {
        SpriteAtlas atlas = new SpriteAtlas(texture, 640, 360, 0, 0);
        int frameCount = 12;
        List<TextureRegion> frames = new ArrayList<>(2 * frameCount - 2);
        for (int i = 0; i < frameCount; i++) {
            frames.add(atlas.frame(i));
        }
        for (int i = frameCount - 2; i > 0; i--) {
            frames.add(frames.get(i));
        }

        Sprite sky = new Sprite("Sky");
        sky.setLayer(SKY_LAYER);
        sky.setLocal(sky.getLocal().withScale(0.5f));
        SpriteAnimator animator = new SpriteAnimator(sky);
        sky.addComponent(animator);
        animator.play(new SpriteAnimation(frames, 0.1, true));
        return sky;
    }

    private static Sprite addMoon(Node root, Texture texture, Camera camera,
                                  CameraFollowComponent cameraFollow) {
        TextureRegion region = new TextureRegion(texture).setOrigin(0, 0);
        Sprite moon = new Sprite("Moon").setRegion(region);
        moon.setLayer(MOON_LAYER);
        float moonX = 200f;
        float moonY = 15f;
        float moonScale = 1f;
        moon.setLocal(moon.getLocal().withPosition(moonX, moonY).withScale(moonScale));
        root.addChild(moon);

        float moonParallax = 0f;
        ParallaxComponent parallax = new ParallaxComponent(moon, camera, moonParallax);
        moon.addComponent(parallax);
        cameraFollow.addParallax(parallax);
        return moon;
    }

    private static Sprite addCloseMountains(Node root, Texture texture, Camera camera,
                                           CameraFollowComponent cameraFollow) {
        TextureRegion region = new TextureRegion(texture);
        region.setOrigin(0, region.height());
        Sprite mountains = new Sprite("CloseMountains").setRegion(region);
        mountains.setLayer(CLOSE_MOUNTAINS_LAYER);
        float mountainsX = 0f;
        float mountainsY = 180f;
        float mountainsScale = 1f;
        mountains.setLocal(mountains.getLocal()
                .withPosition(mountainsX, mountainsY)
                .withScale(mountainsScale));
        root.addChild(mountains);

        float mountainsParallax = 0.2f;
        ParallaxComponent parallax = new ParallaxComponent(mountains, camera, mountainsParallax);
        mountains.addComponent(parallax);
        cameraFollow.addParallax(parallax);
        return mountains;
    }

    private static Sprite createKirby(Texture texture) {
        SpriteAtlas atlas = new SpriteAtlas(texture, 26, 23, 13, 23);
        List<TextureRegion> frames = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            frames.add(atlas.frame(i));
        }

        TextureRegion firstFrame = frames.getFirst();
        SpriteAnimation walk = new SpriteAnimation(frames, 0.12, true);
        SpriteAnimation idle = new SpriteAnimation(List.of(frames.get(8)), 0.12, false);

        float groundY = 128;
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
