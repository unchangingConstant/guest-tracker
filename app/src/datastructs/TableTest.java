package datastructs;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Comparator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests all functions for the table class
 * 
 * @author Ethan Begley
 * @version 1/31/2025
 */
class TableTest {

    private Table table;
    private Table table1;
    private Table table2;

    private String[] validColumnNames;
    private Comparator<Object[]> comparator;

    @BeforeEach
    void setUp() {
        validColumnNames = new String[] { "ID", "Name" };
        comparator = Comparator.comparingInt(o -> (int)o[0]);

        table1 = new Table(validColumnNames, comparator);

        table2 = new Table(validColumnNames, comparator);
        table2.add(new Object[] { 1, "Ethan" });
        table2.add(new Object[] { 2, "Ngoc" });
        table2.add(new Object[] { 3, "Doug" });
    }


    /**
     * FOLLOWING METHODS TEST CONSTRUCTOR
     */

    /**
     * - Case where comparator is passed (Ensure table is sorted correctly even
     * if objects are added out of order)
     */
    @Test
    void testConstructorWithComparator() {
        Table tableWithComparator = new Table(validColumnNames, comparator);
        tableWithComparator.add(new Object[] { 3, "Ngoc" });
        tableWithComparator.add(new Object[] { 1, "Doug" });
        tableWithComparator.add(new Object[] { 2, "Ethan" });

        assertEquals(1, tableWithComparator.row(0)[0]);
        assertEquals(2, tableWithComparator.row(1)[0]);
        assertEquals(3, tableWithComparator.row(2)[0]);
    }


