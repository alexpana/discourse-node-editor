package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.ui.Theme.Companion.theme
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.text.Text
import java.util.*

class NodeUI<out T : NodeContent> constructor(val content: T) : Pane() {

    private var _selected = false
    var selected: Boolean
        get() = _selected
        set(value) {
            if (value != _selected) {
                if (value) {
                    styleClass.add("selected")
                } else {
                    styleClass.remove("selected")
                }
            }
            _selected = value
        }

    val model = content.node

    val slotComponents = ArrayList<SlotComponent>()

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

    fun getSlots(): List<Slot> {
        return content.slots
    }

    fun getSlot(index: Int): Slot {
        return content.slots[index]
    }

    fun getHitbox(): Rectangle2D {
        return Rectangle2D((layoutX + getInset()), (layoutY + getInset()), (width - 2 * getInset()), (height - 2 * getInset()))
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