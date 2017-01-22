package com.manabreak.node_editor.ui

import com.manabreak.node_editor.ui.Renderer.drawLink
import com.manabreak.node_editor.ui.Renderer.drawNodeHighlight
import com.manabreak.node_editor.ui.SwingUtils.expand
import com.manabreak.node_editor.ui.SwingUtils.isInputTransparent
import com.manabreak.node_editor.ui.SwingUtils.screenToLocal
import com.manabreak.node_editor.ui.layout.AbsoluteLayoutManager
import com.manabreak.node_editor.model.Node
import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.ui.tools.CreateLinkTool
import com.manabreak.node_editor.ui.tools.DragNodeTool
import com.manabreak.node_editor.ui.tools.MarqueeSelectionTool
import com.manabreak.node_editor.ui.tools.Tool
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

    init {
        layout = AbsoluteLayoutManager(Dimension(1000, 1000))
        Toolkit.getDefaultToolkit().addAWTEventListener(MouseDragListener(), MOUSE_MOTION_EVENT_MASK or MOUSE_EVENT_MASK)
    }

    fun addNode(nodeUI: NodeUI<*>, x: Int, y: Int) {
        add(nodeUI)
        nodes.add(nodeUI)
        nodeModelToComponent[nodeUI.model] = nodeUI
        nodeUI.setBounds(x, y, nodeUI.preferredSize.getWidth().toInt(), nodeUI.preferredSize.getHeight().toInt())
    }

    fun getSlotLocation(slot: Slot): Point {
        return getNodeUIForSlot(slot).getSlotLocation(slot)
    }

    fun getNodeUIForSlot(slot: Slot): NodeUI<*> {
        return nodeModelToComponent[slot.node]!!
    }

    fun getNodeUIUnderCursor(cursor: Point): NodeUI<*>? {
        return nodes.find { it.getHitbox().contains(cursor) }
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
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // draw grid
        grid.paint(g, 0, 0, width, height)

        // draw links
        for ((from, to) in linkManager.links) {
            drawLink(g2d, from, to, position = { getSlotLocation(it) })
        }

        // draw selection highlights
        for (nodeUI in selection.selectedNodes) {
            drawNodeHighlight(g2d, expand(nodeUI.getHitbox(tempRect), 2))
        }

        activeTool?.paintUnderNodes(g)
    }

    private fun paintOverNodes(g: Graphics) {
        activeTool?.paintOverNodes(g)
    }

    private fun onMouseDown(event: MouseEvent) {
        val localPoint = screenToLocal(event.locationOnScreen, this)
        val componentUnderCursor = SwingUtils.getComponentAt(this, localPoint)

        if (componentUnderCursor is SlotComponent) {
            activeTool = connectionHelper
        } else {
            val node = getNodeUIUnderCursor(localPoint)
            if (node != null) {
                if (event.modifiers and CTRL_MASK != 0) {
                    selection.add(node)
                } else {
                    if (!node.selected) {
                        selection.setSelection(node)
                    }
                }
                if (isInputTransparent(componentUnderCursor)) {
                    activeTool = dragHelper
                }
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
        paintImmediately(bounds)
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
}
