
package it.unicam.cs.mpgc.rpg130396.core.rendering;

import it.unicam.cs.mpgc.rpg130396.core.nodes.DrawList;

import java.util.List;
import java.util.Objects;

public class Renderer {

    private final DrawContext context;
    private final RenderTarget sceneTarget;

    private List<PassBinding> bindings;

    public Renderer(
            DrawContext context,
            RenderTarget sceneTarget,
            List<PassBinding> bindings
    ) {
        this.context = Objects.requireNonNull(context);
        this.sceneTarget = Objects.requireNonNull(sceneTarget);
        setPipeline(bindings);
    }

    public Renderer setPipeline(List<PassBinding> bindings) {
        this.bindings = List.copyOf(bindings);
        return this;
    }

    public void render(DrawList drawList) {

        Objects.requireNonNull(drawList);

        context.setTarget(sceneTarget);
        context.clear();

        for (DrawCall drawCall : drawList.getCalls()) {

            context.drawTexture(
                    drawCall.textureRegion(),
                    drawCall.transform2D()
            );
        }

        for (PassBinding binding : bindings) {

            context.setTarget(binding.output());

            if (binding.clearOutput()) {
                context.clear();
            }

            binding.pass().render(
                    context,
                    binding.input(),
                    binding.output()
            );
        }
    }
}
