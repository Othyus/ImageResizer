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
    private static final int WIDTH = 380;
    private static final int HEIGHT = 360;

    private final Resizer resizer;
    private final JTextField directoryPathTextField;
    private final JTextField targetDirectoryPathTextField;
    private final JTextField renamePatternTextField;
    private final JList<String> factorList;

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
        directoryPathLabel.setText("Images à redimensionner");
        directoryPathLabel.setBackground(Color.WHITE);
        directoryPathLabel.setEditable(false);
        directoryPathLabel.setVisible(true);
        add(directoryPathLabel);

        directoryPathTextField = new JTextField();
        directoryPathTextField.setSize(195, 25);
        directoryPathTextField.setLocation(13, 37);
        directoryPathTextField.setText("/");
        directoryPathTextField.setEditable(false);
        directoryPathTextField.setVisible(true);
        add(directoryPathTextField);

        final JButton directorySelectorButton = new JButton("Parcourir");
        directorySelectorButton.setSize(100, 25);
        directorySelectorButton.setLocation(215, 37);
        directorySelectorButton.addActionListener(event -> openDirectorySelector());
        directorySelectorButton.setVisible(true);
        add(directorySelectorButton);

        final JTextPane targetDirectoryPathLabel = new JTextPane();
        targetDirectoryPathLabel.setSize(200, 25);
        targetDirectoryPathLabel.setLocation(10, 70);
        targetDirectoryPathLabel.setText("Répertoire de destination");
        targetDirectoryPathLabel.setBackground(Color.WHITE);
        targetDirectoryPathLabel.setEditable(false);
        targetDirectoryPathLabel.setVisible(true);
        add(targetDirectoryPathLabel);

        targetDirectoryPathTextField = new JTextField();
        targetDirectoryPathTextField.setSize(195, 25);
        targetDirectoryPathTextField.setLocation(13, 97);
        targetDirectoryPathTextField.setText("/");
        targetDirectoryPathTextField.setEditable(false);
        targetDirectoryPathTextField.setVisible(true);
        add(targetDirectoryPathTextField);

        final JButton targetDirectorySelectorButton = new JButton("Parcourir");
        targetDirectorySelectorButton.setSize(100, 25);
        targetDirectorySelectorButton.setLocation(215, 97);
        targetDirectorySelectorButton.addActionListener(event -> openTargetDirectorySelector());
        targetDirectorySelectorButton.setVisible(true);
        add(targetDirectorySelectorButton);

        final JTextPane renamePatternLabel = new JTextPane();
        renamePatternLabel.setSize(250, 25);
        renamePatternLabel.setLocation(10, 130);
        renamePatternLabel.setText("Format du fichier créé");
        renamePatternLabel.setBackground(Color.WHITE);
        renamePatternLabel.setEditable(false);
        renamePatternLabel.setVisible(true);
        add(renamePatternLabel);

        renamePatternTextField = new JTextField();
        renamePatternTextField.setSize(100, 25);
        renamePatternTextField.setLocation(13, 157);
        renamePatternTextField.setText("$i");
        renamePatternTextField.setEditable(true);
        renamePatternTextField.setVisible(true);
        add(renamePatternTextField);

        final JTextPane renamePatternExampleLabel = new JTextPane();
        renamePatternExampleLabel.setSize(240, 25);
        renamePatternExampleLabel.setLocation(120, 157);
        renamePatternExampleLabel.setText("Maison-$i -> Maison-1.jpg, Maison-2.jpg");
        renamePatternExampleLabel.setBackground(Color.WHITE);
        renamePatternExampleLabel.setEditable(false);
        renamePatternExampleLabel.setVisible(true);
        add(renamePatternExampleLabel);

        final JTextPane factorLabel = new JTextPane();
        factorLabel.setSize(130, 25);
        factorLabel.setLocation(10, 190);
        factorLabel.setText("Multiplicateur de taille");
        factorLabel.setBackground(Color.WHITE);
        factorLabel.setEditable(false);
        factorLabel.setVisible(true);
        add(factorLabel);

        final String[] factorOptions = {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100", "150", "200"};
        factorList = new JList<>(factorOptions);
        factorList.setSelectedIndex(4);
        factorList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        factorList.setSize(300, 25);
        factorList.setVisibleRowCount(1);

        final JPanel factorPanel = new JPanel();
        factorPanel.setSize(300, 25);
        factorPanel.setLocation(10, 210);
        factorPanel.add(factorList);
        add(factorPanel);

        final JButton startButton = new JButton("Start");
        startButton.setSize(100, 25);
        startButton.setLocation(130, 260);
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

    private void openTargetDirectorySelector() {
        final JFileChooser fileChooser = new JFileChooser(); // *** number 1
        fileChooser.setCurrentDirectory(new java.io.File(DOWNLOADS_DIRECTORY));
        fileChooser.setSelectedFile(new File(""));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fileChooser.showOpenDialog(this) == JFileChooser.OPEN_DIALOG) {
            final File selectedFile = fileChooser.getSelectedFile();
            targetDirectoryPathTextField.setText(selectedFile.getAbsolutePath());
            resizer.setTargetImagesDirectory(selectedFile);
        }
    }

    private void start() {
        if (!resizer.hasSelectedFile()) {
            popup("Il faut d'abord choisir un fichier ou un dossier");
            return;
        }

        try{
            final int percentage = Integer.parseInt(factorList.getSelectedValue());
            resizer.process(renamePatternTextField.getText(), percentage);
        } catch (final NumberFormatException exception) {
            popup("Le multiplicateur choisi n'est pas un entier");
        }
    }

    public void popup(final String text) {
        final PopupPanel popupPanel = new PopupPanel(text);
        popupPanel.setVisible(true);
    }
}
