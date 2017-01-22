package com.manabreak.discourse.nodes

import com.manabreak.node_editor.NodeContent
import com.manabreak.node_editor.Theme
import com.manabreak.node_editor.model.Node
import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.model.Slot.Direction.INPUT
import com.manabreak.node_editor.model.Slot.Direction.OUTPUT
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField


class ReplyChoiceNode : NodeContent {

    override val title = "Reply Choice"

    override val color = Theme.theme.choiceNodeColor

    override val slots: ArrayList<Slot>
        get() = node.slots

    override val content = JPanel()

    override val header = JPanel()

    override val icon = BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB)

    override val preferredSize = Dimension(200, 100)

    override val node = Node("Reply Choice")

    private val slotOffsets = HashMap<Slot, Int>()

    val input = newSlot(INPUT, 13, true)
    val firstOutput = newSlot(OUTPUT, 40)
    val secondOutput = newSlot(OUTPUT, 60)

    init {
        slots.add(input)
        slots.add(firstOutput)
        slots.add(secondOutput)

        content.isOpaque = false
        content.layout = FlowLayout()
        content.add(JLabel("Choice: "))
        content.add(JTextField())
    }

    private fun newSlot(direction: Slot.Direction, verticalOffset: Int, allowMultipleLink: Boolean = false): Slot {
        val slot = node.addSlot(direction, allowMultipleLink)
        slotOffsets[slot] = verticalOffset
        return slot
    }

    override fun getSlotLocation(slot: Slot): Int {
        return slotOffsets[slot]!!
    }
}
