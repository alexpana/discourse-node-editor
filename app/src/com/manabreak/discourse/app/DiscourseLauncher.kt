import com.manabreak.discourse.nodes.ReplyChoiceNode
import com.manabreak.discourse.nodes.SequenceNode
import com.manabreak.node_editor.ui.NodeEditor
import com.manabreak.node_editor.ui.NodeUI
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.text.Font
import javafx.stage.Stage
import java.awt.FontFormatException
import java.io.IOException
import javax.swing.SwingUtilities
import javax.swing.UnsupportedLookAndFeelException

object DiscourseLauncher {

    @Throws(UnsupportedLookAndFeelException::class, IOException::class, FontFormatException::class)
    @JvmStatic fun main(args: Array<String>) {
        Application.launch(DiscourseApplication::class.java, *args)
    }

    class DiscourseApplication : Application() {

        override fun start(stage: Stage?) {

            loadFont("fontawesome-webfont")
            loadFont("OpenSans-ExtraBold")

            val nodeEditor = NodeEditor()

            val nodeA = NodeUI(ReplyChoiceNode())
            nodeEditor.addNode(nodeA, 10.0, 10.0)

            val nodeB = NodeUI(ReplyChoiceNode())
            nodeEditor.addNode(nodeB, 300.0, 20.0)

            val nodeC = NodeUI(ReplyChoiceNode())
            nodeEditor.addNode(nodeC, 300.0, 200.0)

            val nodeS = NodeUI(SequenceNode())
            nodeEditor.addNode(nodeS, 500.0, 200.0)

            SwingUtilities.invokeLater {
                nodeEditor.linkManager.link(nodeA.content.firstOutput, nodeB.content.input)
                nodeEditor.linkManager.link(nodeA.content.secondOutput, nodeC.content.input)
                nodeEditor.refresh()
            }

            stage!!.scene = Scene(nodeEditor)
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
}
