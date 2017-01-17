package ui.editor;

import lombok.Getter;
import lombok.Setter;
import ui.FontAwesomeGlyph;
import ui.GlyphIconPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static java.awt.AWTEvent.MOUSE_EVENT_MASK;
import static java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK;
import static ui.Theme.theme;

/**
 * A NodeUI is a UI component that holds a node.
 */
public class NodeUI extends JPanel {

	@Getter
	protected final List<Slot> slots = new ArrayList<>();

	@Getter @Setter
	private boolean isSelected = false;

	public NodeUI() {
		add(new Header(), BorderLayout.NORTH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		int slotSize = theme().getSlotSize();

		int inset = slotSize / 2;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (isSelected) {
			Color focusHalo = theme().getSelectionColor();
			Color transparentFocusHalo = new Color((focusHalo.getRGB() << 8) + 0x50, true);
			g2d.setColor(transparentFocusHalo);
			g2d.fillRoundRect(inset - 2, inset - 2, getWidth() - 2 * inset + 4, getHeight() - 2 * inset + 4, 8, 8);
			g2d.setColor(theme().getSelectionColor());
			g2d.fillRoundRect(inset - 1, inset - 1, getWidth() - 2 * inset + 2, getHeight() - 2 * inset + 2, 6, 6);
		}

		g2d.setColor(theme().getNodeBorderColor());
		g2d.fillRoundRect(inset, inset, getWidth() - 2 * inset, getHeight() - 2 * inset, 6, 6);

		g2d.setColor(theme().getNodeSpecularColor());
		g2d.fillRoundRect(inset + 2, inset + 2, getWidth() - 2 * inset - 4, getHeight() - 2 * inset - 4, 4, 4);

		g2d.setColor(theme().getNodeBackgroundColor());
		g2d.fillRoundRect(inset + 2, inset + 3, getWidth() - 2 * inset - 4, getHeight() - 2 * inset - 5, 4, 4);

		Font font = (Font) UIManager.getDefaults().get("Font.OpenSans-ExtraBold");

		g2d.setColor(new Color(0x738495));
		g2d.setFont(font.deriveFont(12.0f));
		g2d.drawString("Reply Choice", inset + 10, inset + 18);

		GlyphIconPainter.drawIcon(g2d, FontAwesomeGlyph.LIST, 30, 30, 50);

		for (Slot slot : slots) {
			paintSlot(g2d, slot);
		}
	}

	private void paintSlot(Graphics2D g2d, Slot slot) {
		int slotSize = theme().getSlotSize();
		int slotBorderWidth = theme().getSlotBorderWidth();

		int positionX = slot.getDirection() == Slot.Direction.INPUT ? 0 : (getWidth() - slotSize);
		g2d.setColor(theme().getNodeBorderColor());
		drawCircle(g2d, positionX, slot.getPosition(), slotSize);

		g2d.setColor(theme().getSlotColor());
		drawCircle(g2d, positionX + slotBorderWidth, slot.getPosition() + slotBorderWidth, slotSize - 2 * slotBorderWidth);
	}

	private void drawCircle(Graphics2D g2d, int x, int y, int size) {
		g2d.fillRoundRect(x, y, size, size, size, size);
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

			setLayout(new BorderLayout());
			setOpaque(false);
			setMinimumSize(new Dimension(100, 20));
			setPreferredSize(new Dimension(1000, 20));
			setMaximumSize(new Dimension(2000, 20));
		}

		private void beginDrag(Point mousePosition) {
			lastMousePosition = mousePosition;
			isDragging = true;
		}

		private void endDrag() {
			isDragging = false;
		}

		private void updateDrag(Point newMousePosition) {
			if (!isDragging) {
				return;
			}
			int deltaX = newMousePosition.x - lastMousePosition.x;
			int deltaY = newMousePosition.y - lastMousePosition.y;

			NodeUI node = NodeUI.this;
			node.setLocation(node.getLocation().x + deltaX, node.getLocation().y + deltaY);
			lastMousePosition = newMousePosition;
			getTopLevelAncestor().repaint();
		}
	}
}
