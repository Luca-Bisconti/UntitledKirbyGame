
package it.unicam.cs.mpgc.rpg130396.core.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.DirectionalLight;
import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Material;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

import java.util.List;

public interface DrawContext {

    void clear();

    void drawTexture(
            TextureRegion region,
            Material material,
            Transform2D transform
    );

    void setDirectionalLights(List<DirectionalLight> lights);

    void setTarget(RenderTarget target);
}
