package com.manabreakstudios.discourse.ui.core.utils;

import com.manabreakstudios.discourse.ui.core.editor.NodeUI;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SwingUtils {

    private static final List<Class> INPUT_TRANSPARENT_COMPONENTS = Arrays.asList(JLabel.class, JPanel.class, JLayeredPane.class);

    public static Point screenToLocal(Point screen, Component component) {
        Point result = new Point(screen);
        SwingUtilities.convertPointFromScreen(result, component);
        return result;
    }

    public static NodeUI getNodeUIAncestor(Component component) {
        Component ancestor = component;
        while (ancestor != null) {
            if (ancestor instanceof NodeUI) {
                return (NodeUI) ancestor;
            }
            ancestor = ancestor.getParent();
        }
        return null;
    }

    public static Component getComponentAt(Component parent, Point point) {
        return SwingUtilities.getDeepestComponentAt(parent, point.x, point.y);
    }

    public static boolean isInputTransparent(Component component) {
        for (Class aClass : INPUT_TRANSPARENT_COMPONENTS) {
            if (aClass.isAssignableFrom(component.getClass())) {
                return true;
            }
        }
        return false;
    }

    public static Rectangle expand(Rectangle rect, int size) {
        rect.setRect(rect.x - size, rect.y - size, rect.width + 2 * size, rect.height + 2 * size);
        return rect;
    }

    public static void drawCircle(Graphics2D g2d, int x, int y, int size) {
        g2d.fillRoundRect(x, y, size, size, size, size);
    }
}
