package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.ArrayList;
import backend.Visit;
import backend.Visit.VisitKey;
import backend.VisitTable;
import backend.VisitorTable;
import dataviews.OngoingVisit;
import modelviewtools.DataDelegate;

public class DataManager extends DataDelegate {

    /**
     * The following constants define commands that can be passed to a
     * Signal to specify what change the delegate made
     */
    public static final String ADDED_VISIT = "addedvisit";
    public static final String ADDED_VISITOR = "addedvisitor";
    public static final String REMOVED_VISIT = "removedvisit";
    public static final String REMOVED_VISITOR = "removedvisitor";
    public static final String STARTED_VISIT = "startedvisit";
    public static final String ENDED_VISIT = "endedvisit";

    private VisitorTable visitors; // A table of all visitors
    private VisitTable visits; // A table of all visits

    /**
     * Initializes the Database with a given CSV file
     */
    public DataManager() {
    }


    /**
     * TODO decide what the return type will be
     * 
     * @return
     */
    public OngoingVisit[] getOngoingVisits() {

        ArrayList<OngoingVisit> ongoingVisits = new ArrayList<OngoingVisit>();
        
        for (Object[] visit: visits.entriesWhere(new String[] {"visitorID", } ))
        
    }


    public Visitor getVisitor(int visitorID) {
        return table.entry(visitorID);
    }


    public Visit getVisit(VisitKey key) {
        return (Visit)((VisitTable)table.entry(key.visitorID()).field(
            "history")).entry(key);
    }


    /**
     * Adds a listener that will be notified as to this Delegate's actions
     * 
     * @param listener
     *            Listener to be notified by this Delegate
     */
    public void addListener(ActionListener listener) {
        listeners.add(listener);
    }


    /**
     * Adds a visit to the attendance table.
     * 
     * @param visitorID
     *            ID of student the visit belongs to
     * @param startTime
     *            of visit
     * @param endTime
     *            of visit
     */
    public void addVisit(
        int visitorID,
        LocalTime startTime,
        LocalTime endTime) {

        VisitTable visitorVisits = (VisitTable)table.entry(visitorID).field(
            "history");
        Visit newVisit = new Visit(visitorID, startTime, endTime);

        visitorVisits.add(newVisit);

        // Notifies listeners of change in database
        if (newVisit.isOngoing()) {
            signalViews(new ActionEvent(this, visitorID, "addedOngoing"));
        }
        else {
            signalViews(new ActionEvent(this, visitorID, "addedVisit"));
        }

    }


    public void endVisit(int visitorID) throws Exception {

        Visitor visitor = table.entry(visitorID);

        // If visitor not visiting, throw exception
        if (!visitor.isVisiting()) {
            throw new Exception("Student has no visit to end");
        }

        // Fetches current ongoing visit and visitor history
        VisitTable visitorHistory = (VisitTable)visitor.field("history");
        Visit ongoingVisit = visitorHistory.entry(0);

        // Makes new visit with old startTime and endTime as the currentTime
        LocalTime currentTime = LocalTime.now();
        Visit closedVisit = new Visit(visitorID, (LocalTime)ongoingVisit.field(
            "startTime"), currentTime);

        // Removes ongoing visit, replaces it with a closed version
        visitorHistory.remove(0);
        visitorHistory.add(closedVisit);

        signalViews(new ActionEvent(this, visitorID, "endedOngoing"));

    }


    /**
     * Notifies all ActionListeners that an action has occurred
     * 
     * @param e
     *            The ActionEvent that has occurred
     */
    private void signalDataViews(ActionEvent e) {
        for (ActionListener listener : listeners) {
            listener.actionPerformed(e);
        }
    }

}
