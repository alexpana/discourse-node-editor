package com.manabreak.node_editor

import java.awt.Color
import java.awt.Font
import java.awt.FontFormatException
import java.io.IOException

class Theme private constructor() {

    val nodeBorderColor = Color(0x333537)

    val nodeSpecularColor = Color(0x4C4F52)

    val nodeBackgroundColor = Color(0x424548)

    val slotBorder = Color(0x333637)

    val slotDefaultColor = Color(0x64686C)

    val slotAcceptColor = Color(0x557EA4)

    val slotDeclineColor = Color(0xA4555A)

    val linkColor = Color(0x90969C)

    val gridColor = Color(0x424548)

    val gridMajorColor = Color(0x464A4D)

    val marqueeSelectColor = Color(0x90969C)

    val slotSize = 11

    val slotBorderWidth = 2

    val boldFont: Font

    val defaultFont: Font

    val fontAwesome: Font

    val selectionColor = Color(0x90FFB384.toInt(), true)
    //    private final Color selectionColor = new Color(0xAA84C4FF, true);

    val choiceNodeColor = Color(0x738495)

    val checkNodeColor = Color(0xA67557)

    val propertyUpdateNodeColor = Color(0x4EB3B3)

    val scriptNodeColor = Color(0x55A5F0)

    init {
        boldFont = loadFont("fonts/OpenSans-ExtraBold.ttf")
        defaultFont = loadFont("fonts/OpenSans-Regular.ttf")
        fontAwesome = loadFont("fonts/fontawesome-webfont.ttf")
    }

    companion object {
        val theme: Theme = Theme()
    }

    @Throws(FontFormatException::class, IOException::class)
    private fun loadFont(filePath: String): Font {
        val inputStream = Theme::class.java.classLoader.getResourceAsStream(filePath)
        return Font.createFont(Font.TRUETYPE_FONT, inputStream)
    }
}
