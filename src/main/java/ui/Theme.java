package ui;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class Theme {

    public static Theme instance;

    @Getter
    private final Color nodeBorderColor = new Color(0x333537);

    @Getter
    private final Color nodeSpecularColor = new Color(0x4C4F52);

    @Getter
    private final Color nodeBackgroundColor = new Color(0x424548);

    @Getter
    private final Color slotBorder = new Color(0x333637);

    @Getter
    private final Color slotColor = new Color(0x64686C);

    @Getter
    private final Color linkColor = new Color(0x90969C);

    @Getter
    private final int slotSize = 11;

    @Getter
    private final int slotBorderWidth = 2;

    @Getter
    private final Font boldFont;

    @Getter
    private final Font defaultFont;

    @Getter
    private final Font fontAwesome;

    @Getter
    private final Color selectionColor = new Color(0xB66840);

    private final Color choiceNodeColor = new Color(0x323232);

    private final Color checkNodeColor = new Color(0x323232);

    private final Color propertyUpdateNodeColor = new Color(0x323232);

    private final Color scriptNodeColor = new Color(0x323232);

    private Theme(UIDefaults uiDefaults) {
        boldFont = uiDefaults.getFont("Font.OpenSans-ExtraBold");
        defaultFont = uiDefaults.getFont("Font.OpenSans-Regular");
        fontAwesome = uiDefaults.getFont("Font.FontAwesome");
    }

    public static void initialize(UIDefaults uiDefaults) {
        instance = new Theme(uiDefaults);
    }

    public static Theme theme() {
        return instance;
    }
}
