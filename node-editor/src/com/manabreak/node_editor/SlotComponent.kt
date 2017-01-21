package com.manabreak.node_editor

import com.manabreak.node_editor.Renderer.createSlotImage
import com.manabreak.node_editor.Theme.Companion.theme
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.swing.JPanel

class SlotComponent constructor(val slotBinding: SlotBinding) : JPanel() {

    var state = State.ACCEPT

    val slot: Slot
        get() {
            return slotBinding.slot
        }

    override fun paintComponent(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.drawImage(state.image, 0, 0, null)
    }

    enum class State constructor(image: BufferedImage) {
        NORMAL(createSlotImage(theme.slotDefaultColor)),
        ACCEPT(createSlotImage(theme.slotAcceptColor)),
        DECLINE(createSlotImage(theme.slotDeclineColor));

        val image: BufferedImage

        init {
            this.image = image
        }
    }
}
