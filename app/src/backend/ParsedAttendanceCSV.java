package backend;

import java.io.File;

/**
 * Takes a CSV file in constructor, provides methods to easily navigate its
 * entries.
 * 
 * @author Ethan Begley
 * @version 12/9/2024
 */
public class ParsedAttendanceCSV {

    /**
     * Stores all entries in CSV file. Each String array stores all the data
     * from one entry
     */
    private String[][] entries;
    private int currentEntry;

    /**
     * Creates ParsedCSVFile from a CSV file
     * 
     * @param file
     *            The CSV file you want to store
     */
    public ParsedAttendanceCSV(File file) {
        entries = parseCSVFile(file);
        currentEntry = 0;
    }


    /**
     * Returns the next entry in the CSV file. Returns null if no entries are
     * left.
     * 
     * @return The next entry in the CSV file
     */
    public String[] getNextEntry() {
        if (entries.length == currentEntry) {
            return null;
        }
        currentEntry++;
        return entries[currentEntry - 1];
    }


    /**
     * Parses the passed CSV file into an array of String arrays. Each String
     * array represent all of an entry's data
     * 
     * @param file
     *            CSV file you want to parse
     * @return
     */
    private String[][] parseCSVFile(File file) {
        return null;
    }

}
