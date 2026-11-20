package it.unicam.cs.mpgc.rpg130396.game;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
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

    private static final int CHARACTER_HALF_WIDTH = 13;
    private static final int CHARACTER_FOOT_OFFSET = 9;

    public Scene create(Texture backgroundTexture, Texture groundTexture, SpriteAtlas atlas) {
        Objects.requireNonNull(backgroundTexture);
        Objects.requireNonNull(groundTexture);
        Objects.requireNonNull(atlas);

        Node root = new Node("Root") {};

        Sprite background = new Sprite("Background").setRegion(new TextureRegion(backgroundTexture));
        background.setLayer(0);
        background.setLocal(background.getLocal().withPosition(-WIDTH / 2f, -HEIGHT / 2f));
        root.addChild(background);

        float groundTop = HEIGHT / 2f - groundTexture.height();
        Sprite ground = new Sprite("Ground").setRegion(new TextureRegion(groundTexture));
        ground.setLayer(1);
        ground.setLocal(ground.getLocal().withPosition(-WIDTH / 2f, groundTop));
        root.addChild(ground);

        Sprite character = new Sprite("Character");
        character.setLayer(2);
        float playerGroundY = groundTop - CHARACTER_FOOT_OFFSET;
        character.setLocal(character.getLocal().withPosition(0, playerGroundY));
        SpriteAnimator animator = new SpriteAnimator(character);
        SpriteAnimation idle = new SpriteAnimation(List.of(atlas.frame(0)), 0.125, true);
        SpriteAnimation walk = new SpriteAnimation(
                List.of(atlas.frame(0), atlas.frame(1), atlas.frame(2), atlas.frame(1)), 0.125, true);
        float horizontalLimit = WIDTH / 2f - CHARACTER_HALF_WIDTH;
        character.addComponent(new PlayerController(character, animator, idle, walk,
                -horizontalLimit, horizontalLimit, playerGroundY));
        character.addComponent(animator);
        root.addChild(character);

        Camera camera = new Camera("MainCamera", WIDTH, HEIGHT);
        return new Scene(root, camera, true);
    }
}
