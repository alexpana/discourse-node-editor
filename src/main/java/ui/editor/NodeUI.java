package ui.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static java.awt.AWTEvent.MOUSE_EVENT_MASK;
import static java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK;

/**
 * A NodeUI is a UI component that holds a node.
 */
public class NodeUI extends JPanel {

    public static final Color BORDER_COLOR = new Color(0x333537);

    public static final Color SPECULAR_COLOR = new Color(0x4C4F52);

    public static final Color BACKGROUND_COLOR = new Color(0x424548);

    public static final Color SLOT_BORDER = new Color(0x333637);

    public static final Color SLOT_COLOR = new Color(0x5E6266);

    public static final int SLOT_SIZE = 13;

    public static final int SLOT_BORDER_WIDTH = 3;

    public NodeUI() {
//        setBorder(BorderFactory.createLineBorder(UIManager.getColor("Label.foreground")));
//        setLayout(new BorderLayout());
        add(new Header(), BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(BORDER_COLOR);
        g2d.fillRoundRect(0, 0, getWidth() - SLOT_SIZE / 2, getHeight(), 4, 4);

        g2d.setColor(SPECULAR_COLOR);
        g2d.fillRoundRect(2, 2, getWidth() - SLOT_SIZE / 2 - 4, getHeight() - 4, 4, 4);

        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRoundRect(2, 3, getWidth() - SLOT_SIZE / 2 - 4, getHeight() - 5, 4, 4);

        Font font = (Font) UIManager.getDefaults().get("Font.OpenSans-ExtraBold");

        g2d.setColor(new Color(0x738495));
        g2d.setFont(font.deriveFont(13.0f));
        g2d.drawString("Reply Choice", 10, 18);

        g2d.setColor(SLOT_BORDER);
        g2d.fillOval(getWidth() - SLOT_SIZE, 20, SLOT_SIZE, SLOT_SIZE);

        g2d.setColor(SLOT_COLOR);
        g2d.fillOval(getWidth() - SLOT_SIZE + SLOT_BORDER_WIDTH, 20 + SLOT_BORDER_WIDTH, SLOT_SIZE - 2 * SLOT_BORDER_WIDTH, SLOT_SIZE - 2 * SLOT_BORDER_WIDTH);

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
