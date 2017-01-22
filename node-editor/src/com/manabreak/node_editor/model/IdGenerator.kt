package com.manabreak.node_editor.model

object IdGenerator {

    private var _nextId: Int = 0

    val nextId: Int
        get() {
            return _nextId++
        }
}