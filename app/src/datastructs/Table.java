package datastructs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Make sure all Objects stored in a table are IMMUTABLE
 * Table is ordered by first field by default
 * 
 * TODO check for dumb stuff
 */
public class Table implements Iterable<Object[]> {

    /**
     * Name of each column. The order of the columnNames should correspond to
     * that column's index.
     * 
     * Example: if field "name" can be called with field(0), then columnNames[0]
     * should equal "name"
     */
    protected String[] columnNames;
    // Stores all rows
    protected SortModeList<Object[]> rows;

    /**
     * Initializes table sorted with given comparator and labelled with given
     * columnNames.
     * 
     * @param columnNames
     *            The name of each column in order.
     */
    public Table(String[] columnNames, Comparator<Object[]> comparator) {
        this.columnNames = columnNames;
        this.rows = new SortModeList<Object[]>(comparator);
    }


    /**
     * Initializes table with passed columnNames
     * 
     * @param columnNames
     *            The name of each column in order.
     */
    public Table(String[] columnNames) {
        this.columnNames = columnNames;
        this.rows = new SortModeList<Object[]>();
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

        Object[] rowCopy = new Object[columnCount()];
        Object[] ogRow = rows.get(row);
        for (int i = 0; i < columnCount(); i++) {
            rowCopy[i] = ogRow[i];
        }
        return rowCopy;
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

        verifyColIndex(column);

        Table entriesWhere = new Table(columnNames);

        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i)[column].equals(value)) {
                entriesWhere.add(row(i));
            }
        }

        return entriesWhere;
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
        for (int i = 0; i < columnCount(); i++) {
            if (columnNames[i].equals(columnName)) {
                return entriesWhere(i, value);
            }
        }
        throw new NoSuchElementException("Column name doesn't exist");
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
     * @throws NoSuchElementException
     *             If the columnName doesn't exist in the table
     */
    public Object valueAt(int row, String columnName) {
        for (int i = 0; i < columnCount(); i++) {
            if (columnNames[i].equals(columnName)) {
                return valueAt(row, i);
            }
        }

        throw new NoSuchElementException("Column name doesn't exist");
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
        throw new NoSuchElementException("Row not found");
    }


    /**
     * This method ensures the following conditions are met before adding an
     * entry to the table:
     * 
     * - row's # of columns == table's number of columns
     * 
     * @param row
     *            The row you want to add
     * @throws IllegalArgumentException
     *             If the row passed doesn't meet the requirements above
     */
    public void add(Object[] row) {
        if (row.length != columnCount()) {
            throw new IllegalArgumentException(
                "Row columnCount and table columnCount differ. Row: "
                    + row.length + " columns, Table: " + columnCount());
        }
        rows.add(row);

    }


    /**
     * The number of columns in the table.
     * 
     * @return The number of columns in the table
     */
    public int columnCount() {
        return columnNames.length;
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
        return columnNames;
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
     * Checks that a given integer is above 0 and below the table's column count
     * 
     * @param colIndex
     *            Column index you want to check
     */
    private void verifyColIndex(int colIndex) {
        if (colIndex < 0 || colIndex >= columnCount()) {
            throw new IndexOutOfBoundsException(
                "Column index is out of bounds");
        }
    }

}