    /**
     * - Case where String array is empty (Throw IllegalArgumentException)
     */
    @Test
    void testConstructorWithEmptyColumnNames() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Table(new String[] {});
        });
    }


    /**
     * - Case where String array is null (Throw NullPointerException)
     */
    @Test
    void testConstructorWithNullColumnNames() {
        assertThrows(NullPointerException.class, () -> {
            new Table(null);
        });
    }


    /**
     * - Case where comparator is null (Initialize normal, unsorted table)
     */
    @Test
    void testConstructorWithNullComparator() {
        Table unsortedTable = new Table(validColumnNames, null);
        unsortedTable.add(new Object[] { 3, "Ngoc" });
        unsortedTable.add(new Object[] { 1, "Doug" });
        unsortedTable.add(new Object[] { 2, "Ethan" });

        assertEquals(3, unsortedTable.row(0)[0]); // No sorting
        assertEquals(1, unsortedTable.row(1)[0]);
        assertEquals(2, unsortedTable.row(2)[0]);
    }


    /**
     * - Case where there are duplicate column names (Throw
     * IllegalArgumentException)
     */
    @Test
    void testConstructorWithDuplicateColumnNames() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Table(new String[] { "ID", "Name", "ID" });
        });
    }


    /**
     * - Case where an empty column name is used (Initialize with empty column
     * name normally)
     */
    @Test
    void testConstructorWithEmptyColumnName() {
        Table emptyColumnNameTable = new Table(new String[] { "", "Name" });
        assertEquals("", emptyColumnNameTable.columnNames()[0]);
        assertEquals("Name", emptyColumnNameTable.columnNames()[1]);
    }


    /**
     * - Case where String array used to initialize table is changed afterward
     * (Ensure changes to this array doesn't reflect in table)
     */
    @Test
    void testConstructorIndependenceFromInputArray() {
        String[] columnNames = new String[] { "ID", "Name" };
        Table independentTable = new Table(columnNames);
        columnNames[0] = "Modified"; // Change the original array

        assertEquals("ID", independentTable.columnNames()[0]); // Ensure table
                                                               // is unaffected
    }


    /**
     * Tests that adding to an empty table works normally
     */
    @Test
    void testAddEmptyTable() {
        table1.add(new Object[] { 1, "Ethan" });
        assertArrayEquals(table1.row(0), new Object[] { 1, "Ethan" });
    }


    /**
     * Tests that adding multiple rows in succession works normally.
     */
    @Test
    void testAddMultiple() {
        table1.add(new Object[] { 1, "Ethan" });
        table1.add(new Object[] { 2, "Ngoc" });
        table1.add(new Object[] { 3, "Doug" });
        assertArrayEquals(table1.row(0), new Object[] { 1, "Ethan" });
        assertArrayEquals(table1.row(1), new Object[] { 2, "Ngoc" });
        assertArrayEquals(table1.row(2), new Object[] { 3, "Doug" });
    }


    /**
     * Tests that add() adds the rows in sorted order.
     */
    @Test
    void testAddSorted() {
        table1.add(new Object[] { 3, "Doug" });
        table1.add(new Object[] { 1, "Ethan" });
        table1.add(new Object[] { 2, "Ngoc" });
        assertArrayEquals(table1.row(0), new Object[] { 1, "Ethan" });
        assertArrayEquals(table1.row(1), new Object[] { 2, "Ngoc" });
        assertArrayEquals(table1.row(2), new Object[] { 3, "Doug" });
    }


    /**
     * Tests that IllegalArgumentException is thrown when row with invalid
     * column count is passed.
     */
    @Test
    void testAddInvalidColCount() {
        assertThrows(IllegalArgumentException.class, () -> table1.add(
            new Object[0]));
        assertThrows(IllegalArgumentException.class, () -> table1.add(
            new Object[3]));
    }


    /**
     * Tests that IllegalArgumentException is thrown if null row is added
     */
    @Test
    void testAddNull() {
        assertThrows(IllegalArgumentException.class, () -> table1.add(null));
    }


    /**
     * Tests that rows with null columns are added normally
     */
    @Test
    void testAddNullFields() {
        table1.add(new Object[] { 1, null });
        assertArrayEquals(table1.row(0), new Object[] { 1, null });
    }


    /**
     * Tests that rows with duplicate columns are added normally
     */
    @Test
    void testAddDuplicateFields() {
        table1.add(new Object[] { 5, 5 });
        assertArrayEquals(table1.row(0), new Object[] { 5, 5 });
    }


    /**
     * Tests that when a row is added to the table, any changes made to the
     * passed object doesn't impact the entry in the table
     */
    @Test
    void testAddDeepCopy() {
        Object[] row = new Object[] { 1, "Ethan" };
        table1.add(row);
        row[0] = 99;
        assertArrayEquals(new Object[] { 1, "Ethan" }, table1.row(0));
    }


    /**
     * Tests that row returns a deep copy array. (thought the objects in the
     * array don't necessarily have to be deep copies). This test also makes
     * sure that cases index = 0 and index = Table.columnCount() - 1 works
     * normally
     */
    @Test
    void testRowDeepCopy() {
        Object[] copyRow = table2.row(0);
        copyRow[0] = 4;
        // Checks that the table row remains unchanged even though the copy row
        // is changed.
        Object[] tableRow = table2.row(0);
        assertEquals(tableRow[0], 1);

        tableRow = table2.row(2);
        assertArrayEquals(tableRow, new Object[] { 3, "Doug" });

    }


    /**
     * Makes sure that row() throws IndexOutOfBoundsException if requested index
     * is, out of bounds.
     */
    @Test
    void testRowIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> table2.row(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> table2.row(3));
    }


    /**
     * Tests that row() method throws IndexOutOfBounds if used on empty table
     */
    @Test
    void testRowEmptyTable() {
        assertThrows(IndexOutOfBoundsException.class, () -> table1.row(0));
    }


    /**
     * Tests that column(int) returns the specified column of values
     */
    @Test
    void testColumnValidIndex() {
        assertArrayEquals(table2.column(0), new Object[] { 1, 2, 3 });
        assertArrayEquals(table2.column(1), new Object[] { "Ethan", "Ngoc",
            "Doug" });
    }


    /**
     * Tests that column(String) returns the specified column of values
     */
    @Test
    void testColumnValidName() {
        assertArrayEquals(table2.column("ID"), new Object[] { 1, 2, 3 });
        assertArrayEquals(table2.column("Name"), new Object[] { "Ethan", "Ngoc",
            "Doug" });
    }


    /**
     * Any index passed that is less than 0 or greater than Table.columnCount()
     * - 1 should return IndexOutOfBoundsException.
     */
    @Test
    void testColumnInvalidIndices() {
        assertThrows(IndexOutOfBoundsException.class, () -> table2.column(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> table2.column(2));
    }


    /**
     * If a non-existent column name is passed, throws Exception
     */
    @Test
    void testColumnInvalidName() {
        assertThrows(Exception.class, () -> table2.column("Dees"));
    }


    /**
     * If table is empty, column() throws Exception
     */
    @Test
    void testColumnEmptyTable() {
        assertThrows(Exception.class, () -> table.column(0));
    }


    /**
     * If column(null) is called, throws Exception
     */
    @Test
    void testColumnNullName() {
        assertThrows(Exception.class, () -> table2.column(null));
    }


    /**
     * entriesWhere is the most error-prone method in Table, so there will be
     * many, many tests. The following numbers will be used to indicate which
     * overload of the method will be tested in any particular test:
     * 
     * 0 - for single condition methods
     * 1 - for multiple condition methods
     * 2 - for single string condition methods
     * 3 - for multiple string condition methods
     * 4 - for single index condition methods
     * 5 - for multiple index condition methods
     */

    /**
     * Tests entriesWhere in normal case and also tests for immutability
     */
    @Test
    void testEntriesWhere() {

    }


    /**
     * Tests that empty table is returned if table is empty (0, 1)
     */
    @Test
    void testEntriesWhereEmptyTable() {
        assertEquals(table1.entriesWhere(0, 1).rowCount(), 0);
        assertEquals(table1.entriesWhere("ID", 1).rowCount(), 0);

        assertEquals(table1.entriesWhere(new int[] { 0, 1 }, new Object[] { 0,
            "Ethan" }).rowCount(), 0);
        assertEquals(table1.entriesWhere(validColumnNames, new Object[] { 0,
            "Ethan" }).rowCount(), 0);
    }


    /**
     * Tests that empty table is returned if no matches are found (0, 1)
     */
    @Test
    void testEntriesWhereNoMatches() {
        assertEquals(table2.entriesWhere(0, 4).rowCount(), 0);
        assertEquals(table2.entriesWhere("ID", 4).rowCount(), 0);

        assertEquals(table2.entriesWhere(new int[] { 0, 1 }, new Object[] { 4,
            "Emison" }).rowCount(), 0);
        assertEquals(table2.entriesWhere(validColumnNames, new Object[] { 4,
            "Emison" }).rowCount(), 0);
    }


    /**
     * Tests that Exception is throw if column parameter has null values passed
     * (2, 3)
     */
    @Test
    void testEntriesWhereNull() {
        assertThrows(Exception.class, () -> table2.entriesWhere(null, 0));
        assertThrows(Exception.class, () -> table2.entriesWhere(new String[] {
            null, "Name" }, new Object[] { 0, "Ethan" }));

    }


    /**
     * Tests that method returns rows where null equality is assessed (0, 1)
     */
    @Test
    void testEntriesWhereNullCondition() {
        Table table3 = new Table(validColumnNames);
        table3.add(new Object[] { 0, null });

        assertArrayEquals(table3.entriesWhere(1, null).row(0), new Object[] { 0,
            null });
        assertArrayEquals(table3.entriesWhere("Name", null).row(0),
            new Object[] { 0, null });

        assertArrayEquals(table3.entriesWhere(new int[] { 0, 1 }, new Object[] {
            0, null }).row(0), new Object[] { 0, null });
        assertArrayEquals(table3.entriesWhere(validColumnNames, new Object[] {
            0, null }).row(0), new Object[] { 0, null });

    }


    /**
     * Tests that method throws exception if array lengths are mismatched. Tests
     * case where column array is shorter, and case where values array is
     * shorter. (1)
     */
    @Test
    void testEntriesWhereArrayMismatch() {
        assertThrows(Exception.class, () -> table2.entriesWhere(new int[] { 0,
            1 }, new Object[] { 0 }));
        assertThrows(Exception.class, () -> table2.entriesWhere(
            validColumnNames, new Object[] { 0 }));

        assertThrows(Exception.class, () -> table2.entriesWhere(new int[] { 0 },
            new Object[] { 0, "Ethan" }));
        assertThrows(Exception.class, () -> table2.entriesWhere(new String[] {
            "ID" }, new Object[] { 0, "Ethan" }));
    }


    /**
     * Tests that method returns all entries if two empty arrays are passed.
     * (Makes sense, if there are no conditions, all rows should return) (1)
     */
    @Test
    void testEntriesWhereEmptyArray() {
        assertEquals(table2.entriesWhere(new int[0], new String[0]).rowCount(),
            3);
        assertEquals(table2.entriesWhere(new String[0], new String[0])
            .rowCount(), 3);
    }


    /**
     * Tests that method throws exception of out of bounds column indices are
     * passed. Tests cases where indices are below 0 and above table's last
     * column index (4, 5)
     */
    @Test
    void testEntriesWhereColumnIndicesOutOfBounds() {
        assertThrows(Exception.class, () -> table2.entriesWhere(3, "what"));
        assertThrows(Exception.class, () -> table2.entriesWhere(-1, "what"));
        assertThrows(Exception.class, () -> table2.entriesWhere(new int[] { 0,
            3 }, new Object[] { 0, "what" }));
        assertThrows(Exception.class, () -> table2.entriesWhere(new int[] { -1,
            0 }, new Object[] { 0, "what" }));
    }


    /**
     * Tests that a row is not returned if it only partially meets conditions
     * (1)
     */
    @Test
    void testEntriesWherePartiallyMeets() {
        assertEquals(table2.entriesWhere(validColumnNames, new Object[] { 0,
            "Ngoc" }).rowCount(), 0);
        assertEquals(table2.entriesWhere(new int[] { 0, 1 }, new Object[] { 0,
            "Ngoc" }).rowCount(), 0);
    }


    /**
     * Tests that Exception is throw if invalid column name is passed (2, 3)
     */
    @Test
    void testEntriesWhereInvalidColName() {
        assertThrows(Exception.class, () -> table2.entriesWhere("Dees", 0));
        assertThrows(Exception.class, () -> table2.entriesWhere(new String[] {
            "ID", "Dees" }, new Object[] { 0, 0 }));
    }


    /**
     * Tests that exception is throw if null column names are passed (2, 3)
     */
    @Test
    void testEntriesWhereNullColName() {
        assertThrows(Exception.class, () -> table2.entriesWhere(null, 0));
        assertThrows(Exception.class, () -> table2.entriesWhere(new String[] {
            "ID", null }, new Object[] { 0, 0 }));
    }


    /**
     * Tests that order in new table is preserved (0, 1)
     */
    @Test
    void testEntriesWherePreservesOrder() {
        Table table3 = new Table(validColumnNames);
        table3.add(new Object[] { 1, "Ethan" });
        table3.add(new Object[] { 2, "Ngoc" });
        table3.add(new Object[] { 3, "Ethan" });

        Table newTable = table3.entriesWhere(1, "Ethan");

        assertArrayEquals(newTable.row(0), new Object[] { 1, "Ethan" });
        assertArrayEquals(newTable.row(1), new Object[] { 3, "Ethan" });
    }


    /**
     * Tests that Object arrays can be used on the SortModeList contains()
     * method.
     * 
     * - Case where Object array is used on contains() method
     */
    @Test
    void testContains() {
        assertTrue(table.contains(new Object[] { 1, "Ngoc", 25 }));
    }


    /**
     * Tests valueAt()
     * 
     * - Case where column is specified by index
     * - Case where column is specified by name
     * - Case where column name is invalid
     */
    @Test
    void testValueAt() {
        // Specified by index case
        assertEquals(30, table.valueAt(1, 2));
        // Specified by name case
        assertEquals("Doug", table.valueAt(2, "Name"));
        // Invalid columnName case
        assertThrows(NoSuchElementException.class, () -> table.valueAt(1,
            "InvalidColumn"));
    }


    /**
     * Tests remove()
     * 
     * - Case where row is specifed by index
     * - Case where row is specified by Object array
     * - Case where row is not found
     */
    @Test
    void testRemove() {
        // Row specified by index case
        Object[] removedRow = table.remove(1);
        assertArrayEquals(new Object[] { 2, "Ethan", 30 }, removedRow);
        // Row specified by Object array case
        table.remove(new Object[] { 1, "Ngoc", 25 });
        assertEquals(0, table.entriesWhere("Name", "Ngoc").rowCount());
        // Row is not found case
        assertThrows(NoSuchElementException.class, () -> table.remove(
            new Object[] { 4, "Himothy", 86 }));

    }
}
