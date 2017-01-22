package com.manabreak.node_editor.ui

import javax.swing.*
import java.awt.*
import java.util.Arrays

object SwingUtils {

    private val INPUT_TRANSPARENT_COMPONENTS = Arrays.asList(JLabel::class.java, JPanel::class.java, JLayeredPane::class.java)

    fun screenToLocal(screen: Point, component: Component): Point {
        val result = Point(screen)
        SwingUtilities.convertPointFromScreen(result, component)
        return result
    }

    fun getNodeUIAncestor(component: Component): NodeUI<*>? {
        var ancestor: Component? = component
        while (ancestor != null) {
            if (ancestor is NodeUI<*>) {
                return ancestor
            }
            ancestor = ancestor.parent
        }
        return null
    }

    fun getComponentAt(parent: Component, point: Point): Component {
        return SwingUtilities.getDeepestComponentAt(parent, point.x, point.y)
    }

    fun isInputTransparent(component: Component): Boolean {
        for (aClass in INPUT_TRANSPARENT_COMPONENTS) {
            if (aClass.isAssignableFrom(component.javaClass)) {
                return true
            }
        }
        return false
    }

    fun expand(rect: Rectangle, size: Int): Rectangle {
        rect.setRect((rect.x - size).toDouble(), (rect.y - size).toDouble(), (rect.width + 2 * size).toDouble(), (rect.height + 2 * size).toDouble())
        return rect
    }

    fun drawCircle(g2d: Graphics2D, x: Int, y: Int, size: Int) {
        g2d.fillRoundRect(x, y, size, size, size, size)
    }
}
