package com.manabreak.discourse.nodes

import com.manabreak.node_editor.NodeContent
import com.manabreak.node_editor.Slot
import com.manabreak.node_editor.Slot.Direction.INPUT
import com.manabreak.node_editor.Slot.Direction.OUTPUT
import com.manabreak.node_editor.Theme
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

    override val slots = ArrayList<Slot>()

    override val content = JPanel()

    override val header = JPanel()

    override val icon = BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB)

    override val preferredSize = Dimension(200, 100)

    init {
        slots.add(Slot(INPUT, 0, 13))
        slots.add(Slot(OUTPUT, 1, 40))
        slots.add(Slot(OUTPUT, 2, 60))
        content.isOpaque = false
        content.layout = FlowLayout()
        content.add(JLabel("Choice: "))
        content.add(JTextField())
    }
}
