package com.manabreak.discourse.nodes

import com.manabreak.node_editor.model.Slot
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.JPanel

class SequenceNode : AbstractNode("Sequence") {

    override val title = "Sequence"

    override val color = Nodes.SEQUENCE.color

    override val header: JPanel
        get() = throw UnsupportedOperationException()

    override val icon: BufferedImage
        get() = throw UnsupportedOperationException()

    override val preferredSize = Dimension(100, 200)

    val inputSlot = newSlot(Slot.Direction.INPUT, 13, true)
    val outputSlot = newSlot(Slot.Direction.OUTPUT, 13, true)
    val sequenceSlots = ArrayList<Slot>()

    init {
    }

}