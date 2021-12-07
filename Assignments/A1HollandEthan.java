/**
 * A1HollandEthan
 * 
 * COMP 2140 SECTION A01
 * INSTRUCTOR   Shahin Kamali 
 * ASSIGNMENT   Assignment 1
 * @author      Ethan Holland, 7865033
 * @version     Oct 5, 2021
 * 
 * PURPOSE: This program can iteratively, and recursively insertion sort an array of integers into increasing order, 
 *          and compare the speeds of the two solutions.
 *          There are also helper methods to fill an array of random integers, output the contents of an array, 
 *          and test whether an array is sorted in increasing order.
 */

import java.lang.Math;

public class A1HollandEthan {

    // Global Variables.
    private static final int ARRAY_SIZE = 10000;
    private static final int MAX_VALUE = 5000;
    // Used to indicate whether the recursive or iterative algorithm should be used.
    private static final boolean RECURSIVE = false;
    private static final boolean ITERATIVE = true;

    public static void main(String[] args){

        System.out.println("\nAssignment 1 Solution\n");
        // Test the recursive insertion sort and return the length of time that it took to finish.
        long recursiveTime = testInsertionSort(RECURSIVE);
        // Test the iterative insertion sort and return the length of time that it took to finish.
        long iterativeTime = testInsertionSort(ITERATIVE);
        // Print the comparison of the recursive and iterative solutions.
        printTimeResults(iterativeTime, recursiveTime);
        
        System.out.println("\nProcessing Ends."); // End program.
    } // end main

    private static void printTimeResults(long iterativeTime, long recursiveTime){
        /** 
         * PURPOSE: Takes long @params iterativeTime and recursiveTime
         *          to output to the console the length of time that the recursive and iterative 
         *          insertion sorting algorithms took to finish, then compares the two lengths of time.
         */

        System.out.println("\nTiming:");
        // Print the length of time that the recursive algorithm took to finish.
        System.out.println("\nTime to insertion sort (recursively): " + recursiveTime + " nanoseconds.");
        // Print the length of time that the iterative algorithm took to finish.
        System.out.println("\nTime to insertion sort (iteratively): " + iterativeTime + " nanoseconds.");
        

        // Compare the speed of the iterative and recursive algorithms and print the comparison.
        if (recursiveTime <= iterativeTime)
            System.out.println("\nThe recursive insertion sort was at least as fast as the iterative one!");
        else
            System.out.println("The recursive insertion sort was not as fast as the iterative one!");
    } // end printTimeResults

    private static long testInsertionSort(boolean iterative){
        /** 
         * PURPOSE: Creates randomized int[] array via fillArray() and executes either the iterative or the recursive insertion sorting algorithm,
         *          and returns the length of time that it took to finish. 
         *          Which algorithm is executed is dependant on boolean @param iterative,
         *          if iterative is true then the iterativeInsertionSort() is executed, otherwise recursiveInsertionSort() is executed.
         *          The contents of the randomized int[] array is printed before and after sorting,
         *          The array is tested for successful sorting using isSorted(), and the result is printed.
         */

        // Create a String indicating which algorithm is being executed, for printing purposes.
        String sortingSolution; 
        if (iterative)
            sortingSolution = "Iterative";
        else
            sortingSolution = "Recursive";
        // Record the starting and stopping clock times of the algorithm being executed.
        long start, stop;

        System.out.println("Testing " + sortingSolution + " Insertion Sort:\n"); 
        // Create and fill and new array with randomized integers.
        int[] array = new int[ARRAY_SIZE];
        fillArray(array, MAX_VALUE);
        // Print the new array contents.
        System.out.println("Array before sorting:\n" + arrayToString(array) + "\n");

        // Test the insertion sorting algorithm and record the start and stop clock times.
        start = System.nanoTime();
        if (iterative) 
            iterativeInsertionSort(array);
        else
            recursiveInsertionSort(array);
        stop = System.nanoTime();

        // Verify that the array is sorted in ascending order, and print the result.
        if (isSorted(array)) 
            System.out.println("Array is correctly sorted\n");
        else
            System.out.println("ERROR: Array is NOT correctly sorted\n");
        // Print the sorted array.
        System.out.println("Array after sorting:\n" + arrayToString(array) + "\n");

        return stop - start; // Output the total time that the algorithm took to compute.
    } // end testInsertionSort

