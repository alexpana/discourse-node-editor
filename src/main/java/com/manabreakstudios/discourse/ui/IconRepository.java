package com.manabreakstudios.discourse.ui;

import com.sun.javafx.iio.ImageStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

import static com.manabreakstudios.discourse.ui.FontAwesomeGlyph.LIST;
import static com.manabreakstudios.discourse.ui.Theme.theme;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class IconRepository {

    private static final float NODE_ICON_FONT_SIZE = 28.0f;

    private static final int NODE_ICON_SIZE = 40;

    private static Icon replyChoiceNodeIcon = null;

    public static Icon getReplyChoiceNodeIcon() {
        if (replyChoiceNodeIcon == null) {
            replyChoiceNodeIcon = createReplyChoiceNodeIcon();
        }
        return replyChoiceNodeIcon;
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
