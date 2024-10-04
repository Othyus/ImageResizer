package magikdreams.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class PopupPanel extends JFrame {
    private static final int WIDTH = 275;
    private static final int HEIGHT = 150;

    public PopupPanel(final String text){
        super("Informations");
        final WindowListener windowListener = new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                setVisible(false);
            }
        };

        addWindowListener(windowListener);
        setSize(WIDTH, HEIGHT);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        final JTextField informationsTextField = new JTextField();
        informationsTextField.setSize(250, 25);
        informationsTextField.setLocation(5, 5);
        informationsTextField.setText(text);
        informationsTextField.setEditable(false);
        informationsTextField.setVisible(true);
        add(informationsTextField);

        final JButton closeButton = new JButton("Fermer");
        closeButton.setSize(90, 25);
        closeButton.setLocation(85, 50);
        closeButton.addActionListener(event -> setVisible(false));
        closeButton.setVisible(true);
        add(closeButton);
    }
}
