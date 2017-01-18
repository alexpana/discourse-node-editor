package com.manabreakstudios.discourse.ui.editor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON2;
import static com.manabreakstudios.discourse.ui.Theme.theme;

public class NodeEditor extends JPanel {

	private final List<NodeUI> nodeUIList = new ArrayList<>();

	private final List<Connection> connections = new ArrayList<>();

	public NodeEditor() {
		setLayout(new NodeLayoutManager());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == BUTTON1) {
					for (NodeUI nodeUI : nodeUIList) {
						if (nodeUI.getBounds().contains(e.getX(), e.getY())) {
							nodeUI.setSelected(true);
							invalidate();
						}
					}
				}

				if (e.getButton() != BUTTON2) {
					return;
				}
				JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.add("Reply Choice");
				popupMenu.add("Decision");
				popupMenu.add("Reply");
				popupMenu.add("Script");
				popupMenu.show(NodeEditor.this, e.getX(), e.getY());
			}
		});
	}

	public void addNode(NodeUI nodeUI, int x, int y) {
		add(nodeUI);
		nodeUIList.add(nodeUI);
		nodeUI.setBounds(x, y, (int) nodeUI.getPreferredSize().getWidth(), (int) nodeUI.getPreferredSize().getHeight());
	}

	public void connect(Slot from, Slot to) {
		connections.add(new Connection(from, to));
	}

	@Override
	protected void paintChildren(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (Connection connection : connections) {
			Point positionFrom = getSlotLocation(connection.getFrom());
			Point positionTo = getSlotLocation(connection.getTo());

			int horizontalDistance = Math.abs(positionFrom.x - positionTo.x);
			int handleOffset = Math.max(horizontalDistance / 2, 20);

			CubicCurve2D cubicCurve2D = new CubicCurve2D.Float(
					positionFrom.x, positionFrom.y,
					positionFrom.x + handleOffset, positionFrom.y,
					positionTo.x - handleOffset, positionTo.y,
					positionTo.x, positionTo.y);

			g2d.setColor(theme().getNodeBorderColor());
			g2d.setStroke(new BasicStroke(5f));
			g2d.draw(cubicCurve2D);

			g2d.setColor(theme().getLinkColor());
			g2d.setStroke(new BasicStroke(1.5f));
			g2d.draw(cubicCurve2D);
		}

		super.paintChildren(g);
	}

	private Point getSlotLocation(Slot slot) {
		NodeUI node = slot.getNode();
		int positionX = slot.getDirection() == Slot.Direction.INPUT ? 0 : (node.getWidth() - theme().getSlotSize());
		return new Point(node.getX() + positionX + theme().getSlotSize() / 2, node.getY() + slot.getPosition() + theme().getSlotSize() / 2);
	}

	@RequiredArgsConstructor
	private static class Connection {

		@Getter
		private final Slot from;
		@Getter
		private final Slot to;
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
