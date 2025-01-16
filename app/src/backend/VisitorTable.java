package backend;

import java.util.Comparator;
import datastructs.Table;

/**
 * Table that stores visitors. No two visitors may have the same visitor ID.
 * 
 * @author Ethan Begley
 * @version 12/9/2024
 */
public class VisitorTable extends Table {

    /**
     * Creates a VisitorTable
     */
    public VisitorTable() {
        super(new String[] { "visitorID", "firstName", "middleName",
            "lastName" }, new CompareVisitors());
    }


    /**
     * TODO comment
     * 
     * @param fullName
     * @param visitorID
     */
    public void add(String[] fullName, int visitorID) {
        if (fullName.length != 3) {
            throw new IllegalArgumentException(
                "String arr fullName must have length == 3");
        }
        if (visitorID < 10001) {
            throw new IllegalArgumentException(
                "visitorID must be at least 10001");
        }

        for (Object currID : rows) {
            if ((int)currID == visitorID) {
                throw new IllegalStateException(
                    "visitorID already exists in visitor table. Try "
                        + generateVisitorID());
            }
        }

        rows.add(new Object[] { visitorID, fullName[0], fullName[1],
            fullName[2] });
    }


    /**
     * Adds an entry to the table in order. This method ensures the rules
     * outlined in the class description are met before adding an entry.
     * 
     * @param fullName
     *            The name of the visitor you want to add
     */
    public void add(String[] fullName) {
        if (fullName.length != 3) {
            throw new IllegalArgumentException(
                "String arr fullName must have length == 3");
        }

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
