package it.unicam.cs.mpgc.rpg130396.javaFX.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawContext;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderPass;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTexture;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Effect;
import javafx.scene.effect.ImageInput;
import javafx.scene.paint.Color;


public final class JavaFXNativeBloomPass implements RenderPass {

    private static final Transform2D ORIGIN = new Transform2D();

    private final Bloom bloom;
    private final Effect composition;
    private final ImageInput original = new ImageInput();
    private final ImageInput bloomed = new ImageInput();
    private final ColorInput black = new ColorInput(0, 0, 0, 0, Color.BLACK);
    private final ColorInput white = new ColorInput(0, 0, 0, 0, Color.WHITE);
    private JavaFXRenderTarget nativeTarget;

    public JavaFXNativeBloomPass(double threshold) {
        this(threshold, 1);
    }






    public JavaFXNativeBloomPass(double threshold, double intensity) {
        if (!Double.isFinite(threshold) || threshold < 0 || threshold > 1) {
            throw new IllegalArgumentException("Bloom threshold must be in [0, 1]");
        }
        if (!Double.isFinite(intensity) || intensity < 0 || intensity > 2) {
            throw new IllegalArgumentException("Bloom intensity must be in [0, 2]");
        }
        bloom = intensity == 0 ? null : new Bloom(threshold);
        composition = createComposition(intensity);
    }

    private Effect createComposition(double intensity) {
        if (intensity == 0 || intensity == 1) {
            return null;
        }

        Blend originalRgb = new Blend(BlendMode.SRC_OVER, black, original);
        Blend nativeRgb = new Blend(BlendMode.SRC_OVER, black, bloomed);
        Blend halo = new Blend(BlendMode.DIFFERENCE, originalRgb, nativeRgb);
        Blend adjustedRgb = new Blend(intensity > 1 ? BlendMode.ADD : BlendMode.DIFFERENCE, nativeRgb, halo);
        adjustedRgb.setOpacity(Math.abs(intensity - 1));



        ColorAdjust silhouette = new ColorAdjust(0, 0, -1, 0);
        silhouette.setInput(bloomed);
        Blend inverseAlpha = new Blend(BlendMode.SRC_OVER, white, silhouette);
        Blend straightRgb = new Blend(BlendMode.COLOR_DODGE, adjustedRgb, inverseAlpha);
        return new Blend(BlendMode.SRC_ATOP, bloomed, straightRgb);
    }

    @Override
    public void render(DrawContext context, RenderTarget input, RenderTarget output) {
        if (!(input instanceof JavaFXRenderTarget source) || !(output instanceof JavaFXRenderTarget destination)
                || source.canvas() == destination.canvas()
                || source.width() != destination.width() || source.height() != destination.height()) {
            throw new IllegalArgumentException("Native bloom requires distinct JavaFX canvases of equal size");
        }

        JavaFXTexture texture = (JavaFXTexture) source.asTexture();
        TextureRegion region = new TextureRegion(texture);
        if (composition == null) {
            draw(context, destination, region, bloom);
            return;
        }

        if (nativeTarget == null || nativeTarget.width() != source.width() || nativeTarget.height() != source.height()) {
            nativeTarget = new JavaFXRenderTarget(source.width(), source.height());
            black.setWidth(source.width());
            black.setHeight(source.height());
            white.setWidth(source.width());
            white.setHeight(source.height());
        }
        context.setTarget(nativeTarget);
        try {
            context.clear();
            draw(context, nativeTarget, region, bloom);
            bloomed.setSource(((JavaFXTexture) nativeTarget.asTexture()).image());
        } finally {
            context.setTarget(destination);
        }
        original.setSource(texture.image());
        draw(context, destination, region, composition);
    }

    private static void draw(DrawContext context, JavaFXRenderTarget destination, TextureRegion region, Effect effect) {
        GraphicsContext graphics = destination.canvas().getGraphicsContext2D();
        graphics.save();
        try {
            graphics.setEffect(effect);
            context.drawTexture(region, ORIGIN);
        } finally {
            graphics.restore();
        }
    }
}
