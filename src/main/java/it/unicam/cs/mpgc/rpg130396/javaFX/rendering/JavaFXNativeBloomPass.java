package it.unicam.cs.mpgc.rpg130396.javaFX.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawContext;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderPass;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;


public final class JavaFXNativeBloomPass implements RenderPass {

    private final Bloom bloom;

    public JavaFXNativeBloomPass(double threshold) {
        if (!Double.isFinite(threshold) || threshold < 0 || threshold > 1) {
            throw new IllegalArgumentException("Bloom threshold must be in [0, 1]");
        }
        bloom = new Bloom(threshold);
    }

    @Override
    public void render(DrawContext context, RenderTarget input, RenderTarget output) {
        if (!(input instanceof JavaFXRenderTarget source) || !(output instanceof JavaFXRenderTarget destination)
                || source.canvas() == destination.canvas()
                || source.width() != destination.width() || source.height() != destination.height()) {
            throw new IllegalArgumentException("Native bloom requires distinct JavaFX canvases of equal size");
        }


        TextureRegion region = new TextureRegion(source.asTexture());
        GraphicsContext graphics = destination.canvas().getGraphicsContext2D();
        graphics.save();
        try {
            graphics.setEffect(bloom);
            context.drawTexture(region, new Transform2D());
        } finally {
            graphics.restore();
        }
    }
}
