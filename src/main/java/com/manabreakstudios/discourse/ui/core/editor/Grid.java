package com.manabreakstudios.discourse.ui.core.editor;

import com.manabreakstudios.discourse.ui.Theme;
import lombok.Getter;

import java.awt.*;

public class Grid {

    @Getter
    private int size = 30;

    public Point snap(Point point) {
        return new Point((int) (point.getX() / (float) size) * size, (int) (point.getY() / (float) size) * size);
    }

    public void paint(Graphics g, int x, int y, int w, int h) {
        int firstVisibleLine = 0;
        int firstVisibleColumn = 0;

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Theme.theme().getGridColor());
        for (int l = firstVisibleLine; l < h; l += size) {
            g2d.drawLine(0, l, w, l);
        }
        for (int c = firstVisibleColumn; c < w; c += size) {
            g2d.drawLine(c, 0, c, h);
        }
        g2d.dispose();
    }
}
