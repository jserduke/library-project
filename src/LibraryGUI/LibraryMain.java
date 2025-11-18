package LibraryGUI;

import javax.swing.SwingUtilities;

public class LibraryMain {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LibrarySystemDashboard(null).setVisible(true));
	}	
}


