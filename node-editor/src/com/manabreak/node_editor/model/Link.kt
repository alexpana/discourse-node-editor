package com.manabreak.node_editor.model

data class Link(val from: Slot, val to: Slot) {

    fun other(slot: Slot): Slot {
        if (slot == from) {
            return to
        }

        if (slot == to) {
            return from
        }

        throw IllegalArgumentException("Slot $slot is not part of this link $this")
    }
}