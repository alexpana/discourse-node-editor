package com.manabreak.node_editor.tools

import com.manabreak.node_editor.NodeEditor
import java.awt.Graphics
import java.awt.event.MouseEvent

abstract class Tool(val editor: NodeEditor) {

    open fun paintUnderNodes(g: Graphics) {
    }

    open fun paintOverNodes(g: Graphics) {
    }

    open fun onMouseDown(event: MouseEvent) {
    }

    open fun onMouseDrag(event: MouseEvent) {
    }

    open fun onMouseUp(event: MouseEvent) {
    }
}