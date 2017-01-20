package com.manabreakstudios.discourse.ui.core.editor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SelectionModel {

    @Getter
    private final List<NodeUI> selectedNodes = new ArrayList<>();

    @Getter
    private final List<NodeUI> temporarySelectedNodes = new ArrayList<>();

    private final NodeEditor editor;

    public void clear() {
        clear(true);
    }

    private void clear(boolean refresh) {
        for (NodeUI selectedNode : selectedNodes) {
            selectedNode.setSelected(false);
        }
        selectedNodes.clear();

        if (refresh) {
            editor.refresh();
        }
    }

    public void add(NodeUI node) {
        add(node, true);
    }

    private void add(NodeUI node, boolean refresh) {
        if (!node.isSelected()) {
            node.setSelected(true);
            selectedNodes.add(node);
            if (refresh) {
                editor.refresh();
            }
        }
    }

    public void setSelection(NodeUI... nodes) {
        clear(false);

        for (NodeUI node : nodes) {
            add(node, false);
        }

        editor.refresh();
    }

    public void temporarySelectFromMarquee(Rectangle selectionRectangle, List<NodeUI> nodes) {
        for (NodeUI temporarySelectedNode : temporarySelectedNodes) {
            temporarySelectedNode.setTemporarySelected(false);
        }
        temporarySelectedNodes.clear();

        for (NodeUI node : nodes) {
            if (selectionRectangle.contains(node.getBounds())) {
                temporarySelectedNodes.add(node);
                node.setTemporarySelected(true);
            }
        }
    }

    public void commitTemporarySelection() {
        clear(false);
        for (NodeUI temporarySelectedNode : temporarySelectedNodes) {
            add(temporarySelectedNode, false);
            temporarySelectedNode.setTemporarySelected(false);
        }
        temporarySelectedNodes.clear();
        editor.refresh();
    }
}
