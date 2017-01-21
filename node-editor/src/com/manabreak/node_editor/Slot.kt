package com.manabreak.node_editor

data class Slot(val direction: Slot.Direction, val index: Int, val position: Int) {
    enum class Direction {
        INPUT, OUTPUT
    }
}
