package com.manabreak.node_editor

import com.manabreak.node_editor.model.Node
import com.manabreak.node_editor.model.Slot
import java.awt.Color
import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.swing.JPanel

interface NodeContent {
    val title: String

    val color: Color

    val content: JPanel

    val header: JPanel

    val icon: BufferedImage

    val slots: List<Slot>

    val preferredSize: Dimension

    val node: Node

    fun getSlotLocation(slot: Slot): Int
}
