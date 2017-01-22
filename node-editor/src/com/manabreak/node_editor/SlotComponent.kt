package com.manabreak.node_editor

import com.manabreak.node_editor.Renderer.createSlotImage
import com.manabreak.node_editor.Theme.Companion.theme
import com.manabreak.node_editor.model.Slot
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.swing.JPanel

class SlotComponent constructor(val slot: Slot) : JPanel() {

    var state = State.NORMAL

    override fun paintComponent(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.drawImage(state.image, 0, 0, null)
    }

    enum class State constructor(image: BufferedImage) {
        NORMAL(createSlotImage(theme.nodeBorderColor, theme.slotDefaultColor, theme.slotBorderWidth)),
        ACCEPT(createSlotImage(theme.nodeBorderColor, theme.slotAcceptColor, theme.slotBorderWidth)),
        DECLINE(createSlotImage(theme.nodeBorderColor, theme.slotDeclineColor, theme.slotBorderWidth));

        val image: BufferedImage

        init {
            this.image = image
        }
    }
}
