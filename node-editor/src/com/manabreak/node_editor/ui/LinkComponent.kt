package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Link
import com.manabreak.node_editor.model.Slot.Direction.OUTPUT
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Point2D
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.CubicCurve

class LinkComponent(val editor: NodeEditor, val link: Link) : Pane() {

    val resizeListener = ResizeListener(this)

    val backgroundCurve: CubicCurve = CubicCurve()
    val foregroundCurve: CubicCurve = CubicCurve()

    private val nodeFrom: NodeUI<*> = editor.getNodeUIForSlot(link.from)
    private val nodeTo: NodeUI<*> = editor.getNodeUIForSlot(link.to)

    init {
        backgroundCurve.strokeWidth = 5.0
        backgroundCurve.stroke = Theme.theme.nodeBorderColor
        backgroundCurve.fill = Color.TRANSPARENT

        foregroundCurve.strokeWidth = 1.0
        foregroundCurve.stroke = Theme.theme.linkColor
        foregroundCurve.fill = Color.TRANSPARENT

        nodeFrom.widthProperty().addListener(resizeListener)
        nodeFrom.heightProperty().addListener(resizeListener)
        nodeTo.widthProperty().addListener(resizeListener)
        nodeTo.heightProperty().addListener(resizeListener)
        onNodeResize()
    }

    private fun onNodeResize() {
        val fromPoint = if (link.from.direction == OUTPUT) editor.getSlotLocation(link.from) else editor.getSlotLocation(link.to)
        val toPoint = if (link.from.direction == OUTPUT) editor.getSlotLocation(link.to) else editor.getSlotLocation(link.from)
        updateCurveParams(backgroundCurve, fromPoint, toPoint)
        updateCurveParams(foregroundCurve, fromPoint, toPoint)
    }

    private fun updateCurveParams(curve: CubicCurve, from: Point2D, to: Point2D) {
        val horizontalDistance = Math.abs(from.x - to.x)
        val handleOffset = Math.max(horizontalDistance / 2, 20.0)

        curve.startX = from.x
        curve.startY = from.y
        curve.controlX1 = from.x + handleOffset
        curve.controlY1 = from.y
        curve.controlX2 = to.x - handleOffset
        curve.controlY2 = to.y
        curve.endX = to.x
        curve.endY = to.y
    }

    class ResizeListener(val linkComponent: LinkComponent) : ChangeListener<Number> {
        override fun changed(observable: ObservableValue<out Number>?, oldValue: Number?, newValue: Number?) {
            linkComponent.onNodeResize()
        }
    }
}