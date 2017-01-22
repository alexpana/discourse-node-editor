package com.manabreak.node_editor.ui.icons

import com.manabreak.node_editor.ui.Theme.Companion.theme
import com.manabreak.node_editor.ui.icons.FontAwesomeGlyph.LIST
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.util.*
import javax.swing.Icon
import javax.swing.ImageIcon

@Suppress("unused")
object IconRepository {

    private val NODE_ICON_FONT_SIZE = 28.0f

    private val NODE_ICON_SIZE = 40

    private val cache = HashMap<String, BufferedImage>()

    private var replyChoiceNodeIcon: Icon? = null

    fun getReplyChoiceNodeIcon(): Icon? {
        if (replyChoiceNodeIcon == null) {
            replyChoiceNodeIcon = createReplyChoiceNodeIcon()
        }
        return replyChoiceNodeIcon
    }

    fun cacheIcon(name: String, icon: BufferedImage) {
        cache.put(name, icon)
    }

    fun isCached(name: String): Boolean {
        return cache.containsKey(name)
    }

    fun getIcon(name: String): BufferedImage {
        return cache[name]!!
    }

    fun newFontAwesomeIcon(glyph: FontAwesomeGlyph, iconSize: Int, glyphSize: Int, color: Color, offsetX: Int, offsetY: Int): BufferedImage {
        val image = BufferedImage(iconSize, iconSize, TYPE_INT_ARGB)
        val g2d = image.graphics as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val font = theme.fontAwesome.deriveFont(glyphSize)
        g2d.font = font
        g2d.color = color
        g2d.drawString(glyph.representation, offsetX, glyphSize + offsetY)
        return image
    }

    private fun createReplyChoiceNodeIcon(): Icon {
        val image = BufferedImage(NODE_ICON_SIZE, NODE_ICON_SIZE, TYPE_INT_ARGB)
        val g2d = image.graphics as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val font = theme.fontAwesome.deriveFont(NODE_ICON_FONT_SIZE)
        g2d.font = font
        g2d.color = theme.choiceNodeColor
        g2d.drawString(LIST.representation, 6f, NODE_ICON_SIZE - 9f)
        g2d.dispose()
        return ImageIcon(image)
    }
}
