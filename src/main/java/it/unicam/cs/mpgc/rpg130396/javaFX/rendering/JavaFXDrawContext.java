
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

        if (!(target instanceof JavaFXRenderTarget javaFXTarget)) {
            throw new IllegalArgumentException(
                    "RenderTarget non JavaFX: " + target
            );
        }

        this.target = javaFXTarget;
        this.gc = javaFXTarget.canvas().getGraphicsContext2D();


        this.gc.setImageSmoothing(false);
    }

    @Override
    public void clear() {

        gc.save();

        gc.setTransform(new Affine());
        gc.clearRect(
                0,
                0,
                target.width(),
                target.height()
        );

        gc.restore();
    }

    @Override
    public void drawTexture(TextureRegion region, Transform2D transform) {

        Texture texture = region.getTexture();

        if (!(texture instanceof JavaFXTexture javaFXTexture)) {
            throw new IllegalArgumentException(
                    "Texture non JavaFX: " + texture
            );
        }

        float width = region.width;
        float height = region.height;

        gc.save();

        gc.translate(transform.posX, transform.posY);
        gc.rotate(Math.toDegrees(transform.rotation));
        gc.scale(transform.scale, transform.scale);

        gc.drawImage(
                javaFXTexture.image(),


                region.posX,
                region.posY,
                width,
                height,


                -region.originX,
                -region.originY,
                width,
                height
        );

        gc.restore();
    }
}
