package it.unicam.cs.mpgc.rpg130396.core.resources;

import java.util.Objects;

public final class SpriteAtlas {

    private final Texture texture;
    private final int cellWidth;
    private final int cellHeight;
    private final int columns;
    private final int rows;
    private final int originX;
    private final int originY;

    public SpriteAtlas(Texture texture, int cellWidth, int cellHeight, int originX, int originY) {
        this.texture = Objects.requireNonNull(texture);
        if (cellWidth <= 0 || cellHeight <= 0
                || cellWidth > texture.width() || cellHeight > texture.height()) {
            throw new IllegalArgumentException("Atlas cells must have positive dimensions and fit inside the texture");
        }
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        columns = texture.width() / cellWidth;
        rows = texture.height() / cellHeight;
        this.originX = originX;
        this.originY = originY;
    }

    public TextureRegion frame(int index) {
        if (index < 0 || index / columns >= rows) {
            throw new IndexOutOfBoundsException("Atlas frame index: " + index);
        }
        int x = (index % columns) * cellWidth;
        int y = (index / columns) * cellHeight;
        return new TextureRegion(texture, x, y, cellWidth, cellHeight).setOrigin(originX, originY);
    }
}
