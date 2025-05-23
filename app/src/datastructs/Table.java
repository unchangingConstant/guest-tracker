package datastructs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Make sure all Objects stored in a table are IMMUTABLE
 * Table is ordered by first field by default
 * 
 * 
 * TODO Add test case that ensures subclasses of table will return a table of
 * the subclass's type when using entriesWhere()
 * TODO check for dumb stuff
 * TODO check that ALL methods have tests
 * TODO Consider creating more scalable frame work for handling errors
 * TODO Very far in the backlog, but consider creating a TableSet class to
 * improve get by ID operations
 */
public class Table implements Iterable<Object[]> {

    /**
     * Name of each column, stored in a HashSet. The Integer value of the
     * columnNames should correspond to that column's index.
     */
    protected HashMap<String, Integer> columnNames;
    // Stores all rows in sorted list (potentially)
    protected SortModeList<Object[]> rows;

    /**
     * Initializes table sorted with given comparator and labelled with given
     * columnNames.
     * 
     * @param columnNames
     *            The name of each column in order.
     * @param comparator
     *            The comparator you want the list to be sorted with
     */
    public Table(String[] columnNames, Comparator<Object[]> comparator) {

        // Makes sure columnNames isn't length 0
        if (columnNames.length == 0) {
            throw new IllegalArgumentException(
                "columnNames cannot be length 0");
        }

        // Makes sure no columnName is null
        for (String name : columnNames) {
            if (name == null) {
                throw new IllegalArgumentException(
                    "null columnName not allowed");
            }
        }

        // Creates a new SortModeList. Each index will represent an entry, each
        // entry is represented by an object array
        this.rows = new SortModeList<Object[]>(comparator);
        this.columnNames = new HashMap<>();

        for (int i = 0; i < columnNames.length; i++) {
            if (this.columnNames.containsKey(columnNames[i])) {
                throw new IllegalArgumentException(
                    "columnName must be unique, no duplicates");
            }
            this.columnNames.put(columnNames[i], i);
        }

    }


    /**
     * Initializes table with passed columnNames
     * 
     * @param columnNames
     *            The name of each column in order.
     */
    public Table(String[] columnNames) {
        this(columnNames, null);
    }


    /**
     * Returns a deep copy of the array at the index. The Objects in the array
     * themselves are references. This is to prevent changes to the table
     * without using table methods. However, if the values stored in the table
     * are mutable, values can still be changed without table methods. This is
     * why objects stored in the table should be immutable.
     * 
     * @param row
     *            The index of the row you want
     * @return The row at the index
     */
    public Object[] row(int row) {
        return deepCopy(rows.get(row));
    }


    /**
     * Fetches all values from a given column
     * 
     * @param column
     *            The column whose values you want
     * @return An array of all values from that column
     */
    public Object[] column(int column) {

        ArrayList<Object> newColumn = new ArrayList<Object>();

        for (Object[] currRow : this) {
            newColumn.add(currRow[column]);
        }

        return newColumn.toArray();
    }


    /**
     * Fetches all values from a given column
     * 
     * @param columnName
     *            The column whose values you want
     * @return An array of all values from that column
     */
    public Object[] column(String columnName) {
        return column(columnNames.get(columnName));
    }


    /**
     * A query-like method. Used to retrieve all rows where the given columns
     * equal the given values. Works similar to this 'query':
     * 
     * FETCH ENTRIES WHERE
     * - column1.equals(value1) &&
     * - column2.equals(value2)
     * - etc...
     * 
     * All overloads of this method rely on this method.
     * 
     * @param columns
     *            The columns you want to check
     * @param values
     *            The values the columns have to equal
     * @return All rows that meet the set conditions.
     */
    public Table entriesWhere(int[] columns, Object[] values) {

        if (columns.length != values.length) {
            throw new IllegalArgumentException("Argument lengths to not match");
        }

        Table entriesWhere = new Table(columnNames.keySet().toArray(
            new String[0]));
        boolean meetsConditions;
        Object[] row;

        // Ensures all columnIndices passed are valid
        for (int i : columns) {
            if (i >= columnCount() || i < 0) {
                throw new IndexOutOfBoundsException(
                    "One or more column indices passed are out of bounds for this table");
            }
        }

        // Iterates through rows
        for (int i = 0; i < rowCount(); i++) {
            row = row(i); // Ensures deep copies will be added to new table

            // Assumes row doesn't meet conditions until proven otherwise
            meetsConditions = false;

            // Iterates through specified columns of row and checks conditions
            for (int col = 0; col <= columns.length; col++) {

                // if it has iterated through all the columns and the row hasn't
                // been rejected, sets meetConditions to true and breaks the
                // loop
                if (col == columns.length) {
                    meetsConditions = true;
                    break;
                }
                // Checks the current column. If the condition is met, continues
                // to next column
                if (row[columns[col]] == null && values[col] == null) {
                    continue;
                }
                if (row[columns[col]].equals(values[col])) {
                    continue;
                }
                // If neither condition is met, breaks loop
                break;
            }

            // Result of inner loops decide if row gets returned
            if (meetsConditions) {
                entriesWhere.add(row);
            }
        }

        return entriesWhere;

    }


