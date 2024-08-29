package Student;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// Inside the main method before creating the frame
try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
    e.printStackTrace();
}
