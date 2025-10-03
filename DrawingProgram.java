import javax.swing.*;
import java.awt.*;

// MAIN
public class DrawingProgram {

    public static void main(String[] args) {
        JFrame frame = new JFrame("OOAD TT2L Group 5 Drawing Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // The canvas
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setEnabled(false); // fixes the divider in place

        // Left side for composing images
        LeftCanvas leftCanvas = new LeftCanvas();
        leftCanvas.setBorder(BorderFactory.createLineBorder(Color.red));
        splitPane.setLeftComponent(leftCanvas);

        // Right side for drawing
        RightCanvas rightCanvas = new RightCanvas();
        rightCanvas.addMouseListener(rightCanvas);
        rightCanvas.addMouseMotionListener(rightCanvas);
        rightCanvas.setBorder(BorderFactory.createLineBorder(Color.red));
        splitPane.setRightComponent(rightCanvas);

        frame.add(splitPane, BorderLayout.CENTER);

        // The toolbar
        ToolBarPanel toolBarPanel = new ToolBarPanel(rightCanvas, leftCanvas, rightCanvas.getGraphics2D());
        toolBarPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        frame.add(toolBarPanel, BorderLayout.SOUTH);

        // adjusting JFrame
        frame.setSize(1000, 600);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
