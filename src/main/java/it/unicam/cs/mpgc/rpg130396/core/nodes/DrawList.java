package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawCall;

import java.util.ArrayList;
import java.util.Comparator;

public class DrawList {

    public ArrayList<DrawCall> drawList;

    public DrawList(){

        drawList = new ArrayList<>();
    }

    public void add(DrawCall drawCall){
        this.drawList.add(drawCall);
    }

    public ArrayList<DrawCall> getCalls() {
        return drawList;
    }

    public void clear(){
        drawList.clear();
    }

    public void sort() {

    }

}
