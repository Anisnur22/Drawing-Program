import java.awt.*;
import java.awt.image.BufferedImage;

public class CustomCreation extends Creation {
    public CustomCreation(BufferedImage image, Point position) {
        super(image, position);
    }

    @Override
    public void update(int dx, int dy) {
        position.x += dx;
        position.y += dy;
    }
}