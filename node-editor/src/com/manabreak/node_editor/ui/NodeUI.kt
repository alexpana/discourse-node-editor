package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.ui.Theme.Companion.theme
import java.awt.*
import java.util.*
import javax.swing.JLayeredPane
import javax.swing.JPanel

class NodeUI<out T : NodeContent> constructor(val content: T) : JLayeredPane() {

    var selected = false

    val model = content.node

    private val slotComponents = ArrayList<SlotComponent>()

    private val header = Header()

    private val children = ArrayList<Component>()

    init {
        layout = ContentLayout()
        for (slot in this.content.slots) {
            val slotComponent = SlotComponent(slot)
            add(slotComponent, JLayeredPane.POPUP_LAYER)
            children.add(slotComponent)
            slotComponents.add(slotComponent)
        }

        add(this.content.content, JLayeredPane.DEFAULT_LAYER)
        children.add(this.content.content)
        add(header, JLayeredPane.DEFAULT_LAYER)
        children.add(header)

        preferredSize = this.content.preferredSize
    }

    override fun paintComponent(g: Graphics) {
        val inset = getInset()
        val border = theme.nodeBorderWidth

        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g2d.color = theme.nodeBorderColor
        g2d.fillRoundRect(inset, inset, width - 2 * inset, height - 2 * inset, 6, 6)

        g2d.color = theme.nodeSpecularColor
        g2d.fillRoundRect(inset + border, inset + border, width - 2 * (inset + border), height - 2 * (inset + border), 4, 4)

        g2d.color = theme.nodeBackgroundColor
        g2d.fillRoundRect(inset + border, inset + border + 1, width - 2 * (inset + border), height - 2 * (inset + border) - 1, 4, 4)

        val font = theme.boldFont

        g2d.color = content.color
        g2d.font = font.deriveFont(12.0f)
        g2d.drawString(content.title, inset + 10, inset + 18)
    }

    override fun getComponents(): Array<out Component>? {
        return children.toTypedArray()
    }

    fun getSlot(index: Int): Slot {
        return content.slots[index]
    }

    fun getHitbox(): Rectangle {
        return getHitbox(Rectangle())
    }

    fun getHitbox(rv: Rectangle): Rectangle {
        rv.setRect((x + getInset()).toDouble(), (y + getInset()).toDouble(), (width - 2 * getInset()).toDouble(), (height - 2 * getInset()).toDouble())
        return rv
    }

    private fun getInset(): Int {
        return theme.slotSize / 2
    }

    fun getSlotLocation(slot: Slot): Point {
        val positionX = if (slot.direction == Slot.Direction.INPUT) 0 else width - theme.slotSize
        return Point(x + positionX + theme.slotSize / 2, y + content.getSlotLocation(slot) + theme.slotSize / 2)
    }

    inner class Header internal constructor() : JPanel() {
        init {
            layout = BorderLayout()
            isOpaque = false
            minimumSize = Dimension(100, 20)
            preferredSize = Dimension(1000, 20)
            maximumSize = Dimension(2000, 20)
        }
    }

    inner class ContentLayout : LayoutManager {

        override fun addLayoutComponent(name: String, comp: Component) {
        }

        override fun removeLayoutComponent(comp: Component) {
        }

        override fun preferredLayoutSize(parent: Container): Dimension {
            return content.preferredSize
        }

        override fun minimumLayoutSize(parent: Container): Dimension {
            return preferredLayoutSize(parent)
        }

        override fun layoutContainer(parent: Container) {
            val inset = getInset() + theme.nodeBorderWidth
            header.setBounds(inset, inset, width - 2 * inset, 20)
            content.content.setBounds(inset, inset + 20, width - 2 * inset, height - 2 * inset - 20)
            for (slotComponent in slotComponents) {
                val bounds = getSlotBounds(slotComponent)
                slotComponent.bounds = bounds
            }
        }

        private fun getSlotBounds(slotComponent: SlotComponent): Rectangle {
            val positionX = if (slotComponent.slot.direction === Slot.Direction.INPUT) 0 else width - theme.slotSize
            val bounds = Rectangle(positionX, content.getSlotLocation(slotComponent.slot), theme.slotSize, theme.slotSize)
            return bounds
        }
    }
}