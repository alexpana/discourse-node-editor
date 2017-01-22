package com.manabreak.node_editor

import com.manabreak.node_editor.Renderer.drawLink
import com.manabreak.node_editor.Renderer.drawNodeHighlight
import com.manabreak.node_editor.Renderer.drawSelection
import com.manabreak.node_editor.SwingUtils.expand
import com.manabreak.node_editor.SwingUtils.getNodeUIAncestor
import com.manabreak.node_editor.SwingUtils.isInputTransparent
import com.manabreak.node_editor.SwingUtils.screenToLocal
import com.manabreak.node_editor.model.Node
import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.model.Slot.Direction.INPUT
import com.manabreak.node_editor.tools.CreateLinkTool
import java.awt.*
import java.awt.AWTEvent.MOUSE_EVENT_MASK
import java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK
import java.awt.Event.*
import java.awt.event.AWTEventListener
import java.awt.event.InputEvent.CTRL_MASK
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.JPanel

class NodeEditor : JPanel() {

    private val nodes = ArrayList<NodeUI<*>>()

    private val nodeModelToComponent = HashMap<Node, NodeUI<*>>()

    val linkManager = LinkManager()

    private val dragHelper = DragHelper()

    private val connectionHelper = CreateLinkTool(this)

    private val marqueeSelection = MarqueeSelection()

    private val grid = Grid()

    private val selection = SelectionModel(this)

    private val tempRect = Rectangle()

    init {
        layout = AbsoluteLayoutManager()
        Toolkit.getDefaultToolkit().addAWTEventListener(MouseDragListener(), MOUSE_MOTION_EVENT_MASK or MOUSE_EVENT_MASK)
    }

    fun addNode(nodeUI: NodeUI<*>, x: Int, y: Int) {
        add(nodeUI)
        nodes.add(nodeUI)
        nodeModelToComponent[nodeUI.model] = nodeUI
        nodeUI.setBounds(x, y, nodeUI.preferredSize.getWidth().toInt(), nodeUI.preferredSize.getHeight().toInt())
    }

    fun link(from: Slot, to: Slot) {
        linkManager.link(from, to)
        refresh()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        paintUnderNodes(g)
    }

    override fun paintChildren(g: Graphics) {
        super.paintChildren(g)
        paintOverNodes(g)
    }

    private fun paintUnderNodes(g: Graphics) {
        grid.paint(g, 0, 0, width, height)
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        for ((from, to) in linkManager.links) {
            val positionFrom = getSlotLocation(from)
            val positionTo = getSlotLocation(to)
            if (from.direction == INPUT) {
                drawLink(g2d, positionTo, positionFrom)
            } else {
                drawLink(g2d, positionFrom, positionTo)
            }
        }

        if (connectionHelper.isConnecting) {
            if (connectionHelper.from!!.direction == INPUT) {
                drawLink(g2d, connectionHelper.toLocation, connectionHelper.fromLocation)
            } else {
                drawLink(g2d, connectionHelper.fromLocation, connectionHelper.toLocation)
            }
        }

        for (nodeUI in selection.selectedNodes) {
            drawNodeHighlight(g2d, expand(nodeUI.getHitbox(tempRect), 2))
        }
    }

    private fun paintOverNodes(g: Graphics) {
        if (marqueeSelection.isSelecting) {
            drawSelection(g, marqueeSelection.rectangle)
        }
    }

    fun getSlotLocation(slot: Slot): Point {
        return getNodeUIForSlot(slot).getSlotLocation(slot)
    }

    private fun getNodeUIForSlot(slot: Slot): NodeUI<*> {
        return nodeModelToComponent[slot.node]!!
    }

    private fun onMouseDown(event: MouseEvent) {
        val localPoint = screenToLocal(event.locationOnScreen, this)
        val componentUnderCursor = SwingUtils.getComponentAt(this, localPoint)

        if (componentUnderCursor is SlotComponent) {
            connectionHelper.beginConnection(componentUnderCursor.slot)
        } else {
            val node = getNodeUnderCursor(localPoint, componentUnderCursor)

            if (node != null) {
                if (event.modifiers and CTRL_MASK != 0) {
                    selection.add(node)
                } else {
                    if (!node.selected) {
                        selection.setSelection(node)
                    }
                }

                if (isInputTransparent(componentUnderCursor)) {
                    beginDrag(localPoint)
                }
            } else {
                selection.clear()
                marqueeSelection.beginSelection(localPoint)
            }
        }
    }

