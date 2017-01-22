package com.manabreak.node_editor.ui

import com.manabreak.node_editor.model.Link
import com.manabreak.node_editor.model.Slot
import com.manabreak.node_editor.model.Slot.Direction.INPUT
import com.manabreak.node_editor.model.Slot.Direction.OUTPUT
import java.util.*

/**
 * Maintains the links between the nodes. Can
 */
class LinkManager {

    val links = ArrayList<Link>()

    fun link(from: Slot, to: Slot) {
        // TODO: check nodes are different
        // TODO: check directions are correct
        // TODO: check link doesn't already exist

        if (canLink(from, to)) {
            links.add(newLink(from, to))
        } else {
            throw RuntimeException("Cannot link: $from to $to")
        }
    }

    fun canLink(from: Slot, to: Slot): Boolean {
        return from.direction != to.direction
                && from.node != to.node
                && (!isLinked(from) || from.allowsMultipleLinks)
                && (!isLinked(to) || to.allowsMultipleLinks)
    }

    private fun newLink(from: Slot, to: Slot): Link {
        if (from.direction == INPUT && to.direction == OUTPUT) {
            return Link(from, to)
        }

        if (from.direction == OUTPUT && to.direction == INPUT) {
            return Link(to, from)
        }

        throw RuntimeException("Cannot link: $from to $to")
    }

    fun unlink(link: Link) {
        links.remove(link)
    }

    fun isLinked(slot: Slot): Boolean {
        return links.any { it.from == slot || it.to == slot }
    }

    fun findLink(slot: Slot): Link? {
        return links.firstOrNull { it.from == slot || it.to == slot }
    }

    fun findLinks(slot: Slot): List<Link> {
        return links.filter { it.from == slot }
    }
}

