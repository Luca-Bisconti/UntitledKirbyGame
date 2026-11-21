package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureStore;
import it.unicam.cs.mpgc.rpg130396.game.DemoAssets;
import it.unicam.cs.mpgc.rpg130396.game.DemoConfig.AtlasLayout;
import it.unicam.cs.mpgc.rpg130396.game.DemoConfig.Decoration;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTextureLoader;

import java.util.List;

import static it.unicam.cs.mpgc.rpg130396.game.DemoConfig.*;


final class JavaFXDemoAssets {

    private final TextureStore textures = new TextureStore(new JavaFXTextureLoader());

    DemoAssets load() {
        Texture bridge = requiredTexture(BRIDGE_PATH);
        Texture sky = requiredTexture(SKY_PATH);
        Texture moon = requiredTexture(MOON_PATH);
        Texture clouds = requiredTexture(CLOUDS_PATH);
        Texture idle = requiredTexture(IDLE_PATH);
        Texture walk = requiredTexture(WALK_PATH);
        SpriteAnimation crouch = null;
        if (JavaFXDemoAssets.class.getResource(CROUCH_PATH) != null) {
            crouch = animation(CROUCH_PATH, CROUCH_ATLAS, requiredTexture(CROUCH_PATH));
        } else {
            System.out.println("Crouch PNG assente, uso l'animazione idle: " + CROUCH_PATH);
        }
        return new DemoAssets(List.of(
                new DemoAssets.Background("Sky", sky, 0, 0, SKY_PARALLAX, true),
                decoration("Moon", moon, MOON),
                decoration("FarClouds", clouds, FAR_CLOUDS),
                decoration("NearClouds", clouds, NEAR_CLOUDS)
        ), bridge, animation(IDLE_PATH, IDLE_ATLAS, idle), animation(WALK_PATH, WALK_ATLAS, walk), crouch);
    }

    private Texture requiredTexture(String path) {
        if (JavaFXDemoAssets.class.getResource(path) == null) {
            throw new IllegalStateException("PNG obbligatorio assente in modalità real: " + path);
        }
        textures.load(path);
        return textures.get(path);
    }

    private static SpriteAnimation animation(String path, AtlasLayout layout, Texture texture) {
        if (layout == null) {
            throw new IllegalStateException("Configura l'atlas in DemoConfig per " + path);
        }
        return layout.animation(texture);
    }

    private static DemoAssets.Background decoration(String name, Texture texture, Decoration placement) {
        return new DemoAssets.Background(name, texture, placement.centerX(), placement.centerY(),
                placement.factor(), false);
    }
}
