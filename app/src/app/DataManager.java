package app;

import java.time.LocalTime;
import backend.VisitTable;
import backend.VisitorTable;
import datastructs.Table;
import modelviewtools.DataDelegate;
import modelviewtools.Signal;

/**
 * TODO Ensure the DataManager only sends signals upon SUCCESSFUL changes!!!
 */
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

    // TODO Add constructor that takes CSV file

    /**
     * Initializes the Database with a given CSV file
     */
    public DataManager() {
        this.visitors = new VisitorTable();
        this.visits = new VisitTable(visitors);
    }


    /**
     * Returns a Table with all currently ongoing visits
     */
    public Table getOngoingVisits() {
        return visits.entriesWhere("startTime", null);
    }


    /**
     * Returns an Object array representing an entry in the visitor table, said
     * entry representing the visitor with the given visitorID
     * 
     * @param visitorID
     * @return The visitor with the given visitorID
     */
    public Object[] getVisitor(int visitorID) {
        return visitors.row(visitorID);
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
        visits.add(visitorID, startTime, endTime);
        signalDataViews(new Signal(ADDED_VISIT));
    }


    /**
     * Ends a given visitor's ongoingVisit
     * 
     * @param visitorID
     *            The visitor whose ongoing visit you want to end
     * @throws Exception
     *             TODO Decide what kind of exception this will throw
     */
    public void endVisit(int visitorID) throws Exception {
        // Fetches the visitor's current ongoing visit
        String[] checkedColumns = new String[] { "visitorID", "endTime" };
        Object[] criteria = new Object[] { visitorID, null };
        Object[] ongoingVisit = visits.entriesWhere(checkedColumns, criteria)
            .row(0);
        // Removes the ongoingVisit
        visits.remove(ongoingVisit);
        // Adds an endtime to the ongoingVisit
        ongoingVisit[2] = LocalTime.now();
        // Adds the ongoingVisit with the endTime back into the table
        visits.add(ongoingVisit);

        signalDataViews(new Signal(ENDED_VISIT));

    }
}
