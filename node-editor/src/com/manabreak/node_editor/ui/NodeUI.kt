package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.ui.Theme.Companion.theme
import javafx.geometry.Point2D
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.text.Text
import java.awt.Rectangle
import java.util.*

class NodeUI<out T : NodeContent> constructor(val content: T) : Pane() {

    var selected = false

    val model = content.node

    private val slotComponents = ArrayList<SlotComponent>()

    private val header = Header(content)

    init {
        styleClass.addAll("node", "choice")
        setPrefSize(200.0, 100.0)

        children.add(this.content.content)
        children.add(header)

        for (slot in this.content.slots) {
            val slotComponent = SlotComponent(slot)
            children.add(slotComponent)
            slotComponents.add(slotComponent)
        }

        setPrefSize(this.content.prefWidth, this.content.prefHeight)
    }

    fun getSlot(index: Int): Slot {
        return content.slots[index]
    }

    fun getHitbox(): Rectangle {
        return getHitbox(Rectangle())
    }

    fun getHitbox(rv: Rectangle): Rectangle {
        rv.setRect((layoutX + getInset()), (layoutY + getInset()), (width - 2 * getInset()), (height - 2 * getInset()))
        return rv
    }

    private fun getInset(): Double {
        return 2.0
    }

    fun getSlotLocation(slot: Slot): Point2D {
        val positionX = if (slot.direction == Slot.Direction.INPUT) 0.0 else width - theme.slotSize
        return Point2D(layoutX + positionX + theme.slotSize / 2, layoutY + content.getSlotLocation(slot) + theme.slotSize / 2)
    }

    override fun layoutChildren() {
        val inset = getInset() + theme.nodeBorderWidth

        header.resizeRelocate(0.0, 0.0, width, 25.0)
        content.content.resizeRelocate(inset, inset + 25, width - 2 * inset, height - 2 * inset - 25)
        for (slotComponent in slotComponents) {
            val x = if (slotComponent.slot.direction === Slot.Direction.INPUT) 0.0 else width - theme.slotSize
            val y = content.getSlotLocation(slotComponent.slot)
            slotComponent.relocate(x, y)
        }
    }

    inner class Header internal constructor(val content: T) : Region() {
        init {
            styleClass.add("header")

            val title = Text(content.title)
            title.styleClass.add("title")
            title.resizeRelocate(18.0, 5.0, 100.0, height)
            children.add(title)
        }
    }
}