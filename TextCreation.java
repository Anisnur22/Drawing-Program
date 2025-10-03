import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class TextCreation extends Creation {
    private String text;
    private Font font;

    public TextCreation(String text, Point position, Graphics2D g2d) {
        super(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), position);
        this.text = text;
        this.font = new Font("Arial", Font.PLAIN, 16);
    }

    @Override
    public void update(int dx, int dy) {
        position.x += dx;
        position.y += dy;
    }

    @Override
    public void draw(Graphics g) {
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString(text, position.x, position.y);
    }

    @Override
    public boolean contains(Point p) {
        FontMetrics fm = getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        return p.x >= position.x && p.x <= position.x + textWidth &&
                p.y >= position.y - textHeight && p.y <= position.y;
    }

    private FontMetrics getFontMetrics(Font font) {
        return new JPanel().getFontMetrics(font);
    }
}
