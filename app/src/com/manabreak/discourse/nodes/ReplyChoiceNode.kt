package com.manabreak.discourse.nodes

import com.manabreak.node_editor.model.Node
import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.model.Slot.Direction.INPUT
import com.manabreak.node_editor.model.Slot.Direction.OUTPUT
import com.manabreak.node_editor.ui.Theme
import javafx.scene.layout.Pane
import java.awt.image.BufferedImage
import java.util.*


class ReplyChoiceNode : AbstractNode("Reply Choice") {

    override val color = Theme.theme.choiceNodeColor

    override val slots: ArrayList<Slot>
        get() = node.slots

    override val content = Pane()

    override val header = Pane()

    override val icon = BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB)

    override val prefWidth = 200.0

    override val prefHeight = 100.0

    override val node = Node("Reply Choice")

    val input = newSlot(INPUT, 8.0, true)

    val firstOutput = newSlot(OUTPUT, 40.0)

    val secondOutput = newSlot(OUTPUT, 60.0)
}
