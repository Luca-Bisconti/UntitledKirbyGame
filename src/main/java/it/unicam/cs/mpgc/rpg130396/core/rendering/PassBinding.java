package it.unicam.cs.mpgc.rpg130396.core.rendering;

import java.util.Objects;

public record PassBinding(RenderPass pass, RenderTarget input, RenderTarget output, boolean clearOutput) {

    public PassBinding {
        Objects.requireNonNull(pass);
        Objects.requireNonNull(input);
        Objects.requireNonNull(output);
    }
}
