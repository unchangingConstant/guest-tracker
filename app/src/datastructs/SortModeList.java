package datastructs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Ordered list whose sort order can be modified
 * 
 * TODO FINISH!!!!
 * 
 * @author Ethan Begley
 * 
 * @version 12/29/2024
 */
public class SortModeList<T> implements Iterable<T> {

    // The list where everything will be stored
    private ArrayList<T> list;
    // Comparator the list is sorted by
    private Comparator<T> comparator;

    /**
     * Creates a new sorted list sorted with the passed comparator and starting
     * array.
     */
    public SortModeList(Comparator<T> comparator, T[] startingArray) {
        this.list = new ArrayList<T>();
        this.comparator = comparator;
        if (startingArray != null) {
            fillWith(startingArray);
        }
    }


    /**
     * Creates a new sorted list sorted with the passed comparator.
     */
    public SortModeList(Comparator<T> comparator) {
        this(comparator, null);
    }


    /**
     * Creates a new sorted list with no comparator. Works nearly identical to
     * ArrayList.
     */
    public SortModeList() {
        this(null, null);
    }


    /**
     * Adds an item to the list in sorted order
     * 
     * @param item
     *            The item you want to add in order
     */
    public void add(T item) {
        if (comparator == null) {
            list.add(item);
            return;
        }

        for (int i = 0; i < size(); i++) {
            if (comparator.compare(item, list.get(i)) < 0) {
                list.add(i, item);
                return;
            }
        }
        list.add(item);
    }


    /**
     * Gets given index from the list
     * 
     * @param i
     *            Index of desired item
     * @return Item where index is
     */
    public T get(int i) {
        return list.get(i);
    }


    /**
     * Removes given index
     * 
     * @param i
     *            Index of item you want to remove
     */
    public T remove(int i) {
        return list.remove(i);
    }


    /**
     * Removes given item
     * 
     * @param item
     *            you want to remove from the list
     */
    public void remove(T item) {
        list.remove(item);
    }


    /**
     * Returns size of list
     * 
     * @return size of list
     */
    public int size() {
        return list.size();
    }


    /**
     * Clears list of all items
     */
    public void clear() {
        list.clear();
    }


    /**
     * Sets the lists order to the new comparator and re-sorts entire list
     * according to new sortMode.
     * 
     * @param sortMode
     *            The new comparator you want the list to be ordered by.
     */
    public void setSortMode(Comparator<T> sortMode) {
        this.comparator = sortMode;

        @SuppressWarnings("unchecked")
        T[] oldList = (T[])list.toArray();
        clear();
        fillWith(oldList);

    }


    /**
     * Returns an array with all object from list.
     * 
     * @return This sort list in array form.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        return (T[])list.toArray();
    }


    /**
     * Returns an iterator for the list
     * 
     * @return The iterator for this list
     */
    public Iterator<T> iterator() {
        return list.iterator();
    }


    /**
     * Indicates whether list contains the given item or not
     * 
     * @param item
     *            The item you're looking for
     * @return Boolean indicating whether list contains item or not
     */
    public boolean contains(T item) {
        return list.contains(item);
    }


    /**
     * Used to instantiate a SortedList with a given array.
     * 
     * @param arr
     *            The array you'd like to fill the SortModeList with
     */
    private void fillWith(T[] arr) {
        // For every item in the passed array, adds to current SortList in
        // order.
        for (T item : arr) {
            add(item);
        }
    }

}
