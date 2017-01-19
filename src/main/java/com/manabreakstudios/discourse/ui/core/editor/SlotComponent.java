package com.manabreakstudios.discourse.ui.core.editor;

import com.manabreakstudios.discourse.ui.Theme;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;

import static com.manabreakstudios.discourse.ui.core.utils.SwingUtils.drawCircle;

@RequiredArgsConstructor
public class SlotComponent extends JPanel {

    @Getter
    private final SlotBinding slotBinding;

    public Slot getSlot() {
        return slotBinding.getSlot();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int slotSize = Theme.theme().getSlotSize();
        int slotBorderWidth = Theme.theme().getSlotBorderWidth();

        g2d.setColor(Theme.theme().getNodeBorderColor());
        drawCircle(g2d, 0, 0, slotSize);

        g2d.setColor(Theme.theme().getSlotColor());
        drawCircle(g2d, slotBorderWidth, slotBorderWidth, slotSize - 2 * slotBorderWidth);
    }
}
