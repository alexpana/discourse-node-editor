package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Slot
import javafx.scene.layout.Pane

class SlotComponent constructor(val slot: Slot) : Pane() {
    init {
        styleClass.add("slot")
        resize(12.0, 12.0)
    }
}
