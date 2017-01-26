package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Link
import com.manabreak.node_editor.model.Node
import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.ui.tools.CreateLinkTool
import com.manabreak.node_editor.ui.tools.DragNodeTool
import com.manabreak.node_editor.ui.tools.MarqueeSelectionTool
import com.manabreak.node_editor.ui.tools.Tool
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import java.awt.Graphics
import java.awt.Rectangle
import java.util.*

class NodeEditor : Pane(), LinkManager.Listener {

    val nodes = ArrayList<NodeUI<*>>()

    private val nodeModelToComponent = HashMap<Node, NodeUI<*>>()

    private val dragHelper = DragNodeTool(this)

    private val connectionHelper = CreateLinkTool(this)

    private val marqueeSelection = MarqueeSelectionTool(this)

    private val grid = Grid()

    val selection = SelectionModel(this)

    private val tempRect = Rectangle()

    private var activeTool: Tool? = null

    val linkManager = LinkManager()

    val backgroundPane = Pane()

    val linkComponents = HashMap<Link, LinkComponent>()

    init {

        onMousePressed = EventHandler<MouseEvent> { mouseDown(it) }
        onMouseReleased = EventHandler<MouseEvent> { mouseUp(it) }
        onMouseDragged = EventHandler<MouseEvent> { mouseDrag(it) }

        styleClass.add("editor")
        children.add(backgroundPane)
        linkManager.listeners.add(this)
    }

    override fun linkCreated(link: Link) {
        val linkComponent = LinkComponent(this, link)
        linkComponents[link] = linkComponent
        backgroundPane.children.add(linkComponent.backgroundCurve)
        backgroundPane.children.add(linkComponent.foregroundCurve)
    }

    override fun linkRemoved(link: Link) {
        backgroundPane.children.remove(linkComponents[link]?.backgroundCurve)
        backgroundPane.children.remove(linkComponents[link]?.foregroundCurve)
        linkComponents.remove(link)
    }

    fun addNode(nodeUI: NodeUI<*>, x: Double, y: Double) {
        children.add(nodeUI)
        nodes.add(nodeUI)
        nodeModelToComponent[nodeUI.model] = nodeUI
        nodeUI.resizeRelocate(x, y, nodeUI.prefWidth, nodeUI.prefHeight)
    }

    fun getSlotLocation(slot: Slot): Point2D {
        return getNodeUIForSlot(slot).getSlotLocation(slot)
    }

    fun getNodeUIForSlot(slot: Slot): NodeUI<*> {
        return nodeModelToComponent[slot.node]!!
    }

    fun getNodeUIUnderCursor(cursor: Point2D): NodeUI<*>? {
        return nodes.find { it.getHitbox().contains(cursor) }
    }

    private fun paintOverNodes(g: Graphics) {
        activeTool?.paintOverNodes(g)
    }

    private fun mouseDown(event: MouseEvent) {
        val localPoint = Point2D(event.x, event.y)
        val componentUnderCursor = event.target

        if (componentUnderCursor is SlotComponent) {
            activeTool = connectionHelper
        } else {
            val node = getNodeUIUnderCursor(localPoint)
            if (node != null) {
                if (event.isControlDown) {
                    selection.add(node)
                } else {
                    if (!node.selected) {
                        selection.setSelection(node)
                    }
                }
//                if (isInputTransparent(componentUnderCursor)) {
                activeTool = dragHelper
//                }
            } else {
                selection.clear(false)
                activeTool = marqueeSelection
                marqueeSelection.beginSelection(localPoint)
            }
        }

        activeTool?.onMouseDown(event)
        refresh()
    }

    private fun mouseUp(event: MouseEvent) {
        activeTool?.onMouseUp(event)
        activeTool = null
        refresh()
    }

    private fun mouseDrag(event: MouseEvent) {
        activeTool?.onMouseDrag(event)
        refresh()
    }

    fun refresh() {
    }
}
