package com.manabreakstudios.discourse.ui.app.nodes;

import com.manabreakstudios.discourse.ui.core.nodeeditor.NodeUI;
import com.manabreakstudios.discourse.ui.core.nodeeditor.Slot;

import java.awt.*;

import static com.manabreakstudios.discourse.ui.core.nodeeditor.Slot.Direction.INPUT;
import static com.manabreakstudios.discourse.ui.core.nodeeditor.Slot.Direction.OUTPUT;

public class ReplyChoiceNode extends NodeUI {

    public ReplyChoiceNode() {
        slots.add(new Slot(INPUT, 0, 13));
        slots.add(new Slot(OUTPUT, 1, 40));
        slots.add(new Slot(OUTPUT, 2, 60));

        setPreferredSize(new Dimension(200, 100));
    }
}
