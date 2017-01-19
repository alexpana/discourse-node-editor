package com.manabreakstudios.discourse.ui.core.editor;

import com.manabreakstudios.discourse.ui.core.utils.DragHelper;
import com.manabreakstudios.discourse.ui.core.utils.SwingUtils;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.List;

import static com.manabreakstudios.discourse.ui.Theme.theme;
import static com.manabreakstudios.discourse.ui.core.utils.SwingUtils.*;
import static java.awt.AWTEvent.MOUSE_EVENT_MASK;
import static java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK;
import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.BUTTON2;

public class NodeEditor extends JPanel {

    private final List<NodeUI> nodes = new ArrayList<>();

    private final List<NodeUI> selectedNodes = new ArrayList<>();

    private final List<Connection> connections = new ArrayList<>();

    private final DragHelper dragHelper = new DragHelper();
    private final ConnectionHelper connectionHelper = new ConnectionHelper();

    private final Grid grid = new Grid();

    public NodeEditor() {
        setLayout(new NodeLayoutManager());

        Toolkit.getDefaultToolkit().addAWTEventListener(new MouseDragListener(), MOUSE_MOTION_EVENT_MASK | MOUSE_EVENT_MASK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == BUTTON1) {
                    for (NodeUI nodeUI : nodes) {
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
        nodes.add(nodeUI);
        nodeUI.setBounds(x, y, (int) nodeUI.getPreferredSize().getWidth(), (int) nodeUI.getPreferredSize().getHeight());
    }

    public void connect(SlotBinding from, SlotBinding to) {
        connect(from.getNode(), from.getSlot(), to.getNode(), to.getSlot());
    }

    public void connect(NodeUI nodeFrom, Slot slotFrom, NodeUI nodeTo, Slot slotTo) {
        connections.add(new Connection(new SlotBinding(nodeFrom, slotFrom), new SlotBinding(nodeTo, slotTo)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid.paint(g, 0, 0, getWidth(), getHeight());
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Connection connection : connections) {
            Point positionFrom = getSlotLocation(connection.getFrom());
            Point positionTo = getSlotLocation(connection.getTo());
            drawLink(g2d, positionFrom, positionTo);
        }

        if (connectionHelper.isConnecting) {
            drawLink(g2d, connectionHelper.fromLocation, connectionHelper.toLocation);
        }

        super.paintChildren(g);
    }

    private void drawLink(Graphics2D g2d, Point positionFrom, Point positionTo) {
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

    private Point getSlotLocation(SlotBinding slotBinding) {
        Slot slot = slotBinding.getSlot();
        NodeUI node = slotBinding.getNode();
        int positionX = slot.getDirection() == Slot.Direction.INPUT ? 0 : (node.getWidth() - theme().getSlotSize());
        return new Point(node.getX() + positionX + theme().getSlotSize() / 2, node.getY() + slot.getPosition() + theme().getSlotSize() / 2);
    }


    private class MouseDragListener implements AWTEventListener {
        @Override
        public void eventDispatched(AWTEvent event) {
            MouseEvent mouseEvent = (MouseEvent) event;
            NodeEditor editor = NodeEditor.this;
            Point mousePoint = mouseEvent.getPoint();
            Point localPoint = screenToLocal(mouseEvent.getLocationOnScreen(), editor);
            Component componentUnderCursor = SwingUtils.getComponentAt(editor, localPoint);

            if (mouseEvent.getID() == Event.MOUSE_DOWN) {
                if (componentUnderCursor instanceof SlotComponent) {
                    connectionHelper.beginConnection(((SlotComponent) componentUnderCursor).getSlotBinding());
                } else if (isInputTransparent(componentUnderCursor)) {
                    NodeUI node = getNodeUIAncestor(componentUnderCursor);

                    if (node != null) {
                        if ((((MouseEvent) event).getModifiers() & CTRL_MASK) != 0) {
                            addToSelection(node);
                        } else {
                            if (!node.isSelected()) {
                                setSelection(node);
                            }
                        }
                        beginDrag(mousePoint);
                    } else {
                        clearSelection();
                    }
                }
            }

            if (mouseEvent.getID() == Event.MOUSE_MOVE) {
                System.out.println("component = " + SwingUtilities.getDeepestComponentAt(editor, localPoint.x, localPoint.y));
            }

            if (mouseEvent.getID() == Event.MOUSE_UP) {
                if (componentUnderCursor instanceof SlotComponent) {
                    connectionHelper.endConnection(((SlotComponent) componentUnderCursor).getSlotBinding());
                } else {
                    connectionHelper.stop();
                }

                if (dragHelper.isDragging()) {
                    endDrag();
                } else {
                    clearSelection();
                }
            }

            if (event.getID() == Event.MOUSE_DRAG) {
                updateDrag(mousePoint);
                connectionHelper.update(localPoint);
                refresh();
            }
        }
    }

    private void clearSelection() {
        clearSelection(true);
    }


    private void clearSelection(boolean refresh) {
        for (NodeUI selectedNode : selectedNodes) {
            selectedNode.setSelected(false);
        }
        selectedNodes.clear();

        if (refresh) {
            refresh();
        }
    }

    private void addToSelection(NodeUI node) {
        addToSelection(node, true);
    }

    private void addToSelection(NodeUI node, boolean refresh) {
        if (!node.isSelected()) {
            node.setSelected(true);
            selectedNodes.add(node);
            if (refresh) {
                refresh();
            }
        }
    }

    private void setSelection(NodeUI... nodes) {
        clearSelection(false);

        for (NodeUI node : nodes) {
            addToSelection(node, false);
        }

        refresh();
    }


    private void refresh() {
        paintImmediately(getBounds());
    }

    private void beginDrag(Point mousePosition) {
        dragHelper.beginDrag(mousePosition);
    }

    private void endDrag() {
        dragHelper.endDrag();
    }

    private void updateDrag(Point newMousePosition) {
        if (!dragHelper.isDragging()) {
            return;
        }
        dragHelper.updateDrag(newMousePosition);

        Point temp = new Point();
        for (NodeUI selectedNode : selectedNodes) {
            selectedNode.getLocation(temp);
            temp.translate(dragHelper.getDeltaMove().x, dragHelper.getDeltaMove().y);
            selectedNode.setLocation(temp);
        }
    }

    private class ConnectionHelper {
        @Getter
        private boolean isConnecting = false;

        private SlotBinding from;

        @Getter
        private Point fromLocation;

        @Getter
        private Point toLocation;

        public void beginConnection(SlotBinding from) {
            this.from = from;
            this.fromLocation = getSlotLocation(from);
            this.toLocation = this.fromLocation;
            this.isConnecting = true;
        }

        public void endConnection(SlotBinding to) {
            connect(this.from, to);
            stop();
        }

        public void update(Point mouseLocation) {
            this.toLocation = mouseLocation;
        }

        public void cancel() {
            stop();
        }

        public void stop() {
            from = null;
            isConnecting = false;
        }
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
