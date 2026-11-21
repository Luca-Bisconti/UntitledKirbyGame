package it.unicam.cs.mpgc.rpg130396.game;

import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAtlas;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;

import java.util.List;


public final class DemoConfig {

    public static final int LOGICAL_WIDTH = 320;
    public static final int LOGICAL_HEIGHT = 180;

    public static final int BRIDGE_SEGMENTS = 6;
    public static final float BRIDGE_SCALE = 0.75f;
    public static final int BRIDGE_TEXTURE_WIDTH = 224;
    public static final int BRIDGE_TEXTURE_HEIGHT = 226;
    public static final int BRIDGE_WALKABLE_Y = 132;
    public static final float SEGMENT_WORLD_WIDTH = BRIDGE_TEXTURE_WIDTH * BRIDGE_SCALE;
    public static final float WORLD_WIDTH = BRIDGE_SEGMENTS * SEGMENT_WORLD_WIDTH;
    public static final float WORLD_MIN_X = -WORLD_WIDTH / 2;
    public static final float WORLD_MAX_X = WORLD_MIN_X + WORLD_WIDTH;
    public static final float GROUND_Y = 58f;

    public static final String BRIDGE_PATH = "/assets/environment/bridge.png";
    public static final String SKY_PATH = "/assets/backgrounds/sky.png";
    public static final String MOON_PATH = "/assets/decorations/moon.png";
    public static final String CLOUDS_PATH = "/assets/decorations/clouds.png";
    public static final String IDLE_PATH = "/assets/kirby/idle.png";
    public static final String WALK_PATH = "/assets/kirby/walk.png";
    public static final String CROUCH_PATH = "/assets/kirby/crouch.png";


    public static final Decoration MOON = new Decoration(0f, 0f, 0.1f);
    public static final Decoration FAR_CLOUDS = new Decoration(0f, 0f, 0.2f);
    public static final Decoration NEAR_CLOUDS = new Decoration(0f, 0f, 0.4f);
    public static final float SKY_PARALLAX = 0f;


    public static final AtlasLayout IDLE_ATLAS = null;
    public static final AtlasLayout WALK_ATLAS = null;
    public static final AtlasLayout CROUCH_ATLAS = null;

    public record Decoration(float centerX, float centerY, float factor) {}

    public record AtlasLayout(int cellWidth, int cellHeight, int originX, int originY,
                              List<Integer> frames, double frameSeconds, boolean loop) {
        public AtlasLayout {
            frames = List.copyOf(frames);
        }

        public SpriteAnimation animation(Texture texture) {
            SpriteAtlas atlas = new SpriteAtlas(texture, cellWidth, cellHeight, originX, originY);
            return new SpriteAnimation(frames.stream().map(atlas::frame).toList(), frameSeconds, loop);
        }
    }

    private DemoConfig() {}
}
