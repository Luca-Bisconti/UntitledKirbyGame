package it.unicam.cs.mpgc.rpg130396.javaFX.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawContext;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTexture;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

public class JavaFXDrawContext implements DrawContext {

    private JavaFXRenderTarget target;
    private GraphicsContext gc;

    public JavaFXDrawContext(JavaFXRenderTarget defaultTarget) {
        setTarget(defaultTarget);
    }

    @Override
    public void setTarget(RenderTarget target) {
        if (!(target instanceof JavaFXRenderTarget t))
            throw new IllegalArgumentException("RenderTarget non JavaFX: " + target);
        this.target = t;
        this.gc = t.canvas().getGraphicsContext2D();
    }

    @Override
    public void clear() {
        gc.save();
        gc.setTransform(new Affine());   
        gc.clearRect(0, 0, target.width(), target.height());
        gc.restore();
    }

    @Override
    public void drawTexture(TextureRegion region, Transform2D t) {
        Texture tex = region.getTexture();
        if (!(tex instanceof JavaFXTexture fxTex))
            throw new IllegalArgumentException("Texture non JavaFX: " + tex);

        float w = region.width, h = region.height;

        gc.save();
        gc.translate(t.posX, t.posY);
        gc.rotate(Math.toDegrees(t.rotation));   
        gc.scale(t.scale, t.scale);
        gc.drawImage(fxTex.image(),
                region.posX, region.posY, w, h,            
                -t.originX * w, -t.originY * h, w, h);   
        gc.restore();
    }
}