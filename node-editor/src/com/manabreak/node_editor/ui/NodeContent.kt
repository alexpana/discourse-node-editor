package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Node
import com.manabreak.node_editor.model.Slot
import javafx.scene.layout.Region
import java.awt.Color
import java.awt.image.BufferedImage

interface NodeContent {
    val title: String

    val color: Color

    val content: Region

    val header: Region

    val icon: BufferedImage

    val slots: List<Slot>

    val prefWidth: Double

    val prefHeight: Double

    val node: Node

    fun getSlotLocation(slot: Slot): Double
}
