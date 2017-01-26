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
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
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

    val foregroundPane = Pane()

    val foregroundCanvas = Canvas()

    val linkComponents = HashMap<Link, LinkComponent>()

    init {
        foregroundCanvas.graphicsContext2D.translate(0.5, 0.5)
//        foregroundCanvas.isPickOnBounds = false
        foregroundCanvas.isMouseTransparent = true
        foregroundPane.isMouseTransparent = true

        onMousePressed = EventHandler<MouseEvent> { mouseDown(it) }
        onMouseReleased = EventHandler<MouseEvent> { mouseUp(it) }
        onMouseDragged = EventHandler<MouseEvent> { mouseDrag(it) }
        onMouseClicked = EventHandler<MouseEvent> { mouseClicked(it) }

        styleClass.add("editor")
        linkManager.listeners.add(this)

        foregroundPane.translateZ = -10.0
        foregroundPane.children.add(foregroundCanvas)

        backgroundPane.translateZ = 0.0
        children.add(backgroundPane)
        children.add(foregroundPane)
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
        nodeUI.translateZ = 1.0
        children.add(1, nodeUI)
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

    private fun mouseClicked(event: MouseEvent) {
        val nodeUI = getNodeUIUnderCursor(Point2D(event.x, event.y))
        if (nodeUI?.selected ?: false) {
            selection.setSelection(nodeUI!!)
        }
    }

    override fun layoutChildren() {
        backgroundPane.resizeRelocate(0.0, 0.0, boundsInLocal.width, boundsInLocal.height)
        foregroundPane.resizeRelocate(0.0, 0.0, boundsInLocal.width, boundsInLocal.height)
        foregroundCanvas.layoutX = 0.0
        foregroundCanvas.layoutY = 0.0
        foregroundCanvas.width = boundsInLocal.width
        foregroundCanvas.height = boundsInLocal.height
    }

    fun refresh() {
    }
}
