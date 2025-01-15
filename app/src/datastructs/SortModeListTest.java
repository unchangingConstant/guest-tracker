package datastructs;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests key SortModeList methods. This class encapsulates an ArrayList. Any
 * methods written that simply call the ArrayList version with no major changes
 * will not be tested.
 * 
 * @author Ethan Begley
 * @version 1/14/2025
 */
class SortModeListTest {

    // List to be used in all test
    private SortModeList<Integer> list;

    /**
     * Sets up a SortModeList to use for tests
     */
    @BeforeEach
    void setUp() {
        list = new SortModeList<Integer>(Comparator.naturalOrder());
    }


    /**
     * Tests that add() adds elements in order
     * 
     * - Case where elements are added out of order
     */
    @Test
    void testAddInOrder() {
        list.add(3);
        list.add(1);
        list.add(2);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }


    /**
     * Tests setSortMode() method. The main concern is that a call of
     * setSortMode() will sort the list according to the new comparator
     * immediately.
     * 
     * - Case where new comparator sorts in reverse order
     * - Case where add() is called after sortMode is changed
     */
    @Test
    void testSetSortMode() {
        list.add(3);
        list.add(1);
        list.add(2);

        // Case where new comparator sorts in reverse order
        list.setSortMode(Comparator.reverseOrder());

        assertEquals(3, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(1, list.get(2));

        // Case where add() is called after sortMode is changed
        list.add(4);
        assertEquals(4, list.get(0));
    }


    /**
     * Tests that toArray() properly converts the list to an array in the same
     * order as the list.
     * 
     * - Case where toArray() is called
     */
    @Test
    void testToArray() {
        list.add(4);
        list.add(2);

        Object[] array = list.toArray();
        assertArrayEquals(new Object[] { 2, 4 }, array);
    }
}
