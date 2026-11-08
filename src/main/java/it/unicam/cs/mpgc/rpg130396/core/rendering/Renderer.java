package it.unicam.cs.mpgc.rpg130396.core.rendering;

import java.util.List;

public class Renderer {

    private  List<PassBinding> bindings;
    private final DrawContext context;

    public Renderer(List<PassBinding> bindings, DrawContext context) {
        this.bindings = bindings;
        this.context = context;
    }

    public Renderer setPipeline(List<PassBinding> bindings){
        this.bindings = List.copyOf(bindings);
        return this;
    }

    public void render() {

        for (PassBinding b : bindings) {

            context.setTarget(b.output());

            if (b.clearOutput())
                context.clear();

            b.pass().render(context, b.input(), b.output());
        }
    }
}
