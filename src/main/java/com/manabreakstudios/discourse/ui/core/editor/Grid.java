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
        for (int l = 0; firstVisibleLine + l * size < h; l += 1) {
            int lineY = firstVisibleLine + l * size;
            g2d.drawLine(0, lineY, w, lineY);
        }
        for (int c = 0; firstVisibleColumn + c * size < w; c += 1) {
            int lineX= firstVisibleColumn + c * size;
            g2d.drawLine(lineX, 0, lineX, h);
        }

        g2d.setColor(Theme.theme().getGridMajorColor());
        for (int l = 0; firstVisibleLine + l * size < h; l += 4) {
            int lineY = firstVisibleLine + l * size;
            g2d.drawLine(0, lineY, w, lineY);
        }
        for (int c = 0; firstVisibleColumn + c * size < w; c += 4) {
            int lineX= firstVisibleColumn + c * size;
            g2d.drawLine(lineX, 0, lineX, h);
        }

        g2d.dispose();
    }
}
