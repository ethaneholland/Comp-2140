/**
 * A2HollandEthan
 * 
 * COMP 2140 SECTION A01
 * INSTRUCTOR   Shahin Kamali 
 * ASSIGNMENT   Assignment 2
 * @author      Ethan Holland, 7865033
 * @version     Oct 19, 2021
 * 
 * PURPOSE: This program will test and compare an iterative insertion sort and an alternative merge sort
 *          that recursively divides the array and subarrays into 4 partitions and merges them into ascending order.
 */

 
public class A2HollandEthan {
    
    // Global constants used in creating randomized arrays.
    private static final int ARRAY_SIZE = 10000;
    private static final int MAX_VALUE = 5000;
    // Global constant for a number that is bigger than the MAX_VALUE of the elements in an array.
    private static final int EXCEED_MAX = 99999;


    public static void main(String[] args){

        System.out.println("Assignment 2 Solution (merging with four recursive calls)\n");
        // Test the iterative insertion sort and return the length of time that it took to finish.
        long insertionTime = testInsertionSort();
        // Test the alternative merge sort and return the length of time that it took to finish.
        long mergeTime = testMergeSort();
        // Print the comparison of the insertion and merge sorting algorithms.
        printTimeResults(mergeTime, insertionTime);
        System.out.println("Processing Ends.");

    } // end main
    
    private static long testInsertionSort(){
        /**
         * PURPOSE: Create an array of randomized integers from 0 to MAX_VALUE
         *          and test the iterative insertion sorting algorithm to sort the array
         *          into ascending order. 
         *          Return the time that the algorithm took to finish.
         */

        long start, stop; // Record the starting and stopping clock times of the algorithm.

        System.out.println("Testing Iterative Insertion Sort:\n");
        // Create and fill a new array with randomized integers.
        int[] array = new int[ARRAY_SIZE];
        fillArray(array, MAX_VALUE);

        System.out.println("Array before sorting:\n" + arrayToString(array) + "\n"); // Print the new array contents.
        // Test the insertion sorting algorithm and track the start and stop clock times.
        start = System.nanoTime();
        iterativeInsertionSort(array);
        stop = System.nanoTime();

        // Verify that the array is sorted in ascending order, and print the result.
        if (isSorted(array))
            System.out.println("Array is correctly sorted\n");
        else
            System.out.println("ERROR: Array is NOT correctly sorted\n");

        System.out.println("Array after sorting:\n" + arrayToString(array) + "\n"); // Print the sorted array contents.
        
        return stop - start; // Output the total time that the algorithm took to compute.
    } // end testInsertionSort

    private static long testMergeSort(){
        /** 
         * PURPOSE: Create an array of randomized integers from 0 to MAX_VALUE
         *          and test the alternative merge sorting algorithm to sort the array
         *          into ascending order.
         *          Print the height of the recursion tree that the merge sort took.
         *          Return the time that the algorithm took to finish.
         */

        long start, stop; // Record the starting and stopping clock times of the algorithm.

        System.out.println("Testing Alternative Merge Sort:\n");
        // Create and fill a new array with randomized integers.
        int[] array = new int[ARRAY_SIZE];
        fillArray(array, MAX_VALUE);

        System.out.println("Array before sorting:\n" + arrayToString(array) + "\n"); // Print the new array contents.
        // Test the merge sorting algorithm and track the start and stop clock times,
        // and record the height of the recursion tree of the merge sort.
        start = System.nanoTime();
        int height = mergeSortAlt(array);
        stop = System.nanoTime();

        // Verify that the array is sorted in ascending order, and print the result.
        if (isSorted(array))
            System.out.println("Array is correctly sorted\n");
        else
            System.out.println("ERROR: Array is NOT correctly sorted\n");

        System.out.println("Array after sorting:\n" + arrayToString(array) + "\n"); // Print the sorted array contents.
        // Print the height of the recursion tree of the merge sort.
        System.out.println("The height of the recursion tree is: " + height); 

        return stop - start; // Output the total time that the algorithm took to compute.
    } // end testMergeSort

    private static void printTimeResults(long insertionTime, long mergeTime){
        /** 
         * PURPOSE: Takes long @params mergeTime and insertionTime
         *          to output to the console the length of time that the merge sort and insertion sort
         *          algorithms took to finish, then compares the two lengths of time.
         */

        System.out.println("\nTiming:");
        // Print the length of time that the insertion sort took to finish.
        System.out.println("\nTime to iteratively insertion sort: " + insertionTime + " nanoseconds.");
        // Print the length of time that the merge sort took to finish.
        System.out.println("\nTime to alternative merge sort: " + mergeTime + " nanoseconds.");
        
        // Compare the speed of the insertion sort and merge sort algorithms and print the comparison.
        if (mergeTime <= insertionTime)
            System.out.println("\nThe merge sort was at least as fast as the insertion sort!");
        else
            System.out.println("The merge sort was not as fast as the insertion sort!");
    } // end printTimeResults

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

    public static int mergeSortAlt(int[] array){
        /** 
         * PURPOSE: Driver method of a recursive alternative merge sort that divides
         *          an array into 4 partitions.
         *          This method will start the recursive sequence to sort from position 0 to the end of the array.
         *          Returns the height of the recursion tree that the algorithm took to finish sorting in ascending order.
         */
        return mergeSortAlt(array, 0, array.length-1);
    } // end mergeSortAlt (driver)

