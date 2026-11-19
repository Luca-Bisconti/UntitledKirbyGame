
package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.math.Rect;
import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawCall;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

public class DrawCollector {

    public DrawList collect(Scene scene) {

        DrawList drawList = new DrawList();

        if (scene == null || !scene.isActive) {
            return drawList;
        }

        Camera camera = scene.camera();
        Rect cameraBounds = camera.viewRect(0);

        collectNode(
                scene.root(),
                camera,
                cameraBounds,
                drawList
        );

        drawList.sort();

        return drawList;
    }

    private void collectNode(
            Node node,
            Camera camera,
            Rect cameraBounds,
            DrawList drawList
    ) {

        if (!node.active || !node.visible) {
            return;
        }

        if (node instanceof Drawable drawable) {

            if (node instanceof Sprite sprite) {

                TextureRegion region = sprite.getRegion();

                if (region != null && isVisible(
                        node.getWorld(),
                        region,
                        cameraBounds
                )) {
                    addDrawCalls(drawable, camera, drawList);
                }

            } else {
                addDrawCalls(drawable, camera, drawList);
            }
        }

        for (Node child : node.getChildren()) {
            collectNode(child, camera, cameraBounds, drawList);
        }
    }

    private void addDrawCalls(
            Drawable drawable,
            Camera camera,
            DrawList drawList
    ) {

        DrawList temporaryList = new DrawList();

        drawable.addDrawcall(temporaryList);

        for (DrawCall call : temporaryList.getCalls()) {

            Transform2D screenTransform =
                    camera.toScreen(call.transform2D());

            drawList.add(new DrawCall(
                    call.textureRegion(),
                    call.material(),
                    screenTransform,
                    call.layer(),
                    call.zIndex()
            ));
        }
    }

    private boolean isVisible(
            Transform2D transform,
            TextureRegion region,
            Rect cameraBounds
    ) {

        float[] localX = {
                -region.originX,
                region.width - region.originX,
                region.width - region.originX,
                -region.originX
        };

        float[] localY = {
                -region.originY,
                -region.originY,
                region.height - region.originY,
                region.height - region.originY
        };

        float cos = (float) Math.cos(transform.rotation);
        float sin = (float) Math.sin(transform.rotation);

        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < 4; i++) {

            float x = localX[i] * transform.scale;
            float y = localY[i] * transform.scale;

            float worldX = transform.posX + x * cos - y * sin;
            float worldY = transform.posY + x * sin + y * cos;

            minX = Math.min(minX, worldX);
            minY = Math.min(minY, worldY);
            maxX = Math.max(maxX, worldX);
            maxY = Math.max(maxY, worldY);
        }

        return minX < cameraBounds.x() + cameraBounds.width()
                && maxX > cameraBounds.x()
                && minY < cameraBounds.y() + cameraBounds.height()
                && maxY > cameraBounds.y();
    }
}
