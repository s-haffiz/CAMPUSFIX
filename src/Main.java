import javax.swing.SwingUtilities;

// Main class for the project: starts the application
public class Main {
    public static void main(String[] args) {
        // Swing windows should be created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new Frame().setVisible(true));
    }
}