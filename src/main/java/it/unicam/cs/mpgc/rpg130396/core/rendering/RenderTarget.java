package it.unicam.cs.mpgc.rpg130396.core.rendering;

import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;


public interface RenderTarget {
    int width();
    int height();
    Texture asTexture();
}
