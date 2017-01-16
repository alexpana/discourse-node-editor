import com.bulenkov.darcula.DarculaLaf;
import ui.editor.NodeEditor;
import ui.editor.NodeUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Launcher {

	public static void main(String[] args) throws UnsupportedLookAndFeelException {

		DarculaLaf.initInputMapDefaults(UIManager.getDefaults());
		UIManager.setLookAndFeel(new DarculaLaf());
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
}
