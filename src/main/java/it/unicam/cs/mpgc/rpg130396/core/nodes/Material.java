package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

import java.util.Objects;

public final class Material {

    private TextureRegion normalMap;
    private TextureRegion emissiveMap;
    private float emissiveStrength;
    private BlendMode blendMode;

    public Material(TextureRegion normalMap, TextureRegion emissiveMap, float emissiveStrength, BlendMode blendMode) {
        this.normalMap = normalMap;
        this.emissiveMap = emissiveMap;
        this.emissiveStrength = emissiveStrength;
        this.blendMode = blendMode;
    }


    public TextureRegion normalMap()  { return normalMap; }
    public TextureRegion emissiveMap() { return emissiveMap; }
    public float emissiveStrength()   { return emissiveStrength; }
    public BlendMode blendMode()      { return blendMode; }

    public void setEmissiveStrength(float v) { this.emissiveStrength = v; }
    public void setBlendMode(BlendMode m)    { this.blendMode = Objects.requireNonNull(m); }

}