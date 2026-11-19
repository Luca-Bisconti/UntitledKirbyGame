
package it.unicam.cs.mpgc.rpg130396.core.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.nodes.DrawList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;


public class Renderer {

    private final DrawContext context;
    private final RenderTarget sceneTarget;
    private final Map<Integer, RenderTarget> layerTargets;
    private final List<PassBinding> composition;
    private final int logicalWidth;

    private List<PassBinding> bindings;

    public Renderer(
            DrawContext context,
            RenderTarget sceneTarget,
            List<PassBinding> bindings
    ) {
        this.context = Objects.requireNonNull(context);
        this.sceneTarget = Objects.requireNonNull(sceneTarget);
        requirePositiveDimensions(sceneTarget);
        layerTargets = Map.of();
        composition = List.of();
        logicalWidth = 0;
        this.bindings = validatedPasses(bindings);
    }





    public Renderer(
            DrawContext context,
            Map<Integer, RenderTarget> layerTargets,
            RenderTarget output,
            int logicalWidth,
            int logicalHeight,
            List<PassBinding> bindings
    ) {
        this.context = Objects.requireNonNull(context);
        sceneTarget = Objects.requireNonNull(output);
        requireAspectRatio(logicalWidth, logicalHeight);
        this.logicalWidth = logicalWidth;
        this.layerTargets = Collections.unmodifiableMap(new TreeMap<>(Map.copyOf(layerTargets)));
        if (this.layerTargets.isEmpty()) {
            throw new IllegalArgumentException("At least one layer target is required");
        }

        Set<RenderTarget> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        List<PassBinding> passes = new ArrayList<>();
        Compositor compositor = new Compositor();
        RenderTarget previous = null;
        for (RenderTarget target : this.layerTargets.values()) {
            if (target == output) {
                throw new IllegalArgumentException("The output cannot also be a layer target");
            }
            if (target == previous) {
                continue;
            }
            if (!seen.add(target)) {
                throw new IllegalArgumentException("Layers sharing a target must be consecutive");
            }
            passes.add(new PassBinding(compositor, target, output, passes.isEmpty()));
            previous = target;
        }
        composition = List.copyOf(passes);
        validateCompositionSizes();
        this.bindings = validatedPasses(bindings);
    }

    public Renderer setPipeline(List<PassBinding> bindings) {
        this.bindings = validatedPasses(bindings);
        return this;
    }

    private static List<PassBinding> validatedPasses(List<PassBinding> bindings) {
        List<PassBinding> passes = List.copyOf(bindings);
        validatePassSizes(passes);
        return passes;
    }

    public void render(DrawList drawList) {
        List<DrawCall> calls = Objects.requireNonNull(drawList).getCalls();
        validatePassSizes(bindings);
        if (composition.isEmpty()) {
            requirePositiveDimensions(sceneTarget);
            context.setTarget(sceneTarget);
            context.clear();
            for (DrawCall call : calls) {
                context.drawTexture(call.textureRegion(), call.transform2D());
            }
        } else {
            renderLayers(calls);
            runPasses(composition);
        }
        runPasses(bindings);
    }

    private void renderLayers(List<DrawCall> calls) {
        validateCompositionSizes();
        Map<Integer, List<DrawCall>> callsByLayer = new HashMap<>();
        for (DrawCall call : calls) {
            if (!layerTargets.containsKey(call.layer())) {
                throw new IllegalArgumentException("No render target configured for layer " + call.layer());
            }
            callsByLayer.computeIfAbsent(call.layer(), ignored -> new ArrayList<>()).add(call);
        }

        RenderTarget previous = null;
        for (Map.Entry<Integer, RenderTarget> entry : layerTargets.entrySet()) {
            RenderTarget target = entry.getValue();
            if (target != previous) {
                context.setTarget(target);
                context.clear();
                previous = target;
            }

            float scale = (float) target.width() / logicalWidth;
            for (DrawCall call : callsByLayer.getOrDefault(entry.getKey(), List.of())) {
                Transform2D transform = call.transform2D();
                if (scale != 1f) {
                    transform = new Transform2D(
                            transform.posX() * scale, transform.posY() * scale,
                            transform.rotation(), transform.scale() * scale
                    );
                }
                context.drawTexture(call.textureRegion(), transform);
            }
        }
    }

    private void validateCompositionSizes() {
        requireAspectRatio(sceneTarget.width(), sceneTarget.height());
        for (PassBinding binding : composition) {
            RenderTarget input = binding.input();
            requireAspectRatio(input.width(), input.height());
        }
    }

    private static void requireAspectRatio(int width, int height) {
        if (width <= 0 || height <= 0 || width * 9L != height * 16L) {
            throw new IllegalArgumentException("Expected positive 16:9 dimensions, got " + width + "x" + height);
        }
    }

    private static void validatePassSizes(List<PassBinding> passes) {
        for (PassBinding binding : passes) {
            requirePositiveDimensions(binding.input());
            requirePositiveDimensions(binding.output());
        }
    }

    private static void requirePositiveDimensions(RenderTarget target) {
        if (target.width() <= 0 || target.height() <= 0) {
            throw new IllegalArgumentException("Render target dimensions must be positive");
        }
    }

    private void runPasses(List<PassBinding> passes) {
        for (PassBinding binding : passes) {
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
