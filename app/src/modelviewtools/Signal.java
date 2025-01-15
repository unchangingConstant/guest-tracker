package modelviewtools;

/**
 * This is part of the Model-View implementation for this program. A Signal will
 * be sent by a DataDelegate to a DataView whenever the delegate makes a change
 * to its model.
 * 
 * The Signal stores a message that can be read by a DataView. The DataView can
 * then take the appropriate course of action according to the message.
 * 
 * @author Ethan Begley
 * @version 1/10/2025
 */
public class Signal {

    private String message; // The message to be sent to a DataView

    /**
     * Declares a Signal with a message.
     * 
     * @param message
     *            The message you want to send the DataView.
     */
    public Signal(String message) {
        this.message = message;
    }


    /**
     * Returns the message stored by the Signal. It's only meant for the
     * DataView to see, hence, its protected status.
     * 
     * @return The message to be carried to the DataView.
     */
    protected String message() {
        return message;
    }

}
