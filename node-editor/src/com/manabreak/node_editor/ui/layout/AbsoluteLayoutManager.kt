package com.manabreak.node_editor.ui.layout

import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager

class AbsoluteLayoutManager constructor(val preferredSize: Dimension) : LayoutManager {

    override fun addLayoutComponent(name: String, comp: Component) {
    }

    override fun removeLayoutComponent(comp: Component) {
    }

    override fun preferredLayoutSize(parent: Container): Dimension {
        return preferredSize
    }

    override fun minimumLayoutSize(parent: Container): Dimension {
        return preferredSize
    }

    override fun layoutContainer(parent: Container) {
    }
}