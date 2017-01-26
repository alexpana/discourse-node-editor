package com.manabreak.node_editor.ui.tools

import com.manabreak.node_editor.ui.NodeEditor
import javafx.scene.input.MouseEvent
import java.awt.Graphics

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