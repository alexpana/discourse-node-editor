package com.manabreak.node_editor.ui

import javafx.geometry.Rectangle2D
import java.util.*

class SelectionModel constructor(private val editor: NodeEditor) {

    val selectedNodes = ArrayList<NodeUI<*>>()

    fun clear() {
        clear(true)
    }

    fun clear(refresh: Boolean) {
        for (selectedNode in selectedNodes) {
            selectedNode.selected = false
        }
        selectedNodes.clear()

        if (refresh) {
            editor.refresh()
        }
    }

    fun add(node: NodeUI<*>) {
        add(node, true)
    }

    private fun add(node: NodeUI<*>, refresh: Boolean) {
        if (!node.selected) {
            node.selected = true
            selectedNodes.add(node)
            if (refresh) {
                editor.refresh()
            }
        }
    }

    fun setSelection(vararg nodes: NodeUI<*>) {
        clear(false)

        for (node in nodes) {
            add(node, false)
        }

        editor.refresh()
    }

    fun selectFromMarquee(selectionRectangle: Rectangle2D, nodes: List<NodeUI<*>>) {
        for (node in selectedNodes) {
            node.selected = false
        }
        selectedNodes.clear()

        for (node in nodes) {
            if (selectionRectangle.contains(node.layoutX, node.layoutY, node.width, node.height)) {
                selectedNodes.add(node)
                node.selected = true
            }
        }
    }
}
