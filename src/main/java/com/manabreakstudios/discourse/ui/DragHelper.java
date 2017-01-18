package com.manabreakstudios.discourse.ui;

import lombok.Getter;

import java.awt.*;

public class DragHelper {

    @Getter
    private boolean isDragging;

    private Point updateLocation = new Point();

    private Point startLocation = new Point();

    @Getter
    private Point deltaMove = new Point();

    public void beginDrag(Point mouseLocation) {
        isDragging = true;
        startLocation.setLocation(mouseLocation);
        updateLocation.setLocation(mouseLocation);
    }

    public void endDrag() {
        isDragging = false;
        deltaMove.setLocation(0, 0);
    }

    public void updateDrag(Point mouseLocation) {
        if (!isDragging) {
            return;
        }
        deltaMove.setLocation(mouseLocation.x - updateLocation.x, mouseLocation.y - updateLocation.y);
        updateLocation.setLocation(mouseLocation);
    }
}
