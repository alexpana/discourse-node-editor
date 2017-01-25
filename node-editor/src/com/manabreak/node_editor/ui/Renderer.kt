package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.model.Slot.Direction.INPUT
import com.manabreak.node_editor.ui.Theme.Companion.theme
import java.awt.*

internal object Renderer {

    val SELECTION_BACKGROUND = Color(0x10FFB384, true)

    val SELECTION_BORDER = Color(0x90FFB384.toInt(), true)

    fun drawLink(g: Graphics, from: Slot, to: Slot, position: (Slot) -> Point) {
        if (from.direction == INPUT) {
            drawLink(g, position(to), position(from))
        } else {
            drawLink(g, position(from), position(to))
        }
    }

    fun drawLink(g: Graphics, positionFrom: Point, positionTo: Point) {
//        val horizontalDistance = Math.abs(positionFrom.x - positionTo.x)
//        val handleOffset = Math.max(horizontalDistance / 2, 20)
//
//        val cubicCurve2D = CubicCurve2D.Float(
//                positionFrom.x.toFloat(), positionFrom.y.toFloat(),
//                (positionFrom.x + handleOffset).toFloat(), positionFrom.y.toFloat(),
//                (positionTo.x - handleOffset).toFloat(), positionTo.y.toFloat(),
//                positionTo.x.toFloat(), positionTo.y.toFloat())
//
//        val g2d = g as Graphics2D;
//        g2d.color = theme.nodeBorderColor
//        g2d.stroke = BasicStroke(5f)
//        g2d.draw(cubicCurve2D)
//
//        g2d.color = theme.linkColor
//        g2d.stroke = BasicStroke(1.5f)
//        g2d.draw(cubicCurve2D)
    }

    fun drawSelection(g: Graphics, rectangle: Rectangle) {
        //        Graphics2D g2d = (Graphics2D) g.create();
        //
        //        g2d.setColor(theme().getMarqueeSelectColor());
        //        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10, new float[]{10, 10}, 0));
        //        g2d.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        //
        //        g2d.dispose();

        val g2d = g.create() as Graphics2D
        g2d.stroke = BasicStroke(1f)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF)

        g2d.color = SELECTION_BACKGROUND
        g2d.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)

        g2d.color = SELECTION_BORDER
        g2d.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
        g2d.dispose()
    }

    fun drawNodeHighlight(g: Graphics, rectangle: Rectangle) {
        val g2d = g.create() as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.color = theme.selectionColor
        g2d.fillRoundRect(rectangle.x + 1, rectangle.y + 1, rectangle.width - 2, rectangle.height - 2, 6, 6)
        g2d.dispose()
    }

    fun drawCircle(g2d: Graphics2D, x: Int, y: Int, size: Int) {
        g2d.fillRoundRect(x, y, size, size, size, size)
    }
}
