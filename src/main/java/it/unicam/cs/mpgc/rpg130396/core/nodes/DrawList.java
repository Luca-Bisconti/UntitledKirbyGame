
package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawCall;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DrawList {

    private final List<DrawCall> drawCalls = new ArrayList<>();

    public void add(DrawCall drawCall) {
        drawCalls.add(drawCall);
    }

    public List<DrawCall> getCalls() {
        return List.copyOf(drawCalls);
    }

    public void clear() {
        drawCalls.clear();
    }

    public void sort() {
        drawCalls.sort(
                Comparator.comparingInt(DrawCall::layer)
                        .thenComparingInt(DrawCall::zIndex)
        );
    }
}
