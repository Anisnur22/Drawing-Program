import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;

public class ToolBarPanel extends JPanel {
    private RightCanvas rightCanvas;
    private LeftCanvas leftCanvas;
    private Graphics2D g2d;
    private Color currentPenColor;
    private int currentPenSize;

    public ToolBarPanel(RightCanvas rightCanvas, LeftCanvas leftCanvas, Graphics2D g2d) {
        this.rightCanvas = rightCanvas;
        this.leftCanvas = leftCanvas;
        this.g2d = g2d;
        currentPenColor = Color.BLACK;
        currentPenSize = 5;
        setPreferredSize(new Dimension(300, 50));
        addToolBarButtons();
    }

    // Add buttons with icons and labels to the toolbar
    private void addToolBarButtons() {
        add(createButtonWithTooltip("img/icon/text.png", "Text", "Add Text"));
        add(createButtonWithTooltip("img/icon/pen.png", "Pen", "Draw with Pen"));
        add(createButtonWithTooltip("img/icon/circle.png", "Size", "Change Pen Size"));
        add(createButtonWithTooltip("img/icon/color.png", "Color", "Change Pen Color"));
        add(createButtonWithTooltip("img/icon/flower.png", "Flower", "Add Flower Image"));
        add(createButtonWithTooltip("img/icon/cat.png", "Animal", "Add Animal Image"));
        add(createButtonWithTooltip("img/icon/plus.png", "Custom", "Add Custom Image"));
        add(createButtonWithTooltip("img/icon/save.png", "Save", "Save Image"));
        add(createButtonWithTooltip("img/icon/rotate_image.png", "Rotate Image Left", "Rotate Image"));
        add(createButtonWithTooltip("img/icon/rotate_left.png", "Rotate Left", "Rotate Left Canvas"));
        add(createButtonWithTooltip("img/icon/rotate_right.png", "Rotate Right", "Rotate Right Canvas"));
        add(createButtonWithTooltip("img/icon/compose.png", "Compose", "Compose Canvas"));
        add(createButtonWithTooltip("img/icon/clear.png", "Clear Left Canvas", "Clear Left Canvas"));
    }

    private JButton createButtonWithTooltip(String iconPath, String command, String tooltipText) {
        JButton button = createToolBarButton(iconPath, command);
        button.setToolTipText(tooltipText);
        return button;
    }

    private JButton createToolBarButton(String iconPath, String command) {
        // get icon image resources
        URL iconURL = getClass().getClassLoader().getResource(iconPath);
        ImageIcon icon = null;
        if (iconURL != null) {
            try {
                BufferedImage img = ImageIO.read(iconURL);
                Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // constant icon size
                icon = new ImageIcon(scaledImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Icon not found: " + iconPath);
        }

        JButton button = new JButton(icon);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setActionCommand(command);
        button.addActionListener(new ToolBarButtonListener());

        // This creates a slider for pen size
        if (command.equals("Size")) {
            JSlider penSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 30, 4); // Min, Max, Initial
            penSizeSlider.addChangeListener(e -> {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int newSize = source.getValue();
                    RightCanvas.setPenSize(newSize, rightCanvas.getGraphics2D());
                }
            });
            JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.add(penSizeSlider);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    popupMenu.show(button, 0, button.getHeight());
                }
            });
        }
        return button;
    }

    private void toggleDrawingEnabled() {
        boolean enabled = !rightCanvas.isDrawingEnabled();
        rightCanvas.setDrawingEnabled(enabled);
    }

    // Tools Operation
    private class ToolBarButtonListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "Text":
                    System.out.println("Text button clicked");
                    rightCanvas.addText();
                    break;

                case "Pen":
                    System.out.println("Pen button clicked");
                    RightCanvas.setPenColor(currentPenColor);
                    toggleDrawingEnabled();
                    break;

                case "Size":
                    System.out.println("Circle button clicked");
                    RightCanvas.setPenSize(currentPenSize, g2d);
                    break;

                case "Color":
                    System.out.println("Color button clicked");
                    currentPenColor = JColorChooser.showDialog(null, "Choose Pen Color", Color.BLACK);
                    RightCanvas.setPenColor(currentPenColor);
                    break;

                case "Flower":
                    FileOperation.protectFolder("img\\flower");
                    BufferedImage flowerImage = FileOperation.chooseImage("img/flower");
                    if (flowerImage != null) {
                        FlowerCreation flower = new FlowerCreation(flowerImage, new Point(0, 0));
                        leftCanvas.addCreation(flower);
                    }
                    break;

                case "Animal":
                    FileOperation.protectFolder("img\\animal");
                    BufferedImage animalImage = FileOperation.chooseImage("img/animal");
                    if (animalImage != null) {
                        AnimalCreation animal = new AnimalCreation(animalImage, new Point(0, 0));
                        leftCanvas.addCreation(animal);
                    }
                    break;

                case "Custom":
                    FileOperation.protectFolder("img\\custom");
                    BufferedImage customImage = FileOperation.chooseImage("img/custom");
                    if (customImage != null) {
                        CustomCreation custom = new CustomCreation(customImage, new Point(0, 0));
                        leftCanvas.addCreation(custom);
                    }
                    break;

                case "Save":
                    System.out.println("Save button clicked");
                    FileOperation.saveImage(rightCanvas.getDrawing());
                    break;

                case "Rotate Image Left":
                    System.out.println("Rotate Image Left button clicked");
                    if (leftCanvas.getSelectedCreation() != null) {
                        leftCanvas.getSelectedCreation().rotate();
                        leftCanvas.repaint();
                    }
                    break;

                case "Rotate Left":
                    System.out.println("Rotate button clicked");
                    leftCanvas.rotateCanvas();
                    leftCanvas.repaint();
                    break;

                case "Rotate Right":
                    System.out.println("Rotate Right Canvas button clicked");
                    rightCanvas.rotateCanvas();
                    rightCanvas.repaint();
                    break;

                case "Compose":
                    BufferedImage compositeImage = leftCanvas.getCompositeImage();
                    rightCanvas.setImage(compositeImage);
                    break;

                case "Clear Left Canvas":
                    System.out.println("Clear Left Canvas button clicked");
                    leftCanvas.clearCanvas();
                    break;

                default:
                    throw new IllegalStateException("Command error " + command);
            }
        }
    }
}
