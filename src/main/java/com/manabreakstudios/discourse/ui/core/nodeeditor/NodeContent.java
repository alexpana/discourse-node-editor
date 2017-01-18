package com.manabreakstudios.discourse.ui.core.nodeeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public interface NodeContent {
    String getTitle();

    Color getColor();

    JPanel getContent();

    JPanel getHeader();

    BufferedImage getIcon();

    List<Slot> getSlots();

    Dimension getPreferredSize();
}
