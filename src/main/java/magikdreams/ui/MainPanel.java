package magikdreams.ui;

import magikdreams.core.Resizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import static magikdreams.core.Resizer.DOWNLOADS_DIRECTORY;

public class MainPanel extends JFrame {
    private static final int WIDTH = 350;
    private static final int HEIGHT = 175;

    private final Resizer resizer;
    private final JTextField directoryPathTextField;
    private final JCheckBox renameCheckBox;

    public MainPanel(final Resizer resizer) {
        super("Image Resizer");
        this.resizer = resizer;
        resizer.setMainPanel(this);
        final WindowListener windowListener = new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                System.exit(0);
            }
        };

        addWindowListener(windowListener);
        setSize(WIDTH, HEIGHT);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        final JTextPane directoryPathLabel = new JTextPane();
        directoryPathLabel.setSize(200, 25);
        directoryPathLabel.setLocation(10, 10);
        directoryPathLabel.setText("Répertoire d'images");
        directoryPathLabel.setBackground(Color.WHITE);
        directoryPathLabel.setEditable(false);
        directoryPathLabel.setVisible(true);
        add(directoryPathLabel);

        directoryPathTextField = new JTextField();
        directoryPathTextField.setSize(195, 25);
        directoryPathTextField.setLocation(13, 40);
        directoryPathTextField.setText("/");
        directoryPathTextField.setEditable(false);
        directoryPathTextField.setVisible(true);
        add(directoryPathTextField);

        final JButton directorySelectorButton = new JButton("Parcourir");
        directorySelectorButton.setSize(100, 25);
        directorySelectorButton.setLocation(215, 40);
        directorySelectorButton.addActionListener(event -> openDirectorySelector());
        directorySelectorButton.setVisible(true);
        add(directorySelectorButton);

        renameCheckBox = new JCheckBox();
        renameCheckBox.setSize(20, 20);
        renameCheckBox.setLocation(10, 70);
        renameCheckBox.setBackground(Color.WHITE);
        renameCheckBox.setVisible(true);
        add(renameCheckBox);

        final JTextPane renameLabel = new JTextPane();
        renameLabel.setSize(200, 25);
        renameLabel.setLocation(30, 70);
        renameLabel.setText("Renommer les fichiers ?");
        renameLabel.setBackground(Color.WHITE);
        renameLabel.setEditable(false);
        renameLabel.setVisible(true);
        add(renameLabel);

        final JButton startButton = new JButton("Start");
        startButton.setSize(100, 25);
        startButton.setLocation(110, 100);
        startButton.addActionListener(event -> start());
        startButton.setVisible(true);
        add(startButton);
    }

    private void openDirectorySelector() {
        final JFileChooser fileChooser = new JFileChooser(); // *** number 1
        fileChooser.setCurrentDirectory(new java.io.File(DOWNLOADS_DIRECTORY));
        fileChooser.setSelectedFile(new File(""));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fileChooser.showOpenDialog(this) == JFileChooser.OPEN_DIALOG) {
            final File selectedFile = fileChooser.getSelectedFile();
            directoryPathTextField.setText(selectedFile.getAbsolutePath());
            resizer.setImagesDirectory(selectedFile);
        }
    }

    private void start() {
        if (!resizer.hasSelectedFile()) {
            popup("Il faut d'abord choisir un fichier ou un dossier");
            return;
        }
        resizer.process(renameCheckBox.isSelected());
    }

    public void popup(final String text) {
        final PopupPanel popupPanel = new PopupPanel(text);
        popupPanel.setVisible(true);
    }
}
