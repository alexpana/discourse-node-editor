package com.manabreakstudios.discourse.ui.app.nodes;

import com.manabreakstudios.discourse.ui.core.nodeeditor.NodeContent;
import com.manabreakstudios.discourse.ui.core.nodeeditor.Slot;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.manabreakstudios.discourse.ui.Theme.theme;
import static com.manabreakstudios.discourse.ui.core.nodeeditor.Slot.Direction.INPUT;
import static com.manabreakstudios.discourse.ui.core.nodeeditor.Slot.Direction.OUTPUT;

public class ReplyChoiceNode implements NodeContent {

    @Getter
    private final String title = "Reply Choice";

    @Getter
    private final Color color = theme().getChoiceNodeColor();

    @Getter
    private final List<Slot> slots = new ArrayList<>();

    @Getter
    private final JPanel content = new JPanel();

    public ReplyChoiceNode() {
        slots.add(new Slot(INPUT, 0, 13));
        slots.add(new Slot(OUTPUT, 1, 40));
        slots.add(new Slot(OUTPUT, 2, 60));
        content.setOpaque(false);
        content.setLayout(new FlowLayout());
        content.add(new JLabel("Choice: "));
        content.add(new JTextField());
    }

    @Override
    public JPanel getHeader() {
        return null;
    }

    @Override
    public BufferedImage getIcon() {
        return null;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 100);
    }
}
