package ui;

import java.awt.*;

import static ui.Theme.theme;

public class GlyphIconPainter {

	public static void drawIcon(Graphics g, FontAwesomeGlyph glyph, int x, int y, int size) {
		Font font = theme().getFontAwesome().deriveFont(size);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setFont(font);
		g2d.drawString(glyph.getRepresentation(), x, y);
	}
}
