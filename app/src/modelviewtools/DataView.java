package modelviewtools;

/**
 * This is part of the Model-View implementation for this program. The DataView
 * receives a Signal from the viewedDelegate anytime it makes a change to its
 * model (The model being whatever pool of data the delegate is tracking and
 * modifying).
 * Depending on the signal received, the DataView will change accordingly to
 * accurately represent the model's data after the signal/change.
 * 
 * @author Ethan Begley
 * @version 1/10/2025
 */
public abstract class DataView {

    /**
     * Stores the delegate currently being tracked. The DataView should use it
     * to update itself whenever a signal is received
     */
    protected DataDelegate viewedDelegate;

    /**
     * Takes a dataDelegate upon construction and tracks it.
     * 
     * @param dataDelegate
     *            The DataDelegate this view should track.
     */
    public DataView(DataDelegate dataDelegate) {
        this.viewedDelegate = dataDelegate;
    }


    /**
     * Sets the DataView's tracked DataDelegate
     * 
     * @param dataDelegate
     *            The new tracked DataDelegate
     */
    public void viewDataDelegate(DataDelegate dataDelegate) {
        viewedDelegate = dataDelegate;
    }


    /**
     * This method is for DataDelegates to send signals to it. Hence, its
     * protected status. Write whatever code you need here to properly update
     * the DataView accoding to the Signal received.
     * 
     * @param signal
     *            The Signal sent by the DataDelegate
     */
    protected abstract void signal(Signal signal);

}
