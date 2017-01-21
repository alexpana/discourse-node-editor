package com.manabreak.node_editor

import javax.swing.*
import java.awt.*
import java.awt.image.BufferedImage

interface NodeContent {
    val title: String

    val color: Color

    val content: JPanel

    val header: JPanel

    val icon: BufferedImage

    val slots: List<Slot>

    val preferredSize: Dimension
}
