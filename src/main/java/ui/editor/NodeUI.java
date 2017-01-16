package ui.editor;

import javax.swing.*;

/**
 * A NodeUI is a UI component that holds a node.
 */
public class NodeUI extends JPanel {

	public NodeUI() {
		setBorder(BorderFactory.createLineBorder(UIManager.getColor("Label.foreground")));
	}
}