    /**
     * A query-like method. Used to retrieve all rows where the given columns
     * equal the given values. Works similar to this 'query':
     * 
     * FETCH ENTRIES WHERE
     * - column1.equals(value1) &&
     * - column2.equals(value2)
     * - etc...
     * 
     * @param colNames
     *            The columns you want to check
     * @param values
     *            The values the columns have to equal
     * @return All rows that meet the set conditions.
     */
    public Table entriesWhere(String[] colNames, Object[] values) {
        int[] colIndices = new int[colNames.length];

        // Creates colIndicies array with colNames array to pass to
        // entriesWhere(int[], Object[])
        for (int i = 0; i < colNames.length; i++) {
            colIndices[i] = columnNames.get(colNames[i]);
        }

        return entriesWhere(colIndices, values);
    }


    /**
     * A query-like method. Used to retrieve all rows where the given column
     * equals the given value. Works similar to this 'query':
     * 
     * FETCH ENTRIES WHERE column.equals(value)
     * 
     * @param column
     *            The column you want to check
     * @param value
     *            The value the column has to equal
     * @return All rows that meet the set condition.
     */
    public Table entriesWhere(int column, Object value) {
        return entriesWhere(new int[] { column }, new Object[] { value });
    }


    /**
     * A query-like method. Used to retrieve all entries where the given column
     * equals the given value. Works similar to this query:
     * 
     * FETCH ENTRIES WHERE column.equals(value)
     * 
     * @param columnName
     *            The name of the column you want to check
     * @param value
     *            The value the column has to equal
     * @return All entries that meet the set condition.
     * @throws NoSuchElementException
     *             If the columnName doesn't exist in the table
     */
    public Table entriesWhere(String columnName, Object value) {
        return entriesWhere(columnNames.get(columnName), value);
    }


    /**
     * Fetches the value at the given row and column
     * 
     * @param row
     *            The row where the data you want is
     * @param column
     *            The column where the data you want is
     * @return the value at the given row and column
     */
    public Object valueAt(int row, int column) {
        return rows.get(row)[column];
    }


    /**
     * Fetches the value at the given row and column
     * 
     * @param row
     *            The row where the data you want is
     * @param columnName
     *            The column where the data you want is
     * @return the value at the given row and column
     */
    public Object valueAt(int row, String columnName) {
        return valueAt(row, columnNames.get(columnName));
    }


    /**
     * Removes the given row
     * 
     * @param row
     *            The row you want to remove
     * @return The removed row
     */
    public Object[] remove(int row) {
        return rows.remove(row);
    }


    /**
     * Removes first row that equals the passed Object array
     * 
     * @param row
     *            The row to be removed
     */
    public void remove(Object[] row) {
        for (int i = 0; i < rows.size(); i++) {
            if (Arrays.equals(row, rows.get(i))) {
                rows.remove(i);
                return;
            }
        }
        throw new NoSuchElementException("Row not found in table");
    }


    /**
     * This method ensures the following conditions are met before adding an
     * entry to the table:
     * 
     * - row's # of columns == table's number of columns
     * 
     * Additionally, creates a deepCopy of the passed row to add to the table to
     * avoid unintended changes.
     * 
     * @param row
     *            The row you want to add
     * @throws IllegalArgumentException
     *             If the row passed doesn't meet the requirements above
     */
    public void add(Object[] row) {
        if (row == null) {
            throw new IllegalArgumentException("Row cannot be null");
        }
        if (row.length != columnCount()) {
            throw new IllegalArgumentException(
                "Row columnCount and table columnCount differ. Row: "
                    + row.length + " columns, Table: " + columnCount());
        }
        rows.add(deepCopy(row));
    }


    /**
     * The number of columns in the table.
     * 
     * @return The number of columns in the table
     */
    public int columnCount() {
        return columnNames.size();
    }


    /**
     * The number of rows in the table.
     * 
     * @return The number of rows in the table
     */
    public int rowCount() {
        return rows.size();
    }


    /**
     * The name of each column in the table
     * 
     * @return The name of each column in the table
     */
    public String[] columnNames() {
        return columnNames.keySet().toArray(new String[0]);
    }


    /**
     * Indicates whether table contains the given row or not
     * 
     * @param row
     *            The row you're looking for
     * @return Boolean indicating whether table contains the row or not
     */
    public boolean contains(Object[] row) {
        for (Object[] tableRow : rows) {
            if (Arrays.equals(row, tableRow)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns an iterator for this table
     * 
     * @return The iterator for this table
     */
    public Iterator<Object[]> iterator() {
        return rows.iterator();
    }


    /**
     * Creates a deep copy of the passed object array
     * 
     * @param obj
     *            The obj arr you want to copy
     * @return A deep copy of the obj arr
     */
    private Object[] deepCopy(Object[] obj) {

        Object[] rowCopy = new Object[obj.length];
        Object[] ogRow = obj;

        for (int i = 0; i < columnCount(); i++) {
            rowCopy[i] = ogRow[i];
        }

        return rowCopy;
    }

}
