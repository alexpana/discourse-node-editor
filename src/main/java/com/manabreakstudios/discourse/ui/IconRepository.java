package com.manabreakstudios.discourse.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static com.manabreakstudios.discourse.ui.FontAwesomeGlyph.LIST;
import static com.manabreakstudios.discourse.ui.Theme.theme;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class IconRepository {

    private static final float NODE_ICON_FONT_SIZE = 28.0f;

    private static final int NODE_ICON_SIZE = 40;

    private static final Map<String, BufferedImage> cache = new HashMap<>();

    private static Icon replyChoiceNodeIcon = null;

    public static Icon getReplyChoiceNodeIcon() {
        if (replyChoiceNodeIcon == null) {
            replyChoiceNodeIcon = createReplyChoiceNodeIcon();
        }
        return replyChoiceNodeIcon;
    }

    public static void cacheIcon(String name, BufferedImage icon) {
        cache.put(name, icon);
    }

    public static boolean isCached(String name) {
        return cache.containsKey(name);
    }

    public static BufferedImage getIcon(String name) {
        return cache.get(name);
    }

    public static BufferedImage newFontAwesomeIcon(FontAwesomeGlyph glyph, int iconSize, int glyphSize, Color color, int offsetX, int offsetY) {
        BufferedImage image = new BufferedImage(NODE_ICON_SIZE, NODE_ICON_SIZE, TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = theme().getFontAwesome().deriveFont(NODE_ICON_FONT_SIZE);
        g2d.setFont(font);
        g2d.setColor(theme().getChoiceNodeColor());
        g2d.drawString(LIST.getRepresentation(), 6f, NODE_ICON_SIZE - 9f);
        return image;
    }

    private static Icon createReplyChoiceNodeIcon() {
        BufferedImage image = new BufferedImage(NODE_ICON_SIZE, NODE_ICON_SIZE, TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = theme().getFontAwesome().deriveFont(NODE_ICON_FONT_SIZE);
        g2d.setFont(font);
        g2d.setColor(theme().getChoiceNodeColor());
        g2d.drawString(LIST.getRepresentation(), 6f, NODE_ICON_SIZE - 9f);
        g2d.dispose();
        return new ImageIcon(image);
    }
}
