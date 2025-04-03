package backend;

import java.util.Comparator;
import datastructs.Table;

/**
 * Table that stores visitors. This table follows these rules:
 * 
 * - No visitor may have a visitor ID lower than 10001 or greater than 99999
 * - No two visitors may have the same visitor ID.
 * - Each visitor must have a first and last name (not null or empty string)
 * 
 * TODO Create unit tests for row by ID
 * 
 * @author Ethan Begley
 * @version 12/9/2024
 */
public class VisitorTable extends Table {

    /**
     * Creates a VisitorTable
     */
    public VisitorTable() {
        // Calls super constructor with fixed column names and custom
        // comparator. This table is sorted by visitorID.
        super(new String[] { "visitorID", "firstName", "middleName",
            "lastName" }, new CompareVisitors());
    }


    /**
     * This fetches a visitor from the visitor table by ID
     * 
     * @return The visitor with the given visitorID. If visitor not found,
     *         returns null
     */
    public Object[] row(int visitorID) {
        // Looks for visitor with the given visitorID
        // Will throw IndexOutOfBounds exception if visitor not found
        return entriesWhere("visitorID", visitorID).row(0);
    }


    /**
     * Adds an entry in sorted order to the table. Ensures entry follows all
     * table rules. This method is best for filling a table from a pre-existing
     * table, where the added visitors already have preset visitorID's.
     * 
     * @param fullName
     *            The name of the visitor
     * @param visitorID
     *            The visitor's unique ID
     */
    public void add(String[] fullName, int visitorID) {
        // Checks that the name array is of proper length, 3.
        if (fullName.length != 3) {
            throw new IllegalArgumentException(
                "String arr fullName must have length == 3");
        }
        // Makes sure entry has a valid ID
        if (visitorID < 10001 || visitorID > 99999) {
            throw new IllegalArgumentException(
                "visitorID must be at least 10001 and no more than 99999");
        }

        // Optimize this? There has to be a better way given that the list is
        // ordered
        // Checks that visitor's ID doesn't already exist in the table
        for (Object currID : rows) {
            if ((int)currID == visitorID) {
                throw new IllegalStateException(
                    "visitorID already exists in visitor table. Try "
                        + generateVisitorID());
            }
        }

        // Invokes super add method to add the row to the table.
        super.add(new Object[] { visitorID, fullName[0], fullName[1],
            fullName[2] });
    }


    /**
     * Adds an entry to the table in order. This method ensures the new entry
     * follows all table rules and generates an ID for the new visitor.
     * 
     * @param fullName
     *            The name of the visitor you want to add
     */
    public void add(String[] fullName) {
        if (fullName.length != 3) {
            throw new IllegalArgumentException(
                "String arr fullName must have length == 3");
        }

        // Creates new row with a generated ID.
        Object[] visitor = new Object[] { generateVisitorID(), fullName[0],
            fullName[1], fullName[2] };
        super.add(visitor);
    }


    /**
     * Throws error whenever called to ensure the superclass version of this
     * method isn't used.
     * 
     * @throws UnsupportedOperationException
     *             If this method is called at all.
     */
    @Override
    public void add(Object[] row) {
        throw new UnsupportedOperationException(
            "Method not supported by VisitorTable. Try add(String[] fullName)");
    }


    /**
     * Creates an unused visitorID
     * 
     * @return unused visitorID
     */
    private int generateVisitorID() {
        int currID = 10001;

        for (Object[] row : rows) {
            if ((int)row[0] != currID) {
                return currID;
            }
            currID++;
        }

        if (currID > 99999) {
            throw new IllegalStateException(
                "No ID can be generated for this table. Max table capacity has been reached");
        }

        return currID;

    }

    /**
     * Comparator that will sort this visitor table.
     */
    static class CompareVisitors implements Comparator<Object[]> {

        /**
         * Compares two visitors by studentID
         * 
         * @param visitor1
         *            First visitor to compare
         * @param visitor2
         *            Second visitor to compare
         */
        public int compare(Object[] visitor1, Object[] visitor2) {
            return (int)visitor1[0] - (int)visitor2[0];
        }

    }

}
