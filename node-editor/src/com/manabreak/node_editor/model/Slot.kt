package com.manabreak.node_editor.model

data class Slot(
        val id: Int,
        val node: Node,
        val direction: Direction,
        val allowsMultipleLinks: Boolean) {

    enum class Direction {
        INPUT, OUTPUT
    }
}