    public static void fillArray(int[] array, int maxValue){
        // PURPOSE: Fills a @param int[] array with random values from 0 to @param int maxValue.
        
        for (int i = 0; i < array.length; i++)
            array[i] = (int)(Math.random() * maxValue);
    } // end fillArray

    public static String arrayToString(int[] array){
        /** 
         * PURPOSE: Returns a String of the first 10 values of @param int[] array, 
         *          followed by a "..." to indicate skipped elements in the middle, and then the last 10 values.
         *          If the array has 20 or less values then the String output is just the array's elements.
         */

        String output = ""; // Return variable.
        // Loop through the elements of the parameter array.
        for (int i = 0; i < array.length; i++){
            // Only add the first 10 items and the last 10 items of the array.
            if ( (i < 10) || (i >= array.length - 10))
                output += array[i] + " ";
            // if the element is not the first or last 10 elements,
            // we will need to add a " ... ", but only once after the first 10 elements have been added.
            else if (i == 10)
                output += " ... ";
        } // end for
        return output;
    } // end arrayToString

    public static boolean isSorted(int[] array){
        // PURPOSE: Tests and returns whether the values of a @param int[] array are in an ascending order.

        boolean arrayIsAscending = true;
        // Loop through the elements of the parameter array.
        for (int i = 1; i < array.length; i++){
            //if the element is less than the previous element, then the array is not in ascending order.
            if (array[i] < array[i-1])
                arrayIsAscending = false;
        } // end for
        return arrayIsAscending;
    } // end isSorted

    public static void siftUp( int[] array, int pos){
         // PURPOSE: Moves the element at the indicated position up the given array until it hits an element thats greater.
         //          Will be used in iterativeInsertionSort() and recursiveInsertionSort() to sort an array.

        int siftItem = array[pos]; // remember the item we are sifting up.
        int i; // We will need to save the stopping index.
        // Move one position to the left all items that are < siftItem.
        for (i = pos + 1; (i < array.length) && (array[i] < siftItem); i++)
            array[i-1] = array[i];
        
        // Put siftItem into its correct position.
        array[i-1] = siftItem;
    } // end siftUp

    public static void iterativeInsertionSort(int[] array){
        /** 
         * PURPOSE: Iterative solution to an insertion sorting algorithm.
         *          siftUp() is called on each element of @param int[] array by looping through the last to first index.
         *          siftUp() moves the element up until it hits an element greater than it,
         *          so calling siftUp() on every element starting at the end results in an array in ascending order.
         */    

        // If the array has none or one element, it is already sorted in ascending order.
        if (array.length > 1){
            // Loop through the elements of the parameter array, starting at the second last element and call siftUp().
            // siftUp() doesn't need to be called on the last element,
            // because its the only element in the sorted portion, therefore it is already in ascending order.
            for (int i = array.length-2; i >= 0; i--)
                siftUp(array, i);
        }
    } // end iterativeInsertionSort

    public static void recursiveInsertionSort(int[] array){
        /** 
         * PURPOSE: Driver method for a recursive solution to an insertion sorting algorithm.
         *          Helper method recursiveInsertionSort(int[], int) is called from the second last element
         *          The helper method does not need to start at the last element because its the only item in the 
         *          sorted portion of the array, therefore the array is already in ascending order.
         *          The helper method will recursively call itself to move through the elements by decreasing index,
         *          resulting in the array being sorted in ascending order.
         */
        
        // If the array has none or one element, it is already sorted in ascending order.
        if (array.length > 1)
            recursiveInsertionSort(array, array.length-2);
    } // end driver recursiveInsertionSort

    private static void recursiveInsertionSort(int[] array, int pos){
        /** 
         * PURPOSE: Helper method to the driver recursiveInsertionSort(int[]).
         *          Will call siftUp() on the current position of the array,
         *          siftUp() will move the element up until it hits an element thats greater,
         *          and if the current element isn't the beginning element, then we recursively call
         *          the next, smaller element.
         */
        
        siftUp(array, pos); // Move the element up until it hits an element thats greater.
        // If the current position isn't the first element, recursively call on the next smaller position.
        if (pos > 0)
            recursiveInsertionSort(array, pos - 1);
    } // end helper recursiveInsertionSort
    
} // end A1HollandEthan
