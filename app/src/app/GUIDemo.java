package app;

// Java AWT Program for Hello World
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// CODE RIPPED FROM geeksforgeeks.org
// Driver Class
public class GUIDemo {
    // main function
    public static void main(String[] args) {
        // Declaring a Frame and Label
        Frame frame = new Frame("Basic Program");
        Label label = new Label("Hello World!");

        // Aligning the label to CENTER
        label.setAlignment(Label.CENTER);

        // Adding Label and Setting
        // the Size of the Frame
        frame.add(label);
        frame.setSize(300, 300);

        // Making the Frame visible
        frame.setVisible(true);

        // Using WindowListener for closing the window
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