    private fun mouseUp(event: MouseEvent) {
        val localPoint = screenToLocal(event.locationOnScreen, this)
        val componentUnderCursor = SwingUtils.getComponentAt(this, localPoint)

        if (componentUnderCursor is SlotComponent) {
            connectionHelper.endConnection(componentUnderCursor.slot)
        } else {
            connectionHelper.stop()
        }

        if (dragHelper.isDragging) {
            endDrag()
        } else {
            if (marqueeSelection.isSelecting) {
                marqueeSelection.finishSelection()
            }
        }

        refresh()
    }

    private fun mouseDrag(event: MouseEvent) {
        val localPoint = screenToLocal(event.locationOnScreen, this)
        updateDrag(localPoint)
        connectionHelper.update(localPoint)
        marqueeSelection.update(localPoint)
        if (marqueeSelection.isSelecting) {
            selection.selectFromMarquee(marqueeSelection.rectangle, nodes)
        }

        refresh()
    }

    private fun getNodeUnderCursor(cursor: Point, componentUnderCursor: Component): NodeUI<*>? {
        var node = getNodeUIAncestor(componentUnderCursor)
        if (componentUnderCursor is NodeUI<*> && !componentUnderCursor.getHitbox().contains(cursor)) {
            node = null
        }
        return node
    }

    fun refresh() {
        paintImmediately(bounds)
    }

    private fun beginDrag(mousePosition: Point) {
        dragHelper.beginDrag(mousePosition)
    }

    private fun endDrag() {
        dragHelper.endDrag()
    }

    private fun updateDrag(newMousePosition: Point) {
        if (!dragHelper.isDragging) {
            return
        }
        dragHelper.updateDrag(newMousePosition)

        val temp = Point()
        for (selectedNode in selection.selectedNodes) {
            selectedNode.getLocation(temp)
            temp.translate(dragHelper.deltaMove.x, dragHelper.deltaMove.y)
            selectedNode.location = temp
        }
    }

    private inner class MarqueeSelection {

        var isSelecting = false

        val from = Point()

        val to = Point()

        val rectangle = Rectangle()

        fun beginSelection(from: Point) {
            this.from.location = from
            isSelecting = true
        }

        fun update(update: Point) {
            if (!isSelecting) {
                return
            }

            to.location = update
            val x = Math.min(from.x, to.x)
            val y = Math.min(from.y, to.y)
            val width = Math.abs(to.x - from.x)
            val height = Math.abs(to.y - from.y)
            rectangle.setBounds(x, y, width, height)
        }

        fun finishSelection() {
            isSelecting = false
            rectangle.setRect(0.0, 0.0, 0.0, 0.0)
            from.setLocation(0, 0)
            to.setLocation(0, 0)
        }
    }

    private inner class MouseDragListener : AWTEventListener {

        override fun eventDispatched(awtEvent: AWTEvent) {
            val event = awtEvent as MouseEvent

            when (event.id) {
                MOUSE_DOWN -> onMouseDown(event)
                MOUSE_UP -> mouseUp(event)
                MOUSE_DRAG -> mouseDrag(event)
            }
        }
    }

    private class AbsoluteLayoutManager : LayoutManager {

        override fun addLayoutComponent(name: String, comp: Component) {
        }

        override fun removeLayoutComponent(comp: Component) {
        }

        override fun preferredLayoutSize(parent: Container): Dimension {
            return PREFERRED_DIMENSION
        }

        override fun minimumLayoutSize(parent: Container): Dimension {
            return PREFERRED_DIMENSION
        }

        override fun layoutContainer(parent: Container) {
        }

        companion object {

            val PREFERRED_DIMENSION = Dimension(1000, 600)
        }
    }
}
