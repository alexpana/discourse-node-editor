package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Link
import com.manabreak.node_editor.model.Slot.Direction.OUTPUT
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.layout.Pane
import javafx.scene.shape.CubicCurve

class LinkComponent(val editor: NodeEditor, val link: Link) : Pane() {

    val resizeListener = BoundsListener(this)

    val curve = CubicCurveLink()

    val foregroundCurve: CubicCurve
        get() = curve.foregroundCurve

    val backgroundCurve: CubicCurve
        get() = curve.backgroundCurve

    private val nodeFrom: NodeUI<*> = editor.getNodeUIForSlot(link.from)
    private val nodeTo: NodeUI<*> = editor.getNodeUIForSlot(link.to)

    init {
        nodeFrom.layoutXProperty().addListener(resizeListener)
        nodeFrom.layoutYProperty().addListener(resizeListener)
        nodeFrom.widthProperty().addListener(resizeListener)
        nodeFrom.heightProperty().addListener(resizeListener)
        nodeTo.layoutXProperty().addListener(resizeListener)
        nodeTo.layoutYProperty().addListener(resizeListener)
        nodeTo.widthProperty().addListener(resizeListener)
        nodeTo.heightProperty().addListener(resizeListener)
        nodeBoundsChanged()
    }

    private fun nodeBoundsChanged() {
        val fromPoint = if (link.from.direction == OUTPUT) editor.getSlotLocation(link.from) else editor.getSlotLocation(link.to)
        val toPoint = if (link.from.direction == OUTPUT) editor.getSlotLocation(link.to) else editor.getSlotLocation(link.from)
        curve.update(fromPoint, toPoint)
    }

    class BoundsListener(val linkComponent: LinkComponent) : ChangeListener<Number> {
        override fun changed(observable: ObservableValue<out Number>?, oldValue: Number?, newValue: Number?) {
            linkComponent.nodeBoundsChanged()
        }
    }
}