package it.unicam.cs.mpgc.rpg130396.core.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;


public final class Compositor implements RenderPass {

    @Override
    public void render(DrawContext context, RenderTarget input, RenderTarget output) {
        TextureRegion region = new TextureRegion(input.asTexture());
        Transform2D transform = new Transform2D();
        transform.scale = (float) output.width() / input.width();
        context.drawTexture(region, transform);
    }
}
