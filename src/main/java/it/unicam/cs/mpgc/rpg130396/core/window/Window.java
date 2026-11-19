package it.unicam.cs.mpgc.rpg130396.core.window;

import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawContext;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;

public interface Window {

    public void open();
    public void close();


    public Window setTitle(String title);
    public Window setWidth(double width);
    public Window setHeight(double height);
    public Window setResizable(boolean isResizable);
    public Window setIconFilePath(String path);



    public String getTitle();
    public double getWidth();
    public double getHeight();
    public boolean isResizable();
    public String getIconFilePath();


    public Window setFrameListener(FrameListener listener);
    public DrawContext getDrawContext();
    public RenderTarget getScreenTarget();
}
