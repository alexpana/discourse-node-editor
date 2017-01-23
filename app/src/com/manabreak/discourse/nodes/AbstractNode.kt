package com.manabreak.discourse.nodes

import com.manabreak.node_editor.model.Node
import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.ui.NodeContent
import java.util.*
import javax.swing.JPanel

abstract class AbstractNode constructor(name: String) : NodeContent {

    override val title = name

    override val slots = ArrayList<Slot>()

    override val node = Node(name)

    override val content = createContentPanel()

    val slotOffsets = HashMap<Slot, Int>()

    override fun getSlotLocation(slot: Slot): Int {
        return slotOffsets[slot]!!
    }

    fun newSlot(direction: Slot.Direction, verticalOffset: Int, allowMultipleLink: Boolean = false): Slot {
        val slot = node.addSlot(direction, allowMultipleLink)
        slots.add(slot)
        slotOffsets[slot] = verticalOffset
        return slot
    }

    private fun createContentPanel(): JPanel {
        val result = JPanel()
        result.isOpaque = false
        return result
    }
}