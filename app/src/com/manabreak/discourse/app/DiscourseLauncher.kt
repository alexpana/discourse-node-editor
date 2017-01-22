import com.bulenkov.darcula.DarculaLaf
import com.manabreak.discourse.nodes.ReplyChoiceNode
import com.manabreak.node_editor.ui.NodeEditor
import com.manabreak.node_editor.ui.NodeUI
import java.awt.BorderLayout
import java.awt.FontFormatException
import java.io.IOException
import javax.swing.*

object DiscourseLauncher {

    @Throws(UnsupportedLookAndFeelException::class, IOException::class, FontFormatException::class)
    @JvmStatic fun main(args: Array<String>) {
        initLookAndFeel()

        val frame = JFrame()
        frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        frame.setSize(800, 600)
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
        frame.title = "Discourse 0.1"

        frame.contentPane.layout = BorderLayout()

        val nodeEditor = NodeEditor()

        val nodeA = NodeUI(ReplyChoiceNode())
        nodeEditor.addNode(nodeA, 10, 10)

        val nodeB = NodeUI(ReplyChoiceNode())
        nodeEditor.addNode(nodeB, 300, 20)

        val nodeC = NodeUI(ReplyChoiceNode())
        nodeEditor.addNode(nodeC, 300, 200)

        frame.contentPane.add(nodeEditor)
        frame.pack()
        frame.setLocationRelativeTo(null)

        SwingUtilities.invokeLater {
            nodeEditor.linkManager.link(nodeA.content.firstOutput, nodeB.content.input)
            nodeEditor.linkManager.link(nodeA.content.secondOutput, nodeC.content.input)
            nodeEditor.refresh()
        }
    }

    @Throws(UnsupportedLookAndFeelException::class, FontFormatException::class, IOException::class)
    private fun initLookAndFeel() {
        DarculaLaf.initInputMapDefaults(UIManager.getDefaults())
        UIManager.setLookAndFeel(DarculaLaf())
    }
}
