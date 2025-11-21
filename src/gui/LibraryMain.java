package gui;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class LibraryMain {
    public static void main(String[] args) {
    	ArrayList<String> info = new ArrayList<String>();
    	info.add("Local Library");
        SwingUtilities.invokeLater(() -> new WelcomeDashboardFrame(null, null, info).setVisible(true));
    }
}
