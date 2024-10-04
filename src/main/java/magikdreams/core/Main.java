package magikdreams.core;

import magikdreams.ui.MainPanel;

public class Main {
    public static void main(String[] args) {
        final Resizer resizer = new Resizer();
        final MainPanel mainPanel = new MainPanel(resizer);
        mainPanel.setVisible(true);
    }
}
