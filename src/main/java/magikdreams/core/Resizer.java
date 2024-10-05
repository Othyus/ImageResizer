package magikdreams.core;

import magikdreams.ui.MainPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public class Resizer {
    public static final String DOWNLOADS_DIRECTORY = System.getProperty("user.home") + FileSystems.getDefault().getSeparator() + "Downloads";
    private MainPanel mainPanel;
    private File imagesDirectory;
    private File targetImagesDirectory;

    public void setImagesDirectory(final File imagesDirectory) {
        this.imagesDirectory = imagesDirectory;
    }

    public void setTargetImagesDirectory(final File imagesDirectory) {
        this.targetImagesDirectory = imagesDirectory;
    }

    public boolean hasSelectedFile() {
        return imagesDirectory != null && targetImagesDirectory != null;
    }

    public void process(final String outputPattern, final int percentage) {
        System.out.println("Processing with " + imagesDirectory);

        if (!targetImagesDirectory.isDirectory()) {
            mainPanel.popup("La destination doit être un répertoire");
            return;
        }

        if (imagesDirectory.isDirectory()) {
            boolean withError = false;
            final File[] files = imagesDirectory.listFiles();
            if (files == null) {
                mainPanel.popup("Aucun fichier trouvé");
                return;
            }
            for (int i = 0; i < files.length; i++) {
                if (!processFile(files[i], targetImagesDirectory.getAbsolutePath(), outputPattern, percentage, i)) {
                    withError = true;
                }
            }
            mainPanel.popup("Terminé" + (withError ? " avec des erreurs" : ""));
        } else {
            if (processFile(imagesDirectory, targetImagesDirectory.getAbsolutePath(), outputPattern, percentage, 1)) {
                mainPanel.popup("Terminé");
            } else {
                mainPanel.popup("Erreur");
            }
        }
    }

    public boolean processFile(final File file, final String outputDirectory, final String outputPattern, final int percentage, final int count) {
        System.out.println("Process file <" + file.getName() + ">");
        try {
            final BufferedImage sourceImage = ImageIO.read(file);
            if (sourceImage == null) {
                mainPanel.popup("Impossible de lire l'image");
                return false;
            }

            final String[] fileData = splitFileNameAndExtension(file.getName());
            final BufferedImage resizedImage = resizeImage(sourceImage, percentage, fileData[1]);

            final File outputFile = new File(outputDirectory + FileSystems.getDefault().getSeparator() + outputPattern.replaceAll("\\$i", "" + (count+1)) + "." + fileData[1]);
            final boolean created = outputFile.createNewFile();
            if (!created) {
                mainPanel.popup("Impossible de créer le fichier");
                return false;
            }

            return ImageIO.write(resizedImage, fileData[1], outputFile);
        } catch (final IOException exception) {
            mainPanel.popup(exception.getMessage());
        }
        return false;
    }

    public BufferedImage resizeImage(final BufferedImage image, final int percentage, final String format) {
        final int newWidth = (image.getWidth() * percentage) / 100;
        final int newHeight = (image.getHeight() * percentage) / 100;
        final Image scaledInstance = image.getScaledInstance(newWidth, newHeight, 1);

        final BufferedImage result = new BufferedImage(newWidth, newHeight, "png".equalsIgnoreCase(format) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);

        final Graphics2D graphics = result.createGraphics();
        graphics.drawImage(scaledInstance, 0, 0, null);
        graphics.dispose();

        return result;
    }

    public void setMainPanel(final MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    private String[] splitFileNameAndExtension(final String fileName) {
        if (fileName.indexOf(".") > 0) {
            return new String[]{fileName.substring(0, fileName.lastIndexOf(".")), fileName.substring(fileName.lastIndexOf(".") + 1)};
        } else {
            return new String[]{fileName, fileName};
        }
    }
}
