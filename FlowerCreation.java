import java.awt.*;
import java.awt.image.BufferedImage;

public class FlowerCreation extends Creation {
    public FlowerCreation(BufferedImage image, Point position) {
        super(image, position);
    }

    @Override
    public void update(int dx, int dy) {
        double newScale = Math.max(0.1, Math.min(2.0, scale + dx * 0.01));
        int newScaledWidth = (int) (image.getWidth() * newScale);
        int newScaledHeight = (int) (image.getHeight() * newScale);

        int xOffset = (newScaledWidth - image.getWidth()) / 2;
        int yOffset = (newScaledHeight - image.getHeight()) / 2;

        // Check if the new scaled image will be within the canvas boundaries
        if (position.x - xOffset >= 0 && position.x - xOffset + newScaledWidth <= 498 &&
                position.y - yOffset >= 0 && position.y - yOffset + newScaledHeight <= 598) {
            scale = newScale;
        }
    }

    // Override setPosition to ensure it's used correctly
    @Override
    public void setPosition(Point position) {
        super.setPosition(position);
    }
}