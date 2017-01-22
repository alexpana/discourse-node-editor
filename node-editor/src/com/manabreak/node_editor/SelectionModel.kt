package com.manabreak.node_editor

import java.awt.Rectangle
import java.util.*

class SelectionModel constructor(private val editor: NodeEditor) {

    val selectedNodes = ArrayList<NodeUI<*>>()

    fun clear() {
        clear(true)
    }

    private fun clear(refresh: Boolean) {
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

    fun selectFromMarquee(selectionRectangle: Rectangle, nodes: List<NodeUI<*>>) {
        for (node in selectedNodes) {
            node.selected = false
        }
        selectedNodes.clear()

        for (node in nodes) {
            if (selectionRectangle.contains(node.getHitbox())) {
                selectedNodes.add(node)
                node.selected = true
            }
        }
    }
}
