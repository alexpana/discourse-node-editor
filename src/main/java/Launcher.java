import com.bulenkov.darcula.DarculaLaf;
import com.manabreakstudios.discourse.ui.Theme;
import com.manabreakstudios.discourse.ui.editor.NodeEditor;
import com.manabreakstudios.discourse.ui.editor.nodes.ReplyChoiceNode;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Launcher {

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

        ReplyChoiceNode nodeA = new ReplyChoiceNode();
        nodeEditor.addNode(nodeA, 10, 10);

        ReplyChoiceNode nodeB = new ReplyChoiceNode();
        nodeEditor.addNode(nodeB, 300, 20);

        ReplyChoiceNode nodeC = new ReplyChoiceNode();
        nodeEditor.addNode(nodeC, 300, 200);

        nodeEditor.connect(nodeA.getSlots().get(1), nodeB.getSlots().get(0));
        nodeEditor.connect(nodeA.getSlots().get(2), nodeC.getSlots().get(0));

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
        InputStream inputStream = Launcher.class.getResourceAsStream(filePath);
        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        UIManager.getDefaults().put(resourceName, font);
    }
}
