package ui.editor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NodeEditor extends JPanel {

	private final List<NodeUI> nodeUIList = new ArrayList<>();

	public NodeEditor() {
		setLayout(new NodeLayoutManager());
	}

	public void addNode(NodeUI nodeUI) {
		add(nodeUI);
		nodeUI.setBounds(10, 10, 200, 100);
	}

	private static class NodeLayoutManager implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(1000, 1000);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(1000, 1000);
		}

		@Override
		public void layoutContainer(Container parent) {
		}
	}
}
