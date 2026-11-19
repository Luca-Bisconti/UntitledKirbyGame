
package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawCall;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

public class Sprite extends Node implements Drawable {

    private TextureRegion region;
    private Material material;

    public Sprite(String name) {
        super(name);
    }

    public TextureRegion getRegion() {
        return region;
    }

    public Sprite setRegion(TextureRegion region) {
        this.region = region;
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public Sprite setMaterial(Material material) {
        this.material = material;
        return this;
    }

    @Override
    public void addDrawcall(DrawList out) {
        if (region == null) {
            return;
        }

        out.add(new DrawCall(
                region,
                material,
                getWorld().copy(),
                layer,
                zIndex
        ));
    }
}