    private static int mergeSortAlt(int[] array, int lo, int hi){
        /** 
         * PURPOSE: Recursive helper sorts the array from positions lo to hi 
         *          by dividing the array into 4 partitions, and calling mergeSortAlt
         *          on the subarrays until hi is no longer greater than lo. Then helper method merge()
         *          will merge the 4 sorted partitions into 1 sorted subarray.
         *          Returns the amount of recursive calls made from this call and subsequent calls.
         */

        // Track the height of the recursion tree past this call.
        int height = 0;
        // Divide the subarray from lo to hi into 4 partitions.
        int step = (int)Math.ceil( ( (float)(hi+1 - lo) / 4 ) );
        // There are no more recursive calls to be made if there is only one element (lo == hi)
        // or no element in the subarray from lo to hi (lo > hi).
        if ( hi > lo ){
            // There is another set of recursive calls to be made,
            height += 1; // Add one to the height of the recursion tree.
            // Track the recursive calls made past the four partitions.
            int height1, height2, height3, height4;
            // Calculate the stopping index of each partition.
            int mid1 = lo + step - 1;
            int mid2 = lo + 2 * step - 1;
            int mid3 = lo + 3 * step - 1;
            // If the stopping index of any partition is greater than the maximum index (hi) of the subarray,
            // then adjust stopping index to not exceed the maximum index.
            // This might mean that the partition is empty, or has less elements than the step.
            if ( mid3 > hi )
                mid3 = hi;
            if ( mid2 > hi )
                mid2 = hi;
            if ( mid1 > hi )
                mid1 = hi;
            // Recursively sort the four partitions,
            // and record the number of recursive calls that the partitions made to finish sorting.
            height1 = mergeSortAlt(array, lo, mid1);
            height2 = mergeSortAlt(array, mid1 + 1, mid2);
            height3 = mergeSortAlt(array, mid2 + 1, mid3);
            height4 = mergeSortAlt(array, mid3 + 1, hi);
            // Create a temporary array of the same size as the array to aid in merging the four partitions.
            int[] temp = new int[array.length];
            // Merge the 4 sorted partitions into one sorted subarray from lo to hi.
            merge(array, lo, mid1, mid2, mid3, hi, temp);
            // Add to the height of the recursion tree the most recursive calls that any of the partitions took to finish.
            height += Math.max(height1, Math.max(height2, Math.max(height3, height4)));
        }
        return height; // Output the height of the recursion tree past this call.
    } // end mergeSortAlt (recursive helper)

    private static void merge(int[] array, int lo, int mid1, int mid2, int mid3, int hi, int[] temp){
        /** 
         * PURPOSE: Takes the indices of four sorted partitions and merges them into one sorted subarray of array from lo to hi.
         *          Uses the temp array to sort the items of the four partitions and then copies that into the correct position of array.
         */
        // Counters to move through the items in the four sorted partitions when the items are added to the temp array.
        int a,b,c,d;
        // Start all the counters at lo
        // 'a' always starts at lo,
        // 'b', 'c', and 'd' will stay at lo if their respective partitions of the array
        // happen to exceed the upper bounds of the subarray from lo to hi.
        a = b = c = d = lo;
        // If the lower bound of any of the partitions doesn't exceed the maximum index of the subarray from lo to hi,
        // start the counter from the lower bound of the respective partition.
        if ( mid1 < hi )
            b = mid1 + 1;
        if ( mid2 < hi )
            c = mid2 + 1;
        if ( mid3 < hi)
            d = mid3 + 1;

        // iterate through the elements of all four partitions of the subarray from lo to hi.
        for (int k=lo; k <= hi; k++){
            // Find the next smallest number in the array from lo to hi,
            // then add it to the sorted temp array and temporarily replace its spot in the array
            // with the EXCEED_MAX value that is bigger than all other values in the array from 0 to MAX_VALUE
            // to avoid the element from being added to the temp array more than once.
            
            // Check if the first partition contains the next smallest element.
            if ( (array[a] <= array[b]) && (array[a] <= array[c]) && (array[a] <= array[d]) ){
                // Add the next smallest element to the sorted temp array.
                temp[k] = array[a];
                // Temporarily make this used up element a larger number than the MAX_VALUE.
                array[a] = EXCEED_MAX;
                // If there is more elements in the partition to add, increase the partitions counter.
                if ( a < mid1 )
                    a++;
            }
            else if ( (array[b] <= array[a]) && (array[b] <= array[c]) && (array[b] <= array[d]) ){
                // Add the next smallest element to the sorted temp array.
                temp[k] = array[b];
                // Temporarily make this used up element a larger number than the MAX_VALUE.
                array[b] = EXCEED_MAX;
                // If there is more elements in the partition to add, increase the partitions counter.
                if ( b < mid2 )
                    b++;
            }
            else if ( (array[c] <= array[a]) && (array[c] <= array[b]) && (array[c] <= array[d]) ){
                // Add the next smallest element to the sorted temp array.
                temp[k] = array[c];
                // Temporarily make this used up element a larger number than the MAX_VALUE.
                array[c] = EXCEED_MAX;
                // If there is more elements in the partition to add, increase the partitions counter.
                if ( c < mid3 )
                    c++;
            }
            else if ( (array[d] < array[a]) && (array[d] < array[b]) && (array[d] < array[c]) ){
                // Add the next smallest element to the sorted temp array.
                temp[k] = array[d];
                // Temporarily make this used up element a larger number than the MAX_VALUE.
                array[d] = EXCEED_MAX;
                // If there is more elements in the partition to add, increase the partitions counter.
                if ( d < hi )
                    d++;
            }
        } // end for

        // Add the sorted temp array containing all the elements from the four partitions
        // into the array from lo to hi.
        for (int i=lo; i <= hi; i++)
            array[i] = temp[i];
    } // end merge      
} // end A2HollandEthan
