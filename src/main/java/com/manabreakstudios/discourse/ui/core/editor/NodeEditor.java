package com.manabreakstudios.discourse.ui.core.editor;

import com.manabreakstudios.discourse.ui.core.utils.DragHelper;
import com.manabreakstudios.discourse.ui.core.utils.SwingUtils;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static com.manabreakstudios.discourse.ui.Theme.theme;
import static com.manabreakstudios.discourse.ui.core.editor.Renderer.*;
import static com.manabreakstudios.discourse.ui.core.utils.SwingUtils.*;
import static java.awt.AWTEvent.MOUSE_EVENT_MASK;
import static java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK;
import static java.awt.Event.*;
import static java.awt.event.InputEvent.CTRL_MASK;

public class NodeEditor extends JPanel {

    private final List<NodeUI> nodes = new ArrayList<>();

    private final List<Connection> connections = new ArrayList<>();

    private final DragHelper dragHelper = new DragHelper();

    private final ConnectionHelper connectionHelper = new ConnectionHelper();

    private final MarqueeSelection marqueeSelection = new MarqueeSelection();

    private final Grid grid = new Grid();

    private final SelectionModel selection = new SelectionModel(this);

    private final Rectangle tempRect = new Rectangle();

    public NodeEditor() {
        setLayout(new AbsoluteLayoutManager());
        Toolkit.getDefaultToolkit().addAWTEventListener(new MouseDragListener(), MOUSE_MOTION_EVENT_MASK | MOUSE_EVENT_MASK);
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

        for (NodeUI nodeUI : selection.getSelectedNodes()) {
            drawNodeHighlight(g2d, expand(nodeUI.getHitbox(tempRect), 2));
        }
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        if (marqueeSelection.isSelecting) {
            drawSelection(g, marqueeSelection.rectangle);
        }
    }

    private Point getSlotLocation(SlotBinding slotBinding) {
        Slot slot = slotBinding.getSlot();
        NodeUI node = slotBinding.getNode();
        int positionX = slot.getDirection() == Slot.Direction.INPUT ? 0 : (node.getWidth() - theme().getSlotSize());
        return new Point(node.getX() + positionX + theme().getSlotSize() / 2, node.getY() + slot.getPosition() + theme().getSlotSize() / 2);
    }

    private void onMouseDown(MouseEvent event) {
        Point localPoint = screenToLocal(event.getLocationOnScreen(), this);
        Component componentUnderCursor = SwingUtils.getComponentAt(this, localPoint);

        if (componentUnderCursor instanceof SlotComponent) {
            connectionHelper.beginConnection(((SlotComponent) componentUnderCursor).getSlotBinding());
        } else {
            NodeUI node = getNodeUnderCursor(localPoint, componentUnderCursor);

            if (node != null) {
                if ((event.getModifiers() & CTRL_MASK) != 0) {
                    selection.add(node);
                } else {
                    if (!node.isSelected()) {
                        selection.setSelection(node);
                    }
                }

                if (isInputTransparent(componentUnderCursor)) {
                    beginDrag(localPoint);
                }
            } else {
                selection.clear();
                marqueeSelection.beginSelection(localPoint);
            }
        }
    }

    private NodeUI getNodeUnderCursor(Point cursor, Component componentUnderCursor) {
        NodeUI node = getNodeUIAncestor(componentUnderCursor);
        if (componentUnderCursor instanceof NodeUI && !((NodeUI) componentUnderCursor).getHitbox().contains(cursor)) {
            node = null;
        }
        return node;
    }

    private void mouseUp(MouseEvent event) {
        Point localPoint = screenToLocal(event.getLocationOnScreen(), this);
        Component componentUnderCursor = SwingUtils.getComponentAt(this, localPoint);

        if (componentUnderCursor instanceof SlotComponent) {
            connectionHelper.endConnection(((SlotComponent) componentUnderCursor).getSlotBinding());
        } else {
            connectionHelper.stop();
        }

        if (dragHelper.isDragging()) {
            endDrag();
        } else {
            if (marqueeSelection.isSelecting) {
                marqueeSelection.finishSelection(localPoint);
            }
        }

        refresh();
    }

    private void mouseDrag(MouseEvent event) {
        Point localPoint = screenToLocal(event.getLocationOnScreen(), this);

        updateDrag(localPoint);
        connectionHelper.update(localPoint);
        marqueeSelection.update(localPoint);
        if (marqueeSelection.isSelecting) {
            selection.selectFromMarquee(marqueeSelection.rectangle, nodes);
        }

        refresh();
    }

    public void refresh() {
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
        for (NodeUI selectedNode : selection.getSelectedNodes()) {
            selectedNode.getLocation(temp);
            temp.translate(dragHelper.getDeltaMove().x, dragHelper.getDeltaMove().y);
            selectedNode.setLocation(temp);
        }
    }

    private class MarqueeSelection {

        @Getter
        private boolean isSelecting = false;

        private final Point from = new Point();

        private final Point to = new Point();

        private final Rectangle rectangle = new Rectangle();

        public void beginSelection(Point from) {
            this.from.setLocation(from);
            isSelecting = true;
        }

        public void update(Point update) {
            if (!isSelecting) {
                return;
            }

            to.setLocation(update);
            int x = Math.min(from.x, to.x);
            int y = Math.min(from.y, to.y);
            int width = Math.abs(to.x - from.x);
            int height = Math.abs(to.y - from.y);
            rectangle.setBounds(x, y, width, height);
        }

        public void finishSelection(Point end) {
            isSelecting = false;
            rectangle.setRect(0, 0, 0, 0);
            from.setLocation(0, 0);
            to.setLocation(0, 0);
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

    private class MouseDragListener implements AWTEventListener {

        @Override
        public void eventDispatched(AWTEvent awtEvent) {
            MouseEvent event = (MouseEvent) awtEvent;

            switch (event.getID()) {
                case MOUSE_DOWN:
                    onMouseDown(event);
                    break;
                case MOUSE_UP:
                    mouseUp(event);
                    break;
                case MOUSE_DRAG:
                    mouseDrag(event);
                    break;
            }
        }
    }

    private static class AbsoluteLayoutManager implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(1000, 600);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(1000, 600);
        }

        @Override
        public void layoutContainer(Container parent) {
        }
    }
}
