import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RightCanvas extends JPanel implements MouseListener, MouseMotionListener {
    private Point previousPnt = null;
    private Point currentPnt = new Point();
    private BufferedImage canvas;
    private Graphics2D g2d;
    private static Color penColor = Color.BLACK;
    private static int penSize = 4;
    private boolean drawingEnabled = false;
    private List<Creation> creations = new ArrayList<>();
    private Creation selectedCreation = null;
    private Random random = new Random();
    private List<TextCreation> textCreations = new ArrayList<>();

    public RightCanvas() {
        setPreferredSize(new Dimension(498, 598));
        addMouseMotionListener(this);
        addMouseListener(this);
        initCanvas();
    }

    public void setDrawingEnabled(boolean enabled) {
        drawingEnabled = enabled;
    }

    public boolean isDrawingEnabled() {
        return drawingEnabled;
    }

    public void setImage(BufferedImage img) {
        canvas = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.setColor(penColor);
        g2d.setStroke(new BasicStroke(penSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        repaint();
    }

    public BufferedImage getCanvas() {
        return canvas;
    }

    public BufferedImage getImage() {
        return getDrawing();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        previousPnt = e.getPoint();
        selectedCreation = null;
        for (Creation creation : creations) {
            if (creation.contains(e.getPoint())) {
                selectedCreation = creation;
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        previousPnt = null;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (drawingEnabled) {
            currentPnt = me.getPoint();
            drawOnCanvas(previousPnt, currentPnt);
            previousPnt = currentPnt;
            repaint();
        }
    }

    public void addText() {
        String text = JOptionPane.showInputDialog("Enter text:");
        if (text != null && !text.isEmpty()) {
            int x = random.nextInt(getWidth() - 100);
            int y = random.nextInt(getHeight() - 20);
            Point randomPoint = new Point(x, y);
            TextCreation textCreation = new TextCreation(text, randomPoint, getGraphics2D());
            textCreations.add(textCreation);
            repaint();
        }
    }

    public void rotateCanvas() {
        BufferedImage rotated = new BufferedImage(canvas.getHeight(), canvas.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = rotated.createGraphics();
        g.translate((rotated.getWidth() - canvas.getWidth()) / 2, (rotated.getHeight() - canvas.getHeight()) / 2);
        g.rotate(Math.PI / 2, canvas.getWidth() / 2, canvas.getHeight() / 2);
        g.drawRenderedImage(canvas, null);
        g.dispose();
        canvas = rotated;
        g2d = canvas.createGraphics();
        g2d.setColor(penColor);
        g2d.setStroke(new BasicStroke(penSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        setPreferredSize(new Dimension(canvas.getWidth(), canvas.getHeight()));
        revalidate();
        repaint();
    }

    public void addCreation(Creation creation) {
        creations.add(creation);
        repaint();
    }

    public void rotateSelectedImage() {
        if (selectedCreation != null) {
            selectedCreation.rotate();
            repaint();
        }
    }

    public Creation getSelectedCreation() {
        return selectedCreation;
    }

    public static void setPenColor(Color color) {
        penColor = color;
    }

    public static void setPenSize(int size, Graphics2D g2d) {
        penSize = size;
        g2d.setStroke(new BasicStroke(penSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public Graphics2D getGraphics2D() { // used to set pen size
        return g2d;
    }

    public BufferedImage getDrawing() {
        renderAllContentOnCanvas();
        return canvas;
    }

    private void initCanvas() {
        canvas = new BufferedImage(498, 598, BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.setComposite(AlphaComposite.Clear); // make entire right canvas
        // background transparent (able to overlay to left canvas)
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setColor(penColor);
        g2d.setStroke(new BasicStroke(penSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    private void drawOnCanvas(Point from, Point to) {
        if (from != null && to != null) {
            g2d.setColor(penColor);
            g2d.drawLine(from.x, from.y, to.x, to.y);
        }
    }

    // method to render all content onto the canvas
    private void renderAllContentOnCanvas() {
        // Create a new BufferedImage to draw everything on
        BufferedImage newCanvas = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newCanvas.createGraphics();

        // Draw the existing canvas content
        g2d.drawImage(canvas, 0, 0, null);

        // Draw all creations
        for (Creation creation : creations) {
            if (!(creation instanceof TextCreation)) {
                creation.draw(g2d);
            }
        }
        // Draw all text creations
        for (TextCreation textCreation : textCreations) {
            textCreation.draw(g2d);
        }

        g2d.dispose();

        // Replace the old canvas with the new one
        canvas = newCanvas;
        this.g2d = canvas.createGraphics();
        this.g2d.setColor(penColor);
        this.g2d.setStroke(new BasicStroke(penSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the canvas image
        g.drawImage(canvas, 0, 0, null);

        // Draw all creations
        for (Creation creation : creations) {
            if (!(creation instanceof TextCreation)) {
                creation.draw((Graphics2D) g);
            }
        }
        // Draw all text creations
        for (TextCreation textCreation : textCreations) {
            textCreation.draw((Graphics2D) g);
        }
    }

}