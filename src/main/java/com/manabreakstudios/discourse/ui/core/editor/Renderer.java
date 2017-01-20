package com.manabreakstudios.discourse.ui.core.editor;

import java.awt.*;
import java.awt.geom.CubicCurve2D;

import static com.manabreakstudios.discourse.ui.Theme.theme;

class Renderer {

    static void drawLink(Graphics2D g2d, Point positionFrom, Point positionTo) {
        int horizontalDistance = Math.abs(positionFrom.x - positionTo.x);
        int handleOffset = Math.max(horizontalDistance / 2, 20);

        CubicCurve2D cubicCurve2D = new CubicCurve2D.Float(
                positionFrom.x, positionFrom.y,
                positionFrom.x + handleOffset, positionFrom.y,
                positionTo.x - handleOffset, positionTo.y,
                positionTo.x, positionTo.y);

        g2d.setColor(theme().getNodeBorderColor());
        g2d.setStroke(new BasicStroke(5f));
        g2d.draw(cubicCurve2D);

        g2d.setColor(theme().getLinkColor());
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(cubicCurve2D);
    }

    static void drawSelection(Graphics g, Rectangle rectangle) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(theme().getMarqueeSelectColor());
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10, new float[]{10, 10}, 0));
        g2d.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        g2d.dispose();
    }

    static void drawNodeHighlight(Graphics g, Rectangle rectangle) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color focusHalo = theme().getSelectionColor();
        Color transparentFocusHalo = new Color((0x50 << 24) + focusHalo.getRGB(), true);
        g2d.setColor(transparentFocusHalo);
        g2d.fillRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 8, 8);
        transparentFocusHalo = new Color((0xC0 << 24) + focusHalo.getRGB(), true);
        g2d.setColor(transparentFocusHalo);
        g2d.fillRoundRect(rectangle.x + 1, rectangle.y + 1, rectangle.width - 2, rectangle.height - 2, 6, 6);
        g2d.dispose();
    }
}
