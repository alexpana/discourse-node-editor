package ui.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.AWTEvent.MOUSE_EVENT_MASK;
import static java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK;

/**
 * A NodeUI is a UI component that holds a node.
 */
public class NodeUI extends JPanel {

	public NodeUI() {
		setBorder(BorderFactory.createLineBorder(UIManager.getColor("Label.foreground")));
		setLayout(new BorderLayout());
		add(new Header(), BorderLayout.NORTH);
	}

	public class Header extends JPanel {

		private Point lastMousePosition;

		private boolean isDragging = false;

		public Header() {
			Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
				MouseEvent mouseEvent = (MouseEvent) event;
				Point mousePoint = mouseEvent.getLocationOnScreen();
				if (mouseEvent.getID() == Event.MOUSE_DOWN) {
					Point localPoint = new Point(mousePoint);
					SwingUtilities.convertPointFromScreen(localPoint, this);
					if (this.contains(localPoint)) {
						beginDrag(mousePoint);
					}
				}
				if (mouseEvent.getID() == Event.MOUSE_UP) {
					endDrag();
				}
				if (event.getID() == Event.MOUSE_DRAG) {
					updateDrag(mousePoint);
				}
			}, MOUSE_MOTION_EVENT_MASK | MOUSE_EVENT_MASK);

			setMinimumSize(new Dimension(100, 30));
			setPreferredSize(new Dimension(100, 30));
			setMaximumSize(new Dimension(2000, 30));

			Label title = new Label("Title");
			title.setBackground(Color.red);
			title.setAlignment(SwingConstants.CENTER);
			add(title);
		}

		private void beginDrag(Point mousePosition) {
			lastMousePosition = mousePosition;
			isDragging = true;
		}

		private void endDrag() {
			isDragging = false;
		}

		private void updateDrag(Point newMousePosition) {
			System.out.println(isDragging);
			if (!isDragging) {
				return;
			}
			int deltaX = newMousePosition.x - lastMousePosition.x;
			int deltaY = newMousePosition.y - lastMousePosition.y;

			NodeUI node = NodeUI.this;
			System.out.println(deltaX + ", " + deltaY);
			node.setLocation(node.getLocation().x + deltaX, node.getLocation().y + deltaY);
			lastMousePosition = newMousePosition;
		}
	}
}
