package com.manabreak.node_editor.model

import com.manabreak.node_editor.model.IdGenerator.nextId
import java.util.*

class Node(val name: String) {

    val slots = ArrayList<Slot>()

    fun addSlot(direction: Slot.Direction): Slot {
        return addSlot(direction, false)
    }

    fun addSlot(direction: Slot.Direction, allowMultipleLinks: Boolean): Slot {
        val slot = Slot(nextId, this, direction, allowMultipleLinks)
        slots.add(slot)
        return slot
    }
}