package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawCall;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

public class Sprite extends Node implements Drawable{

    TextureRegion region;
    Material material;

    public Sprite(String name) {
        super(name);
    }

    @Override
    public void addDrawcall(DrawList out) {
        out.add(new DrawCall(region, material, getWorld().copy(), layer, zIndex));
    }


}
