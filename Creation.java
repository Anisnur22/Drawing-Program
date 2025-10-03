import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Creation {
    protected BufferedImage image;
    protected Point position;
    protected double scale = 1.0;
    protected boolean flipped = false;

    public Creation(BufferedImage image, Point position) {
        this.image = image;
        this.position = position;
    }

    public abstract void update(int dx, int dy);

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);

        // Calculate the offset to keep the center of the image at the same position
        int xOffset = (scaledWidth - originalWidth) / 2;
        int yOffset = (scaledHeight - originalHeight) / 2;

        if (flipped) {
            g2d.translate(position.x + scaledWidth - xOffset, position.y - yOffset);
            g2d.scale(-scale, scale);
        } else {
            g2d.translate(position.x - xOffset, position.y - yOffset);
            g2d.scale(scale, scale);
        }

        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
    }

    public void rotate() {
        BufferedImage rotated = new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = rotated.createGraphics();
        g.translate((rotated.getWidth() - image.getWidth()) / 2, (rotated.getHeight() - image.getHeight()) / 2);
        g.rotate(Math.PI / 2, image.getWidth() / 2, image.getHeight() / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        image = rotated;
    }

    public boolean contains(Point p) {
        int scaledWidth = (int) (image.getWidth() * scale);
        int scaledHeight = (int) (image.getHeight() * scale);
        int xOffset = (scaledWidth - image.getWidth()) / 2;
        int yOffset = (scaledHeight - image.getHeight()) / 2;
        return p.x >= position.x - xOffset && p.x <= position.x - xOffset + scaledWidth &&
                p.y >= position.y - yOffset && p.y <= position.y - yOffset + scaledHeight;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}