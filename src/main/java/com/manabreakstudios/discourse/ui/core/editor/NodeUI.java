package com.manabreakstudios.discourse.ui.core.editor;

import com.manabreakstudios.discourse.ui.Theme;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

/**
 * A NodeUI is a UI component that holds a node.
 */
public class NodeUI extends JPanel {

    private final NodeContent content;

    @Getter @Setter
    private boolean isSelected = false;

    public NodeUI(NodeContent nodeContent) {
        this.content = nodeContent;
        setLayout(new BorderLayout());
        add(new Header(), BorderLayout.NORTH);
        add(content.getContent(), BorderLayout.CENTER);
        setPreferredSize(content.getPreferredSize());
    }

    public SlotBinding getSlot(int index) {
        return new SlotBinding(this, content.getSlots().get(index));
    }

    @Override
    protected void paintComponent(Graphics g) {
        int slotSize = Theme.theme().getSlotSize();

        int inset = slotSize / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isSelected) {
            Color focusHalo = Theme.theme().getSelectionColor();
            Color transparentFocusHalo = new Color((0x50 << 24) + focusHalo.getRGB(), true);
            g2d.setColor(transparentFocusHalo);
            g2d.fillRoundRect(inset - 2, inset - 2, getWidth() - 2 * inset + 4, getHeight() - 2 * inset + 4, 8, 8);
            transparentFocusHalo = new Color((0xC0 << 24) + focusHalo.getRGB(), true);
            g2d.setColor(transparentFocusHalo);
            g2d.fillRoundRect(inset - 1, inset - 1, getWidth() - 2 * inset + 2, getHeight() - 2 * inset + 2, 6, 6);
        }

        g2d.setColor(Theme.theme().getNodeBorderColor());
        g2d.fillRoundRect(inset, inset, getWidth() - 2 * inset, getHeight() - 2 * inset, 6, 6);

        g2d.setColor(Theme.theme().getNodeSpecularColor());
        g2d.fillRoundRect(inset + 2, inset + 2, getWidth() - 2 * inset - 4, getHeight() - 2 * inset - 4, 4, 4);

        g2d.setColor(Theme.theme().getNodeBackgroundColor());
        g2d.fillRoundRect(inset + 2, inset + 3, getWidth() - 2 * inset - 4, getHeight() - 2 * inset - 5, 4, 4);

        Font font = (Font) UIManager.getDefaults().get("Font.OpenSans-ExtraBold");

        g2d.setColor(content.getColor());
        g2d.setFont(font.deriveFont(12.0f));
        g2d.drawString(content.getTitle(), inset + 10, inset + 18);

        for (Slot slot : content.getSlots()) {
            paintSlot(g2d, slot);
        }
    }

    private void paintSlot(Graphics2D g2d, Slot slot) {
        int slotSize = Theme.theme().getSlotSize();
        int slotBorderWidth = Theme.theme().getSlotBorderWidth();

        int positionX = slot.getDirection() == Slot.Direction.INPUT ? 0 : (getWidth() - slotSize);
        g2d.setColor(Theme.theme().getNodeBorderColor());
        drawCircle(g2d, positionX, slot.getPosition(), slotSize);

        g2d.setColor(Theme.theme().getSlotColor());
        drawCircle(g2d, positionX + slotBorderWidth, slot.getPosition() + slotBorderWidth, slotSize - 2 * slotBorderWidth);
    }

    private void drawCircle(Graphics2D g2d, int x, int y, int size) {
        g2d.fillRoundRect(x, y, size, size, size, size);
    }

    public class Header extends JPanel {
        Header() {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMinimumSize(new Dimension(100, 20));
            setPreferredSize(new Dimension(1000, 20));
            setMaximumSize(new Dimension(2000, 20));
        }
    }
}
