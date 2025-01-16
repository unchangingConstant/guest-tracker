package modelviewtools;

import java.util.ArrayList;

/**
 * This is part of the Model-View implementation for this program. The
 * DataDelegate is in charge of mediating all interactions between some GUI and
 * a data structure. This data structure will be referred to as the "Model".
 * 
 * **** First and foremost, this implementation assumes all changes to the model
 * are made SOLELY by the DataDelegate. ****
 * 
 * The DataDelegate tracks DataViews and sends a Signal to all of them whenever
 * it changes the model. The Signal sent will depend on the type of change made.
 */
public abstract class DataDelegate {

    /**
     * Stores a list of all DataViews that need to be sent signals whenever the
     * DataDelegate changes the model.
     */
    private final ArrayList<DataView> signalList = new ArrayList<DataView>();

    /**
     * Base constructor does nothing. Most functionality is defined by the
     * client. This class serves mostly as a guide and structure.
     */
    public DataDelegate() {
    }


    /**
     * Anytime the DataDelegate makes a change to the model, this method should
     * be called. The Signal sent should depend on the kind of change made.
     * 
     * @param signal
     *            The Signal you want to send the models.
     */
    protected void signalDataViews(Signal signal) {
        for (DataView dataView : signalList) {
            dataView.signal(signal);
        }
    }


    /**
     * Adds a DataView to the signal list. The DataView will now be
     * signaled whenever signalDataViews() is called.
     * 
     * @param dataView
     *            DataView you want signaled when signalViews() is called.
     */
    protected void addView(DataView dataView) {
        signalList.add(dataView);
    }


    /**
     * Removes a DataView from the signal list.
     * 
     * @param dataView
     *            DataView you want taken off the signalList
     */
    protected void removeView(DataView dataView) {
        signalList.remove(dataView);
    }

}
