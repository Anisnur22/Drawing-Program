import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileOperation {
    private static boolean folderProtected = false;
    private static String protectedFolderPath = null;

    public static BufferedImage chooseImage(String directoryPath) {
        JFileChooser fileChooser = new JFileChooser(directoryPath) {
            @Override
            public void approveSelection() {
                File file = getSelectedFile();
                String parentPath = file.getParent().replace("/", "\\"); // Convert backslashes to forward slashes
                String relativePath = parentPath.substring(parentPath.indexOf("img"));
                if (folderProtected && !protectedFolderPath.equals(relativePath)) {
                    System.out.println("Cannot select files outside " + directoryPath);
                    // System.out.println("Protected Folder:" + protectedFolderPath);
                    // System.out.println("Parent Folder:" + relativePath);
                    return;
                }
                super.approveSelection();
            }
        };

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (folderProtected) {
            fileChooser.setCurrentDirectory(new File(protectedFolderPath));
            // fileChooser.setControlButtonsAreShown(false); // Hide "Up One Level" button
        }

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                return ImageIO.read(selectedFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static void protectFolder(String folderPath) {
        folderProtected = true;
        protectedFolderPath = folderPath;
        System.out.println("Folder protected:" + folderPath); // checking folder
    }

    public static void saveImage(BufferedImage image) {
        JFileChooser fileChooser = new JFileChooser("img/custom"); // choose file
        fileChooser.setDialogTitle("Save Drawing");
        fileChooser.setSelectedFile(new File("custom_drawing.png")); // name of saved file
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            File folder = new File("img/custom"); // save in folder img/custom
            File fullPath = new File(folder, fileToSave.getName());
            try {
                ImageIO.write(image, "png", fullPath);
                System.out.println("Saved drawing to " + fullPath.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
