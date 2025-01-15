package backend;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import datastructs.Table;

/**
 * A table of visits.
 * 
 * Each visit must follow these rules:
 * - No two visits from the same visitor may overlap time-wise.
 * - No two visits from the same visitor may be ongoing at the same time.
 * - The only null field any visit may have is an endTime. (A null endTime
 * indicates an ongoing visit)
 * - An ongoing visit can be added so long as the startTime does not fall during
 * any existing visit (Visit.isOverlapping() method accounts for this)
 * - A visit's visitor ID must correspond to a visitor in passed VisitorTable.
 * (The visitor the visit belongs to must exist)
 * 
 * @author Ethan Begley
 * @version 1/5/2024
 */
public class VisitTable extends Table {

    // VisitorTable reference used to ensure that all visits belong to an
    // existing visitor
    private Table visitors;

    /**
     * Creates a visit table. A visit table must have a Visitor table to
     * reference.
     */
    public VisitTable(Table visitors) {
        super(new String[] { "visitorID", "startTime", "endTime" },
            new CompareVisits());
    }


    /**
     * Adds an entry to the table in order. This method ensures all rules
     * outlined in the class description are met before an entry is added to the
     * table
     * 
     * TODO optimize overlap checking logic. In an ordered list, you could make
     * it faster, no?
     * TODO comment parameters bruv
     * 
     * @param visitorID
     * @param startTime
     * @param endTime
     */
    public void add(int visitorID, LocalTime startTime, LocalTime endTime) {
        // Checks that fields are not null (Except for endTime)
        if (startTime == null) {
            throw new IllegalArgumentException(
                "Visit must have a startTime, startTime cannot be null");
        }

        Object[] newVisit = new Object[] { visitorID, startTime, endTime };

        // Checks that visit belongs to existing student
        if (visitors.entriesWhere(0, visitorID).rowCount() == 0) {
            throw new IllegalStateException(
                "Visit must belong to existing student");
        }

        Table visitorVisits = entriesWhere(0, visitorID);

        // Checks that visit doesn't overlap with another visit belonging to the
        // same visitor
        for (Object[] visit : visitorVisits) {
            if (visitsOverlap(visit, newVisit)) {
                throw new IllegalStateException(
                    "Two visits with the same student may not overlap in a VisitTable");
            }
        }

        // Ensures visit isn't ongoing if visitor already has an ongoing visit
        if (newVisit[2] == null && visitorVisits.entriesWhere(2, null)
            .rowCount() > 0) {
            throw new IllegalStateException(
                "Two visits with the same student may not be ongoing");
        }

        // If all tests are passed, finally adds entry
        add(newVisit);
    }


    /**
     * Overrides super add() method to indicate it's not usable anymore.
     * 
     * @throws UnsupportedOperationException
     *             To indicate this method is not available in subclass.
     */
    @Override
    public void add(Object[] row) {
        throw new UnsupportedOperationException(
            "Method not supported by VisitTable. Try add(int visitorID, LocalTime startTime, LocalTime endTime)");
    }


    /**
     * Checks whether two visits overlap or not. If one of the visits is
     * ongoing, checks that the ongoing visit doesn't start during the other
     * visit. If both are ongoing, return false. If visits are equal to each
     * other, return true.
     * 
     * @param visit1
     *            First visit in question
     * @param visit2
     *            Second visit in question
     * @return Whether the two visits overlap or not
     */
    private boolean visitsOverlap(Object[] visit1, Object[] visit2) {
        // If visits are equal to each other
        if (Arrays.equals(visit1, visit2)) {
            return true;
        }
        // If both are ongoing, not overlapping
        if (visit1[2] == null && visit2[2] == null) {
            return false;
        }
        // If one of the visits are ongoing, if ongoing visit's startTime is
        // during other visit, overlapping
        if (visit1[2] == null) {
            return ((LocalTime)visit1[1]).compareTo((LocalTime)visit2[1]) > 0
                && ((LocalTime)visit1[1]).compareTo((LocalTime)visit2[2]) < 0;
        }
        if (visit2[2] == null) {
            return ((LocalTime)visit2[1]).compareTo((LocalTime)visit1[1]) > 0
                && ((LocalTime)visit2[1]).compareTo((LocalTime)visit1[2]) < 0;
        }
        // If none are ongoing, checks if one visit ends before the other
        // one starts
        return ((LocalTime)visit1[2]).compareTo((LocalTime)visit2[1]) < 0;
    }

    /**
     * Comparator that will sort this visit table.
     */
    static class CompareVisits implements Comparator<Object[]> {

        /**
         * Compares two visits. First by studentID, then by startTime
         * 
         * @param visit1
         *            First visit to compare
         * @param visit2
         *            Second visit to compare
         */
        public int compare(Object[] visit1, Object[] visit2) {
            int comparison = (int)visit1[0] - (int)visit2[0];

            if (comparison == 0) {
                return ((LocalTime)visit1[1]).compareTo((LocalTime)visit2[1]);
            }
            return comparison;
        }

    }
}
