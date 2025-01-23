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
 */
class TableTest {

    private Table table;
    private Table table1;

    private String[] validColumnNames;
    private Comparator<Object[]> comparator;

    @BeforeEach
    void setUp() {
        validColumnNames = new String[] { "ID", "Name" };
        comparator = Comparator.comparingInt(o -> (int)o[0]);

        table1 = new Table(validColumnNames, comparator);
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
        tableWithComparator.add(new Object[] { 3, "Charlie" });
        tableWithComparator.add(new Object[] { 1, "Alice" });
        tableWithComparator.add(new Object[] { 2, "Bob" });

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
        unsortedTable.add(new Object[] { 3, "Charlie" });
        unsortedTable.add(new Object[] { 1, "Alice" });
        unsortedTable.add(new Object[] { 2, "Bob" });

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
     * Tests that row returns a deep copy array. (thought the objects in the
     * array don't necessarily have to be deep copies)
     */
    @Test
    void testRowDeepCopy() {
        Object[] copyRow = table.row(0);
        copyRow[0] = 4;
        // Checks that the table row remains unchanged even though the copy row
        // is changed.
        Object[] tableRow = table.row(0);
        assertEquals(tableRow[0], 1);

    }


    /**
     * Tests column()
     * 
     * - Case where there is a single column
     * - Case where a middle column is called
     * - Case where the last column is called
     * - Case where index above bounds
     * - Case where index below bounds
     * - Case where column name doesn't exist
     * - Case where column name is null
     * - Case where table is empty
     * - Case where changes are made to retrieved column (Ensure this doesn't
     * affect the table)
     */
    @Test
    void testColumn() {
        Object[] expectedIDs = new Object[] { 1, 2, 3 };
        Object[] expectedNames = new Object[] { "Ngoc", "Ethan", "Doug" };
        // Called by index case
        assertArrayEquals(table.column(0), expectedIDs);
        // Called by name case
        assertArrayEquals(table.column("Name"), expectedNames);
        // Called by invalid name case
        assertThrows(NoSuchElementException.class, () -> table.column(
            "Sucks to suck"));
    }


    /**
     * Tests entriesWhere()
     * 
     * - Case where single condition is used to retrive rows
     * - Case where multiple conditions are used to retrieve rows
     * - Case where table is empty
     * - Case where column index is above bounds
     * - Case where column index is below bounds
     * - Case where column name doesn't exist
     * - Case where column name is null
     * - Case where no row satify the condition
     * - Case where rows satisfy some of the criteria
     * - Case where primitive type is compared to non-primitive
     * - Case where column array is empty
     * - Case where value array is empty
     * - Case where passed arrays are of unequal size
     * - Case where returned table is modified (Ensure this doesn't affect
     * original table)
     */
    @Test
    void testEntriesWhere() {
        // Column specified by index case
        Table results = table.entriesWhere(1, "Ngoc");
        assertEquals(1, results.rowCount());
        assertEquals(results.valueAt(0, 1), "Ngoc");

        // Column specified by name case
        results = table.entriesWhere("Name", "Ethan");
        assertEquals(1, results.rowCount());
        assertEquals(results.valueAt(0, 1), "Ethan");

        // Invalid column name is passed
        assertThrows(NoSuchElementException.class, () -> table.entriesWhere(
            "InvalidColumn", "Ngoc"));
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
