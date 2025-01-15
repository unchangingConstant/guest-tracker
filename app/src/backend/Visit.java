package backend;

import java.time.LocalTime;
import java.util.NoSuchElementException;
import backend.Visit.VisitKey;
import datatables.Entry;

/**
 * Represents a visit. Stores the end time and start time of a given visit and
 * provides methods to compare visits.
 * 
 * @author Ethan Begley
 * @version 12/29/2024
 */
public class Visit implements Entry<VisitKey> {

    private VisitKey key; // The key of the entry
    private int visitorID; // ID of the visitor this visit belongs to, field 0
    private LocalTime startTime; // Time visit started, field 1
    private LocalTime endTime; // Time visit ended, field 2

    /**
     * Creates a visit with a startTime and endTime
     * 
     * @param visitorID
     *            The ID of the visitor this visit belongs to
     * @param startTime
     *            Visit's start time (cannot be null)
     * @param endTime
     *            Visit's end time (can be null)
     * @throws Exception
     *             Returns IllegalArgumentException if startTime parameter is
     *             null
     */
    public Visit(int visitorID, LocalTime startTime, LocalTime endTime)
        throws IllegalArgumentException {
        if (startTime == null) {
            throw new IllegalArgumentException(
                "Start time cannot be null for a visit");
        }
        this.visitorID = visitorID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.key = new VisitKey(visitorID, startTime);
    }


    /**
     * A visit's key is its startTime.
     * 
     * @return The time the visit started as a LocalTime object
     */
    public VisitKey key() {
        return key;
    }


    /**
     * TODO comment this!!! and give it an exception!!!!!
     */
    public LocalTime field(int column) {
        if (column == 0) {
            return startTime;
        }
        if (column == 1) {
            return endTime;
        }

        throw new IndexOutOfBoundsException("");
    }


    /**
     * The field at the entry's given column
     * 
     * @param columnName
     *            The name of the column you want to retrieve the field from
     * @return The value of the field at the given column
     */
    public Object field(String columnName) {
        switch (columnName) {
            case "visitorID":
                return visitorID;
            case "startTime":
                return startTime;
            case "endTime":
                return endTime;
            default:
                throw new NoSuchElementException(
                    "No such column name exists in the table");
        }
    }


    /**
     * Returns number of fields this visit entry has
     * 
     * @return Number of fields this visit entry has.
     */
    public int columns() {
        return 3;
    }


    /**
     * Returns true if visit ongoing, returns false if not
     * 
     * @return Boolean indicating whether visit is ongoing or not
     */
    public boolean isOngoing() {
        return endTime == null;
    }


    /**
     * Checks whether two visits overlap or not. If one of the visits is
     * ongoing, checks that the ongoing visit doesn't start during the other
     * visit. If both are ongoing, return false. If visits are equal to each
     * other, return true.
     * 
     * TODO Potentially optimize logic
     * 
     * @param visit
     *            Visit you want to check against
     * @return Whether the two visits overlap or not
     */
    public boolean isOverlapping(Visit visit) {
        // If visits are equal to each other
        if (equals(visit)) {
            return true;
        }
        // If both are ongoing, not overlapping
        if (isOngoing() && visit.isOngoing()) {
            return false;
        }
        // If one of the visits are ongoing, if ongoing visit's startTime is
        // during other visit's startTime, overlapping
        if (isOngoing()) {
            return startTime.compareTo(visit.field(0)) < 0 || startTime
                .compareTo(visit.field(1)) > 0;
        }
        if (visit.isOngoing()) {
            return visit.field(0).compareTo(startTime) < 0 || visit.field(0)
                .compareTo(endTime) > 0;
        }
        // If none are ongoing, checks if one visit ends before the other
        // one starts
        return !(startTime.compareTo(visit.field(1)) > 0 || visit.field(0)
            .compareTo(endTime) > 0);
    }


    /**
     * Evaluates the equality of two visits
     * 
     * @param other
     *            The object you're comparing the visit to
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || !(other instanceof Visit)) {
            return false;
        }
        Visit visit = (Visit)other;

        if (startTime.equals(visit.field(0)) && endTime.equals(visit.field(
            1))) {
            return true;
        }
        return false;
    }


    /**
     * The name of each of the Visits's fields
     * 
     * @return The name of each of the Visits's fields
     */
    public String[] columnNames() {
        return new String[] { "visitorID", "startTime", "endTime" };
    }

    /**
     * Since visits are identified by a combination of visitorID and startTime,
     * this key class will act as the entry's "key"
     */
    public class VisitKey implements Comparable<VisitKey> {

        private int visitorID; // The entry's visitor ID
        private LocalTime startTime; // The entry's startTime

        /**
         * Creates a VisitKey with passed values
         * 
         * @param visitorID
         *            The visitor the visit belongs to
         * @param startTime
         *            The start time of the visit
         */
        public VisitKey(int visitorID, LocalTime startTime) {
            this.visitorID = visitorID;
            this.startTime = startTime;
        }


        /**
         * Returns visitorID of the key
         * 
         * @return visitorID
         */
        public int visitorID() {
            return visitorID;
        }


        /**
         * Returns startTime of the key
         * 
         * @return startTime of the key
         */
        public LocalTime startTime() {
            return startTime;
        }


        /**
         * Compares two keys, first by visitorID. If it's equal by that
         * criteria, then compares by startTime.
         * 
         * - This visitorID is less than other visitorID - positive
         * - This startTime before other startTime - positive
         * 
         * @param key
         *            The key you want to compare to
         * @return See method description for return
         */
        public int compareTo(VisitKey key) {

            if (visitorID != key.visitorID()) {
                return key.visitorID() - visitorID;
            }
            return startTime.compareTo(key.startTime());
        }


        /**
         * Determines equality of two keys
         * 
         * @param key
         *            Key you want to determine
         * @return Whether the two keys are equal or not
         */
        public boolean equals(VisitKey key) {
            if (key == this) {
                return true;
            }
            if (key == null || !(key instanceof VisitKey)) {
                return false;
            }

            VisitKey other = (VisitKey)key;

            return other.compareTo(this) == 0;
        }
    }

}
