package backend;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests all VisitorTable methods
 * 
 * @author Ethan Begley
 * @version 1/31/2025
 */
public class VisitorTableTest {

    private Object[] visitor;
    private VisitorTable table1;

    @BeforeEach
    void setUp() {

        visitor = new Object[] { new String[] { "Ethan", "Connor", "Begley" },
            10000 };

        table1 = new VisitorTable();
    }


    /**
     * - Cases where visitor ID is 10001, 10000, 99999, 100000
     * - Case where added visitor's visitor ID already exists in table
     * - Case where visitor has first and last name but not middle
     * - Case where visitor has first, middle and last name
     * - Case where visitor has no first name
     * - Case where visitor has no last name
     * 
     * - Case where name array is length 2
     * - Case where name array is length 3
     * 
     */

    /**
     * Tests that an IllegalArgumentException is thrown if the visitor has a
     * visitor ID < 10001 or > 99999
     */
    @Test
    void testAddInvalidID() {

        assertThrows(Exception.class, () -> table1.add(visitor));
        visitor[1] = 100000;
        assertThrows(Exception.class, () -> table1.add(visitor));
        visitor[1] = 10001;
        assertEquals(1, table1.rowCount());
        visitor[1] = 99999;
        assertEquals(2, table1.rowCount());

    }

}
