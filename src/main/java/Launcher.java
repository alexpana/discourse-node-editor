import com.bulenkov.darcula.DarculaLaf;
import ui.editor.NodeEditor;
import ui.editor.NodeUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        JButton close = new JButton("Close");
        close.setAction(new AbstractAction() {
            {
                putValue(Action.NAME, "Close");
            }

            public void actionPerformed(ActionEvent e) {
                System.out.println("Close clicked");
            }
        });
        frame.getContentPane().add(close, BorderLayout.SOUTH);

        NodeEditor nodeEditor = new NodeEditor();
        nodeEditor.addNode(new NodeUI());
        frame.getContentPane().add(nodeEditor);
    }

    private static void initLookAndFeel() throws UnsupportedLookAndFeelException, FontFormatException, IOException {
        DarculaLaf.initInputMapDefaults(UIManager.getDefaults());
        UIManager.setLookAndFeel(new DarculaLaf());
        loadFont("Font.OpenSans-ExtraBold", "fonts/OpenSans-ExtraBold.ttf");
        loadFont("Font.OpenSans-Regular", "fonts/OpenSans-Regular.ttf");
        loadFont("Font.FontAwesome", "fonts/FontAwesome.otf");
    }

    private static void loadFont(String resourceName, String filePath) throws FontFormatException, IOException {
        InputStream inputStream = Launcher.class.getResourceAsStream(filePath);
        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        UIManager.getDefaults().put(resourceName, font);
    }
}
