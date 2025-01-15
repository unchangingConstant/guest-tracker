package backend;

import java.util.NoSuchElementException;
import datatables.Entry;

/**
 * A visitor tracks its ID, name, and table of visits
 * 
 * @author Ethan Begley
 * @version 1/1/2025
 */
public class Visitor implements Entry<Integer> {

    private int visitorID; // A unique visitor ID, the Key, field 0
    private String[] name; // Visitor's name, fields 1, 2, 3

    /**
     * Creates new visitor. A visitor may not have a null firstName or lastName.
     * TODO looks closely... you didn't complete this constructor dimwit
     * 
     * @param firstName
     *            of the visitor
     * @param middleName
     *            of the visitor
     * @param lastName
     *            of the visitor
     * @param visitorID
     *            The visitor's ID. no two visitors should have the same ID.
     * @throws IllegalArgumentException
     *             When firstName or lastName are null
     */
    public Visitor(
        int visitorID,
        String firstName,
        String middleName,
        String lastName)
        throws IllegalArgumentException {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException(
                "Visitor firstName and lastName can't be null");
        }
        this.visitorID = visitorID;
    }


    /**
     * Returns the field in the given column
     * 
     * @return The field in the given column
     */
    public Object field(int column) {
        if (column == 0) {
            return visitorID;
        }
        if (column < 4 && column > 0) {
            return name[column];
        }
        throw new IndexOutOfBoundsException(
            "Index requested not in bounds [0, 3]");
    }


    /**
     * The field at the entry's given column
     * 
     * @param columnName
     *            The name of the column you want to retrive the field from
     * @return The value of the field at the given column
     */
    public Object field(String columnName) {
        switch (columnName) {
            case "visitorID":
                return visitorID;
            case "firstName":
                return name[0];
            case "middleName":
                return name[1];
            case "lastName":
                return name[2];
            default:
                throw new NoSuchElementException(
                    "No such column name exists in the table");
        }
    }


    /**
     * Returns the number of fields this visitor has.
     * 
     * @return the number of fields this visitor has.
     */
    public int columns() {
        return 4;
    }


    /**
     * The name of each of the entry's columns
     * 
     * @return The name of each of the entry's columns
     */
    public String[] columnNames() {
        return new String[] { "visitorID", "firstName", "middleName",
            "lastName" };
    }


    /**
     * TODO COMMENT!!!
     */
    public Integer key() {
        return visitorID;
    }

}
