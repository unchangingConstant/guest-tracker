package datastructs;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Comparator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableTest {

    private Table table;

    @BeforeEach
    void setUp() {
        // Setting up a table with column names and default comparator
        Comparator<Object[]> comparator = Comparator.comparingInt(
            row -> (int)row[0]);
        table = new Table(new String[] { "ID", "Name", "Age" }, comparator);

        table.add(new Object[] { 1, "Ngoc", 25 });
        table.add(new Object[] { 3, "Doug", 35 });
        table.add(new Object[] { 2, "Ethan", 30 });
    }


    /**
     * Tests that entries are sorted according to comparator passed on
     * instantiation.
     * 
     * - Case where comparator is passed into constructor.
     */
    @Test
    void testComparatorConstructor() {
        assertEquals(table.row(0)[0], 1);
        assertEquals(table.row(1)[0], 2);
        assertEquals(table.row(2)[0], 3);
    }


    /**
     * Tests that row() returns a proper deep copy of the array (Though the
     * elements in the array itself are shallow copies). We will call row(),
     * change on of the elements in the array, and check that nothing has
     * changed in the table.
     * 
     * - Case where test row is index 0
     */
    @Test
    void testRowDeepCopy() {
        // Case where index = 0
        Object[] copyRow = table.row(0);
        copyRow[0] = 4;
        // Checks that the table row remains unchanged even though the copy row
        // is changed.
        Object[] tableRow = table.row(0);
        assertEquals(tableRow[0], 1);

    }


    /**
     * Tests that entriesWhere fetch the correct entries for given column and
     * value.
     * 
     * - Case where method is called with column index
     * - Case where method is called with column name
     * - Case where invalid column name is passed
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


    /**
     * Tests add()
     * 
     * - Case where row doesn't meet column count requirements
     * - Case where row meets columnCount requirements
     */
    @Test
    void testAddRowValid() {
        // Valid case
        table.add(new Object[] { 4, "Daisy", 999 });
        assertTrue(table.contains(new Object[] { 4, "Daisy", 999 }));
        // Invalid case
        assertThrows(IllegalArgumentException.class, () -> table.add(
            new Object[] { "Invalid", "Row" }));
    }
}
