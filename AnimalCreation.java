import java.awt.*;
import java.awt.image.BufferedImage;

public class AnimalCreation extends Creation {
    public AnimalCreation(BufferedImage image, Point position) {
        super(image, position);
    }

    @Override
    public void update(int dx, int dy) {
        flipped = !flipped;
    }

    @Override
    public void setPosition(Point position) {
        super.setPosition(position);
    }
}