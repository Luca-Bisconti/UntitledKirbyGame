package it.unicam.cs.mpgc.rpg130396.game;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.CameraFollowComponent;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.ParallaxComponent;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.nodes.SpriteAnimator;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

import java.util.List;

import static it.unicam.cs.mpgc.rpg130396.game.DemoConfig.*;


public final class DemoSceneFactory {

    public Scene create(DemoAssets assets) {
        if (assets.bridge().width() != BRIDGE_TEXTURE_WIDTH
                || assets.bridge().height() != BRIDGE_TEXTURE_HEIGHT) {
            throw new IllegalArgumentException("Expected a bridge texture of "
                    + BRIDGE_TEXTURE_WIDTH + "x" + BRIDGE_TEXTURE_HEIGHT);
        }

        Node root = new Node("Root") {};
        Camera camera = new Camera("MainCamera", LOGICAL_WIDTH, LOGICAL_HEIGHT);
        Node backgrounds = new Node("Backgrounds") {};
        Node environment = new Node("Environment") {};
        Sprite kirby = new Sprite("Kirby");
        root.addChild(camera);
        root.addChild(backgrounds);
        root.addChild(environment);
        root.addChild(kirby);

        kirby.setLayer(2);
        kirby.setLocal(kirby.getLocal().withPosition(0, GROUND_Y));
        SpriteAnimator animator = new SpriteAnimator(kirby);
        float margin = playerMargin(assets);
        kirby.addComponent(new PlayerController(kirby, animator, assets.idle(), assets.walk(), assets.crouch(),
                WORLD_MIN_X + margin, WORLD_MAX_X - margin, GROUND_Y));
        kirby.addComponent(animator);

        CameraFollowComponent follow = new CameraFollowComponent(camera, kirby, 0, WORLD_MIN_X, WORLD_MAX_X);
        camera.addComponent(follow);

        for (int i = 0; i < assets.backgrounds().size(); i++) {
            DemoAssets.Background plane = assets.backgrounds().get(i);
            validateCoverage(plane);
            Sprite background = new Sprite(plane.name()).setRegion(new TextureRegion(plane.texture()));
            background.setLayer(0);
            background.setZIndex(i);
            background.setLocal(background.getLocal().withPosition(
                    plane.centerX() - plane.texture().width() / 2f, plane.centerY() - plane.texture().height() / 2f));
            backgrounds.addChild(background);
            ParallaxComponent parallax = new ParallaxComponent(background, camera, plane.factor());
            background.addComponent(parallax);
            follow.addParallax(parallax);
        }

        for (int i = 0; i < BRIDGE_SEGMENTS; i++) {
            TextureRegion region = new TextureRegion(assets.bridge()).setOrigin(0, BRIDGE_WALKABLE_Y);
            Sprite bridge = new Sprite("Bridge_" + i).setRegion(region);
            bridge.setLayer(1);
            bridge.setLocal(bridge.getLocal().withPosition(WORLD_MIN_X + i * SEGMENT_WORLD_WIDTH, GROUND_Y)
                    .withScale(BRIDGE_SCALE));
            environment.addChild(bridge);
        }

        return new Scene(root, camera, true);
    }

    private static void validateCoverage(DemoAssets.Background plane) {
        if (!plane.coversViewport()) {
            return;
        }
        double requiredWidth = LOGICAL_WIDTH + Math.abs((double) plane.factor()) * (WORLD_WIDTH - LOGICAL_WIDTH)
                + 2.0 * Math.abs(plane.centerX());
        double requiredHeight = LOGICAL_HEIGHT + 2.0 * Math.abs(plane.centerY());
        if (plane.texture().width() < requiredWidth || plane.texture().height() < requiredHeight) {
            throw new IllegalArgumentException(plane.name() + " must cover the viewport across camera travel (at least "
                    + (int) Math.ceil(requiredWidth) + "x" + (int) Math.ceil(requiredHeight) + ")");
        }
    }

    private static float playerMargin(DemoAssets assets) {
        float margin = 0;
        for (SpriteAnimation animation : List.of(assets.idle(), assets.walk(), assets.crouch())) {
            for (TextureRegion frame : animation.frames()) {
                margin = Math.max(margin, Math.max(frame.originX(), (float) frame.width() - frame.originX()));
            }
        }
        if (margin >= WORLD_WIDTH / 2) {
            throw new IllegalArgumentException("Kirby frames are too wide for the bridge");
        }
        return margin;
    }
}
