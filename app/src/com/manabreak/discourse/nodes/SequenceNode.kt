package com.manabreak.discourse.nodes

import com.manabreak.node_editor.model.Slot
import javafx.scene.layout.Pane
import java.awt.image.BufferedImage
import java.util.*

class SequenceNode : AbstractNode("Sequence") {

    override val title = "Sequence"

    override val color = Nodes.SEQUENCE.color

    override val header: Pane
        get() = throw UnsupportedOperationException()

    override val icon: BufferedImage
        get() = throw UnsupportedOperationException()

    override val prefWidth = 100.0

    override val prefHeight = 200.0

    val inputSlot = newSlot(Slot.Direction.INPUT, 8.0, true)
    val outputSlot = newSlot(Slot.Direction.OUTPUT, 13.0, true)
    val sequenceSlots = ArrayList<Slot>()

    init {
    }

}