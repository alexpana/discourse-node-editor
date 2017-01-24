package com.manabreak.discourse.app

import javafx.application.Application
import javafx.geometry.Point2D
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.CubicCurve
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.transform.Affine
import javafx.scene.transform.Translate
import javafx.stage.Stage
import java.util.*

object DiscourseFxLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        Application.launch(DiscourseApplication::class.java, *args)
    }

    class DiscourseApplication : Application() {

        var font: Font? = null

        override fun start(stage: Stage?) {
            stage!!.scene = Scene(NodeEditor())

            loadFont("fontawesome-webfont")
            loadFont("OpenSans-ExtraBold")

            stage.title = "Discourse 1.0"
            stage.scene.stylesheets.add("css/style.css")

            stage.width = 800.0
            stage.height = 600.0

            stage.show()
        }

        private fun loadFont(fontName: String) {
            val url = DiscourseApplication::class.java.classLoader.getResource("fonts/$fontName.ttf").toExternalForm()
            Font.loadFont(url, 20.0)
        }
    }

    class Grid : Canvas() {
        val size = 30

        val links = ArrayList<Link>()

        fun repaint() {
            val firstVisibleLine = 0
            val firstVisibleColumn = 0

            val g2d = graphicsContext2D

            val minor = Color.web("#424548")
            val major = Color.web("#464A4D")

            g2d.lineWidth = 0.5
            g2d.transform = Affine(Translate(0.5, 0.5))

            g2d.fill = Color.web("#3c3f41")
            g2d.rect(0.0, 0.0, width, height)

            run {
                var l = 0
                while (firstVisibleLine + l * size < height) {
                    val lineY = (firstVisibleLine + l * size).toDouble()
                    g2d.stroke = if (l % 4 == 0) major else minor
                    g2d.strokeLine(0.0, lineY, width, lineY)
                    l += 1
                }
            }
            run {
                var c = 0
                while (firstVisibleColumn + c * size < width) {
                    val lineX = (firstVisibleColumn + c * size).toDouble()
                    g2d.stroke = if (c % 4 == 0) major else minor
                    g2d.strokeLine(lineX, 0.0, lineX, height)
                    c += 1
                }
            }
        }

        fun curve(from: Point2D, to: Point2D, width: Double, color: Color): CubicCurve {
            val horizontalDistance = Math.abs(from.x - to.x)
            val handleOffset = Math.max(horizontalDistance / 2, 20.0)

            val curve = CubicCurve()
            curve.styleClass.add("link")
            curve.startX = from.x
            curve.startY = from.y
            curve.controlX1 = from.x + handleOffset
            curve.controlY1 = from.y
            curve.controlX2 = to.x - handleOffset
            curve.controlY2 = to.y
            curve.endX = to.x
            curve.endY = to.y
            curve.strokeWidth = width
            curve.stroke = color
            curve.fill = Color.TRANSPARENT
            return curve
        }
    }

    class Slot constructor(val offsetX: Double, val offsetY: Double) : Pane() {

        init {
            styleClass.add("slot")
            width = 14.0
            height = 14.0
            relocate(offsetX, offsetY)
        }
    }

    data class Link(val from: Slot, val to: Slot)

    class Node : Pane() {
        val slots = ArrayList<Slot>()
        val title = Text("Reply Choice")

        init {
            styleClass.addAll("node", "choice")
            setPrefSize(200.0, 100.0)

            slots.add(Slot(6.0, 8.0))
            slots.add(Slot(180.0, 40.0))
            slots.add(Slot(180.0, 65.0))

            title.styleClass.add("title")

            slots.forEach { children.add(it) }
            children.add(title)
        }

        override fun layoutChildren() {
            title.relocate(30.0, 5.0)
            title.resize(100.0, 20.0)
        }

        fun slotLocation(slot: Slot): Point2D {
            return Point2D(layoutX + slot.offsetX + 7, layoutY + slot.offsetY + 7)
        }
    }

    class NodeEditor : Pane() {
        val nodes = ArrayList<Node>()
        val grid = Grid()

        init {
            styleClass.add("editor")

            children.add(grid)

            createNode(10.0, 10.0)
            createNode(300.0, 50.0)
            createNode(320.0, 210.0)
            createNode(30.0, 250.0)

            val from = nodes[0].slotLocation(nodes[0].slots[1])
            val to = nodes[2].slotLocation(nodes[2].slots[0])
            children.add(grid.curve(from, to, 5.0, Color.web("#333537")))
            children.add(grid.curve(from, to, 1.0, Color.web("#90969C")))

//            children.filter { it -> it is CubicCurve }.forEach{it -> it.z}

            grid.links.add(Link(nodes[0].slots[1], nodes[1].slots[0]))
            grid.links.add(Link(nodes[0].slots[2], nodes[3].slots[0]))
            grid.links.add(Link(nodes[2].slots[0], nodes[3].slots[1]))
            grid.links.add(Link(nodes[2].slots[0], nodes[3].slots[2]))
        }

        fun createNode(x: Double, y: Double) {
            val node = Node()
            node.relocate(x, y)
            node.resize(node.prefWidth, node.prefHeight)
            nodes.add(node)
            children.add(node)
        }

        override fun layoutChildren() {
            grid.relocate(0.0, 0.0)
            grid.width = width
            grid.height = height
            grid.repaint()
        }
    }
}