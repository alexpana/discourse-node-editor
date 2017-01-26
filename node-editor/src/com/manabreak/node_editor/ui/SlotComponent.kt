package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Slot
import javafx.scene.layout.Pane

class SlotComponent constructor(val slot: Slot) : Pane() {

    private var _state = State.NORMAL

    var state: State
        get() = _state
        set(value) {
            if (_state != value) {
                styleClass.remove(_state.styleClass)
                _state = value
                styleClass.add(_state.styleClass)
            }
        }

    init {
        styleClass.addAll("slot", state.styleClass)
        resize(12.0, 12.0)
    }

    enum class State constructor(styleClass: String) {
        NORMAL("normal"),
        ACCEPT("accept"),
        DECLINE("decline");

        val styleClass: String

        init {
            this.styleClass = styleClass
        }
    }
}
