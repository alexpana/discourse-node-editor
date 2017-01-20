package com.manabreakstudios.discourse.ui.core.editor;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.manabreakstudios.discourse.ui.Theme.theme;

/**
 * A NodeUI is a UI component that holds a node.
 */
public class NodeUI extends JLayeredPane {

    private final NodeContent content;

    private final List<SlotComponent> slotComponentList = new ArrayList<>();

    private final Header header;

    @Getter @Setter
    private boolean isSelected = false;

    public NodeUI(NodeContent nodeContent) {
        this.content = nodeContent;
        this.header = new Header(content);

        setLayout(new ContentLayout());
        add(this.content.getContent(), JLayeredPane.DEFAULT_LAYER);
        add(header, JLayeredPane.DEFAULT_LAYER);

        for (Slot slot : this.content.getSlots()) {
            SlotComponent slotComponent = new SlotComponent(new SlotBinding(this, slot));
            add(slotComponent, JLayeredPane.POPUP_LAYER);
            slotComponentList.add(slotComponent);
        }

        setPreferredSize(this.content.getPreferredSize());
    }

    public SlotBinding getSlot(int index) {
        return new SlotBinding(this, content.getSlots().get(index));
    }

    @Override
    protected void paintComponent(Graphics g) {
        int inset = getInset();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(theme().getNodeBorderColor());
        g2d.fillRoundRect(inset, inset, getWidth() - 2 * inset, getHeight() - 2 * inset, 6, 6);

        g2d.setColor(theme().getNodeSpecularColor());
        g2d.fillRoundRect(inset + 2, inset + 2, getWidth() - 2 * inset - 4, getHeight() - 2 * inset - 4, 4, 4);

        g2d.setColor(theme().getNodeBackgroundColor());
        g2d.fillRoundRect(inset + 2, inset + 3, getWidth() - 2 * inset - 4, getHeight() - 2 * inset - 5, 4, 4);

        Font font = (Font) UIManager.getDefaults().get("Font.OpenSans-ExtraBold");

        g2d.setColor(content.getColor());
        g2d.setFont(font.deriveFont(12.0f));
        g2d.drawString(content.getTitle(), inset + 10, inset + 18);
    }

    private int getInset() {
        return theme().getSlotSize() / 2;
    }

    public class Header extends JPanel {
        Header(NodeContent nodeContent) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setMinimumSize(new Dimension(100, 20));
            setPreferredSize(new Dimension(1000, 20));
            setMaximumSize(new Dimension(2000, 20));
        }
    }

    public Rectangle getHitbox() {
        return getHitbox(new Rectangle());
    }

    public Rectangle getHitbox(Rectangle rv) {
        rv.setRect(getX() + getInset(), getY() + getInset(), getWidth() - 2 * getInset(), getHeight() - 2 * getInset());
        return rv;
    }

    public class ContentLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return content.getPreferredSize();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            int inset = getInset();
            header.setBounds(inset, inset, getWidth() - 2 * inset, 20);
            content.getContent().setBounds(inset, inset + 20, getWidth() - 2 * inset, getHeight() - 2 * inset - 20);
            for (SlotComponent slotComponent : slotComponentList) {
                int positionX = slotComponent.getSlot().getDirection() == Slot.Direction.INPUT ? 0 : (getWidth() - theme().getSlotSize());
                slotComponent.setBounds(new Rectangle(positionX, slotComponent.getSlot().getPosition(), theme().getSlotSize(), theme().getSlotSize()));
            }
        }
    }
}
