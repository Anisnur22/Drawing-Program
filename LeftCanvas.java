import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LeftCanvas extends JPanel {
   private BufferedImage image;
   private Graphics2D g2d;
   private List<Creation> creations = new ArrayList<>();
   private Creation selectedCreation = null;
   private double rotation = 0;
   private BufferedImage loadedImage;
   private Point loadedImagePosition = new Point(0, 0);
   private boolean waitingForNewCreationPosition = false;
   private Creation newCreation = null;

   public LeftCanvas() {
      setPreferredSize(new Dimension(498, 598));
      initCanvas();
      addMouseListeners();
      updateCursor();
   }

   public void clearCanvas() {
      initCanvas();
      creations.clear();
      rotation = 0;
      loadedImage = null;
      loadedImagePosition = new Point(0, 0);
      waitingForNewCreationPosition = false;
      updateCursor();
      repaint();
   }

   public void addCreation(Creation creation) {
      newCreation = creation;
      waitingForNewCreationPosition = true;
      updateCursor();
   }

   public void rotateCanvas() {
      rotation += Math.PI / 2;
      if (rotation >= 2 * Math.PI) {
         rotation -= 2 * Math.PI;
      }
      updateCanvasSize();
      repaint();
   }

   public Creation getSelectedCreation() {
      return selectedCreation;
   }

   public BufferedImage getImage() {
      return image;
   }

   public void setImage(BufferedImage img) {
      loadedImage = img;
      loadedImagePosition = new Point(0, 0);
      repaint();
   }

   public BufferedImage getCompositeImage() {
      BufferedImage composite = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = composite.createGraphics();

      g2d.rotate(rotation, getWidth() / 2.0, getHeight() / 2.0);

      g2d.drawImage(image, 0, 0, null);

      if (loadedImage != null) {
         g2d.drawImage(loadedImage, loadedImagePosition.x, loadedImagePosition.y, null);
      }

      for (Creation creation : creations) {
         creation.draw(g2d);
      }

      g2d.dispose();
      return composite;
   }

   private void initCanvas() {
      image = new BufferedImage(498, 598, BufferedImage.TYPE_INT_ARGB);
      g2d = image.createGraphics();
      g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
      g2d.dispose();
   }

   private void addMouseListeners() {
      MouseAdapter mouseAdapter = new MouseAdapter() {
         private Point lastPoint;
         private boolean draggingLoadedImage = false;

         @Override
         public void mousePressed(MouseEvent e) {
            lastPoint = e.getPoint();
            if (waitingForNewCreationPosition) {
               addCreationAtPosition(e.getPoint());
               waitingForNewCreationPosition = false;
               newCreation = null;
               updateCursor();
               return;
            }
            if (loadedImage != null && new Rectangle(loadedImagePosition.x, loadedImagePosition.y,
                  loadedImage.getWidth(), loadedImage.getHeight()).contains(e.getPoint())) {
               draggingLoadedImage = true;
            } else {
               selectedCreation = null;
               for (Creation creation : creations) {
                  if (creation.contains(e.getPoint())) {
                     selectedCreation = creation;
                     break;
                  }
               }
            }
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            double scaleFactor = 0.5;
            if (draggingLoadedImage) {
               int dx = (int) ((e.getX() - lastPoint.x) * scaleFactor);
               int dy = (int) ((e.getY() - lastPoint.y) * scaleFactor);
               loadedImagePosition.translate(dx, dy);
               lastPoint = e.getPoint();
               repaint();
            } else if (selectedCreation != null) {
               int dx = (int) ((e.getX() - lastPoint.x) * scaleFactor);
               int dy = (int) ((e.getY() - lastPoint.y) * scaleFactor);
               selectedCreation.update(dx, dy);
               lastPoint = e.getPoint();
               repaint();
            }
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            draggingLoadedImage = false;
         }
      };

      addMouseListener(mouseAdapter);
      addMouseMotionListener(mouseAdapter);
   }

   private void updateCursor() {
      SwingUtilities.invokeLater(() -> {
         if (waitingForNewCreationPosition) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
         } else {
            setCursor(Cursor.getDefaultCursor());
         }
      });
   }

   private void addCreationAtPosition(Point position) {
      if (newCreation != null) {
         newCreation.setPosition(position);
         creations.add(newCreation);
         repaint();
      }
   }

   private void updateCanvasSize() {
      int width = image.getWidth();
      int height = image.getHeight();
      if (rotation % Math.PI == 0) {
         setPreferredSize(new Dimension(width, height));
      } else {
         setPreferredSize(new Dimension(height, width));
      }
      revalidate();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g.create();

      g2d.rotate(rotation, getWidth() / 2.0, getHeight() / 2.0);

      g2d.drawImage(image, 0, 0, null);

      if (loadedImage != null) {
         g2d.drawImage(loadedImage, loadedImagePosition.x, loadedImagePosition.y, null);
      }

      for (Creation creation : creations) {
         creation.draw(g2d);
      }

      g2d.dispose();
   }

}