package it.unicam.cs.mpgc.rpg130396.core.rendering;

public record PassBinding(RenderPass pass, RenderTarget input, RenderTarget output, boolean clearOutput) {}