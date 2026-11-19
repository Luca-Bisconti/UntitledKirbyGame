package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.math.Rect;
import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawCall;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

import java.util.ArrayList;

public class DrawCollector {

    public DrawList collect(Scene scene) {

        DrawList drawList = new DrawList();

        if (scene == null || !scene.isActive) {
            return drawList;
        }

        Camera camera = scene.camera();
        Node root = scene.root();

        Rect viewRect = camera.viewRect(0);

        collectNode(root, camera, viewRect, drawList);

        drawList.sort();

        return drawList;
    }

    private void collectNode(
            Node node,
            Camera camera,
            Rect viewRect,
            DrawList drawList
    ) {


        if (!node.active || !node.visible) {
            return;
        }

        if (node instanceof Drawable drawable) {

            boolean insideCamera = true;


            if (node instanceof Sprite sprite) {
                TextureRegion region = sprite.getRegion();

                insideCamera = region != null
                        && intersectsCamera(
                        node.getWorld(),
                        region,
                        viewRect
                );
            }

            if (insideCamera) {
                addScreenDrawCalls(drawable, camera, drawList);
            }
        }


        for (Node child : node.getChildren()) {
            collectNode(child, camera, viewRect, drawList);
        }
    }

    private void addScreenDrawCalls(
            Drawable drawable,
            Camera camera,
            DrawList drawList
    ) {

        ArrayList<DrawCall> calls = (ArrayList<DrawCall>) drawList.getCalls();

        int firstNewCall = calls.size();


        drawable.addDrawcall(drawList);


        for (int i = firstNewCall; i < calls.size(); i++) {

            DrawCall worldCall = calls.get(i);

            Transform2D screenTransform =
                    camera.toScreen(worldCall.transform2D());

            calls.set(i, new DrawCall(
                    worldCall.textureRegion(),
                    worldCall.material(),
                    screenTransform,
                    worldCall.layer(),
                    worldCall.zIndex()
            ));
        }
    }

    private boolean intersectsCamera(
            Transform2D transform,
            TextureRegion region,
            Rect viewRect
    ) {

        float pivotX = region.originX;
        float pivotY = region.originY;

        float width = region.width;
        float height = region.height;


        float[] localX = {
                -pivotX,
                width - pivotX,
                width - pivotX,
                -pivotX
        };

        float[] localY = {
                -pivotY,
                -pivotY,
                height - pivotY,
                height - pivotY
        };

        float cos = (float) Math.cos(transform.rotation);
        float sin = (float) Math.sin(transform.rotation);

        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < 4; i++) {

            float scaledX = localX[i] * transform.scale;
            float scaledY = localY[i] * transform.scale;

            float rotatedX = scaledX * cos - scaledY * sin;
            float rotatedY = scaledX * sin + scaledY * cos;

            float worldX = transform.posX + rotatedX;
            float worldY = transform.posY + rotatedY;

            minX = Math.min(minX, worldX);
            minY = Math.min(minY, worldY);
            maxX = Math.max(maxX, worldX);
            maxY = Math.max(maxY, worldY);
        }


        return minX < viewRect.x() + viewRect.width()
                && maxX > viewRect.x()
                && minY < viewRect.y() + viewRect.height()
                && maxY > viewRect.y();
    }
}