package it.unicam.cs.mpgc.rpg130396.core.resources;

import java.util.Objects;

public final class TextureRegion {

    private final Texture texture;
    private int posX, posY;
    private int width, height;
    private int originX, originY;
    private boolean flippedHorizontally;

    public TextureRegion(Texture texture) {
        this(texture, 0, 0, Objects.requireNonNull(texture).width(), texture.height());
    }

    public TextureRegion(Texture texture, int x, int y, int width, int height) {
        this.texture = Objects.requireNonNull(texture);
        setBounds(x, y, width, height);
    }

    public TextureRegion setPosition(int x, int y) {
        return setBounds(x, y, width, height);
    }

    public TextureRegion setRegionSize(int width, int height) {
        return setBounds(posX, posY, width, height);
    }

    public TextureRegion setBounds(int x, int y, int width, int height) {
        if (x < 0 || y < 0 || width <= 0 || height <= 0
                || (long) x + width > texture.width() || (long) y + height > texture.height()) {
            throw new IllegalArgumentException("Region must have positive dimensions and fit inside its texture");
        }
        posX = x;
        posY = y;
        this.width = width;
        this.height = height;
        return this;
    }


    public TextureRegion setOrigin(int x, int y) {
        originX = x;
        originY = y;
        return this;
    }


    public TextureRegion setFlippedHorizontally(boolean flippedHorizontally) {
        this.flippedHorizontally = flippedHorizontally;
        return this;
    }

    public boolean isFlippedHorizontally() {
        return flippedHorizontally;
    }

    public int posX() { return posX; }
    public int posY() { return posY; }
    public int width() { return width; }
    public int height() { return height; }
    public int originX() { return originX; }
    public int originY() { return originY; }

    public Texture getTexture() {
        return texture;
    }
}
