package com.manabreakstudios.discourse.ui.editor.nodes;

import com.manabreakstudios.discourse.ui.editor.NodeUI;
import com.manabreakstudios.discourse.ui.editor.Slot;

import java.awt.*;

import static com.manabreakstudios.discourse.ui.editor.Slot.Direction.INPUT;
import static com.manabreakstudios.discourse.ui.editor.Slot.Direction.OUTPUT;

public class ReplyChoiceNode extends NodeUI {

    public ReplyChoiceNode() {
        slots.add(new Slot(this, INPUT, 0, 13));
        slots.add(new Slot(this, OUTPUT, 1, 40));
        slots.add(new Slot(this, OUTPUT, 2, 60));

        setPreferredSize(new Dimension(200, 100));
    }
}
