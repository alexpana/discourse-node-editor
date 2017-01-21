package com.manabreak.node_editor

import com.manabreak.node_editor.Theme.Companion.theme
import java.awt.*
import java.util.*
import javax.swing.JLayeredPane
import javax.swing.JPanel

class NodeUI constructor(private val content: NodeContent) : JLayeredPane() {

    var selected = false

    private val slotComponentList = ArrayList<SlotComponent>()

    private val header = Header(content)

    override fun paintComponent(g: Graphics) {
        val inset = getInset()

        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g2d.color = theme.nodeBorderColor
        g2d.fillRoundRect(inset, inset, width - 2 * inset, height - 2 * inset, 6, 6)

        g2d.color = theme.nodeSpecularColor
        g2d.fillRoundRect(inset + 2, inset + 2, width - 2 * inset - 4, height - 2 * inset - 4, 4, 4)

        g2d.color = theme.nodeBackgroundColor
        g2d.fillRoundRect(inset + 2, inset + 3, width - 2 * inset - 4, height - 2 * inset - 5, 4, 4)

        val font = theme.boldFont

        g2d.color = content.color
        g2d.font = font.deriveFont(12.0f)
        g2d.drawString(content.title, inset + 10, inset + 18)
    }

    init {
        layout = ContentLayout()
        add(this.content.content, JLayeredPane.DEFAULT_LAYER)
        add(header, JLayeredPane.DEFAULT_LAYER)

        for (slot in this.content.slots) {
            val slotComponent = SlotComponent(SlotBinding(this, slot))
            add(slotComponent, JLayeredPane.POPUP_LAYER)
            slotComponentList.add(slotComponent)
        }

        preferredSize = this.content.preferredSize
    }

    fun getSlot(index: Int): SlotBinding {
        return SlotBinding(this, content.slots[index])
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

    inner class Header internal constructor(nodeContent: NodeContent) : JPanel() {
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
            val inset = getInset()
            header.setBounds(inset, inset, width - 2 * inset, 20)
            content.content.setBounds(inset, inset + 20, width - 2 * inset, height - 2 * inset - 20)
            for (slotComponent in slotComponentList) {
                val positionX = if (slotComponent.slot.direction === Slot.Direction.INPUT) 0 else width - theme.slotSize
                slotComponent.bounds = Rectangle(positionX, slotComponent.slot.position, theme.slotSize, theme.slotSize)
            }
        }
    }
}