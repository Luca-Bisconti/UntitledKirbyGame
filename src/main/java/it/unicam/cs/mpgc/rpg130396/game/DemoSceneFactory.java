package it.unicam.cs.mpgc.rpg130396.game;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.CameraFollowComponent;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.ParallaxComponent;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.nodes.SpriteAnimator;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAtlas;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

import java.util.List;
import java.util.Objects;


public final class DemoSceneFactory {

    public static final int WIDTH = 240;
    public static final int HEIGHT = 135;
    public static final int WORLD_WIDTH = 720;

    private static final int CHARACTER_HALF_WIDTH = 13;
    private static final int CHARACTER_FOOT_OFFSET = 9;

    public record Background(Texture texture, float factor) {
        public Background {
            Objects.requireNonNull(texture);
            if (!Float.isFinite(factor)) {
                throw new IllegalArgumentException("Parallax factor must be finite");
            }
            double requiredWidth = WIDTH + Math.abs((double) factor) * (WORLD_WIDTH - WIDTH);
            if (texture.width() < requiredWidth || texture.height() < HEIGHT) {
                throw new IllegalArgumentException("Background texture must cover the full camera travel");
            }
        }
    }


    public Scene create(List<Background> backgrounds, Texture groundTexture, SpriteAtlas atlas) {
        List<Background> planes = List.copyOf(backgrounds);
        Objects.requireNonNull(groundTexture);
        Objects.requireNonNull(atlas);

        Node root = new Node("Root") {};
        float worldHalfWidth = WORLD_WIDTH / 2f;

        float groundTop = HEIGHT / 2f - groundTexture.height();
        Sprite ground = new Sprite("Ground").setRegion(new TextureRegion(groundTexture));
        ground.setLayer(1);
        ground.setLocal(ground.getLocal().withPosition(-worldHalfWidth, groundTop));

        Sprite character = new Sprite("Character");
        character.setLayer(2);
        float playerGroundY = groundTop - CHARACTER_FOOT_OFFSET;
        character.setLocal(character.getLocal().withPosition(0, playerGroundY));
        SpriteAnimator animator = new SpriteAnimator(character);
        SpriteAnimation idle = new SpriteAnimation(List.of(atlas.frame(0)), 0.125, true);
        SpriteAnimation walk = new SpriteAnimation(
                List.of(atlas.frame(0), atlas.frame(1), atlas.frame(2), atlas.frame(1)), 0.125, true);
        float horizontalLimit = worldHalfWidth - CHARACTER_HALF_WIDTH;
        character.addComponent(new PlayerController(character, animator, idle, walk,
                -horizontalLimit, horizontalLimit, playerGroundY));
        character.addComponent(animator);
        Camera camera = new Camera("MainCamera", WIDTH, HEIGHT);
        camera.addComponent(new CameraFollowComponent(camera, character, 0, -worldHalfWidth, worldHalfWidth));


        root.addChild(character);
        root.addChild(camera);
        for (int i = 0; i < planes.size(); i++) {
            Background plane = planes.get(i);
            Texture texture = plane.texture();
            Sprite background = new Sprite("Background " + (i + 1)).setRegion(new TextureRegion(texture));
            background.setLayer(0);
            background.setZIndex(i);
            background.setLocal(background.getLocal().withPosition(-texture.width() / 2f, -HEIGHT / 2f));
            root.addChild(background);
            background.addComponent(new ParallaxComponent(background, camera, plane.factor()));
        }
        root.addChild(ground);
        return new Scene(root, camera, true);
    }
}
