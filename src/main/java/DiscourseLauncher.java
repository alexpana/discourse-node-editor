import com.bulenkov.darcula.DarculaLaf;
import com.manabreakstudios.discourse.ui.Theme;
import com.manabreakstudios.discourse.ui.app.nodes.ReplyChoiceNode;
import com.manabreakstudios.discourse.ui.core.nodeeditor.NodeEditor;
import com.manabreakstudios.discourse.ui.core.nodeeditor.NodeUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class DiscourseLauncher {

    public static void main(String[] args) throws UnsupportedLookAndFeelException, IOException, FontFormatException {
        initLookAndFeel();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("Discourse 0.1");

        frame.getContentPane().setLayout(new BorderLayout());

        NodeEditor nodeEditor = new NodeEditor();

        NodeUI nodeA = new NodeUI(new ReplyChoiceNode());
        nodeEditor.addNode(nodeA, 10, 10);

        NodeUI nodeB = new NodeUI(new ReplyChoiceNode());
        nodeEditor.addNode(nodeB, 300, 20);

        NodeUI nodeC = new NodeUI(new ReplyChoiceNode());
        nodeEditor.addNode(nodeC, 300, 200);

        nodeEditor.connect(nodeA.getSlot(1), nodeB.getSlot(0));
        nodeEditor.connect(nodeA.getSlot(2), nodeC.getSlot(0));

        frame.getContentPane().add(nodeEditor);
        frame.invalidate();
    }

    private static void initLookAndFeel() throws UnsupportedLookAndFeelException, FontFormatException, IOException {
        DarculaLaf.initInputMapDefaults(UIManager.getDefaults());
        UIManager.setLookAndFeel(new DarculaLaf());
        loadFont("Font.OpenSans-ExtraBold", "fonts/OpenSans-ExtraBold.ttf");
        loadFont("Font.OpenSans-Regular", "fonts/OpenSans-Regular.ttf");
        loadFont("Font.FontAwesome", "fonts/fontawesome-webfont.ttf");

        Theme.initialize(UIManager.getDefaults());
    }

    private static void loadFont(String resourceName, String filePath) throws FontFormatException, IOException {
        InputStream inputStream = DiscourseLauncher.class.getResourceAsStream(filePath);
        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        UIManager.getDefaults().put(resourceName, font);
    }
}
