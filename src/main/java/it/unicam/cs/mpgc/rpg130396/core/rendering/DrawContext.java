
package it.unicam.cs.mpgc.rpg130396.core.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

public interface DrawContext {

    void clear();

    void drawTexture(
            TextureRegion region,
            Transform2D transform
    );

    void setTarget(RenderTarget target);
}
