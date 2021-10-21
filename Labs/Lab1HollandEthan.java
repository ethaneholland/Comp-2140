/*******************************************************
 *   Lab 1   COMP 2140   Fall 2021
 *
 *   Student name: Ethan Holland
 *   Student ID:   7865033
 *
 *******************************************************/


public class Lab1HollandEthan {
    
    public static final int N = 35; // compute S(N) recursively and iteratively

    public static final long S0 = 1L; // S(0) = 1
    public static final long S1 = 2L; // S(1) = 2
    public static final long S2 = 3L; // S(2) = 3
    
    public static void main( String[] args ) {
	System.out.println( "\n\nLab 1 COMP 2140 Solution: Recursion and Iteration\n" );
	
	testShalenMethods();

	System.out.println( "\nProgram ends successfully" );
    } // end main


    private static void testShalenMethods() {
	long start, stop, elapsedTime;  // Time how long computing S(N) takes.
	long result; // result returned when computing S(N)

	// Test the NONrecursive Shalen method
	start = System.nanoTime();
	result = nonrecursiveShalen( N );
	stop = System.nanoTime();
	elapsedTime = stop - start;
	printReport( "Report on the nonrecursiveShalen method:",
		     N, result, elapsedTime );
    
	// Test the recursive Shalen method
	start = System.nanoTime();
	result = recursiveShalen( N );
	stop = System.nanoTime();
	elapsedTime = stop - start;
	printReport( "Report on the recursiveShalen method:",
		     N, result, elapsedTime );

    } // end method testShalenMethods

    // Print out a header specifying the method used to compute S(n),
    // then on the next lines, print out n, S(n), and the time taken to
    // compute n using the method.
    private static void printReport( String methodUsed,
				     int n,
				     long result,
				     long elapsedTime ) {
	System.out.println( methodUsed + "\n" );
	System.out.println( "Time needed to compute S( " + n
			    + " ): " + elapsedTime + " nanoseconds");
	System.out.println( "S( " + n + " ) = " + result + "\n" );
    
    } // end method printReport
    

    // Recursively compute S(n),
    // where S(0) = 1 and S(1) = 2 and S(2) = 3 (these are the base cases)
    // and, when n > 2, S(n) is computed using the following formula
    // S(n) = S(n-1) + sum of ((-1)^k * S((n-2)-2*k)) for k = 0, 1, ..., (n-2)/2.
    //
    // Note that when n > 2,
    // 1) S(n-1) and each S((n-2)-2*k) term on the right side of the equals sign
    //     is computed with a recursive call; and
    // 2) (-1)^k is 1 when k=0 and alternates between -1 and 1 as k increases.

    public static long recursiveShalen( int n ) {
        /* PURPOSE: Recursively computes the Shalen number S(n)
         *          where S(0) = 1 and S(1) = 2 and S(2) = 3 (these are the base cases)
         *          and, when n > 2, S(n) is computed using the following formula:
         *          S(n) = S(n-1) + sum of ((-1)^k * S((n-2)-2*k)) for k = 0, 1, ..., (n-2)/2.
         *          
         *          If @param n satisfies one of the base cases then @return is the corresponding Long value,
         *          Otherwise @return is the addition of S(n-1) and sum of ((-1)^k * S((n-2)-2*k)) for k = 0, 1, ..., (n-2)/2
         *          which are found recursively.
         */

        long shalenNumber; // @return variable

        // Check for if n satisfies a base case
        if (n == 0)
            shalenNumber = S0;
        else if (n == 1)
            shalenNumber = S1;
        else if (n == 2)
            shalenNumber = S2;
        else{
            // n > 2 and doesn't satisfy any base cases

            // Calculate the sum of  ((-1)^k * S((n-2)-2*k)) for k = 0, 1, ..., (n-2)/2 
            long summand = 0; // Current summand at the index of summation
            long summation = 0; // Total of all summands from lower to upper bounds of summation
            //Loop through index of summation from the lower bound of summation 0 to the upper bound of summation ( n - 2 ) / 2
            for (int index = 0; index <= ( n - 2 ) / 2; index++){
                
                if (index % 2 != 0) 
                    summand = -1;   // If k is negative the summand will be a multiple of -1
                else            
                    summand = 1;    // If k is positive the summand will be a multiple of 1
                // Recursively find S((n-2)-2*k)
                summand *= recursiveShalen( (n - 2) - 2 * index);
                summation += summand; // Add the current summand to the total summation
            } // end for

            // @return will be the addition of S(n-1) and sum of ((-1)^k * S((n-2)-2*k)) for k = 0, 1, ..., (n-2)/2
            shalenNumber = recursiveShalen( n - 1 ) + summation;
        } // end else

        return shalenNumber; //return the n'th Shalen Number
	
    } // end method recursiveShalen

    // Compute S(n) without recursion,
    // where S(0) = 1 and S(1) = 2 and S(2) = 3
    // and, when n > 2, S(n) is computed using the following formula
    // S(n) = S(n-1) + sum of ((-1)^k * S((n-2)-2*k)) for k = 0, 1, ..., (n-2)/2.
    //
    // Idea:
    // Store S(0), S(1), S(2) in an array of n+1 elements.
    // Then compute S(3), S(4), ..., S(n) in turn, storing each in the array.
    // When computing S(i),look up in the array the values of
    // S(i-1) and S((i-2)-2*k) on the right side of the formula --- they
    // will already have been stored in the array by the time we are
    // computing S(i).
    public static long nonrecursiveShalen( int n ) {
        /* PURPOSE: Iteratively computes the Shalen number S(n)
         *          where S(0) = 1 and S(1) = 2 and S(2) = 3 (these are the base cases)
         *          and, when n > 2, S(n) is computed using the following formula:
         *          S(n) = S(n-1) + sum of ((-1)^k * S((n-2)-2*k)) for k = 0, 1, ..., (n-2)/2.
         *          
         *          If @param n satisfies one of the base cases then @return is the corresponding Long value,
         *          Otherwise @return is the addition of S(n-1) and sum of ((-1)^k * S((n-2)-2*k)) for k = 0, 1, ..., (n-2)/2
         *          which are found iteratively by computing each Shalen Number after the base cases up until @param n inclusive, 
         *          and storing them in long[] shalenNumbersArray.
         */
    
        long[] shalenNumbersArray = new long[n + 1]; // List of all Shalen Numbers from 0 to n
        // Base case Shalen numbers
        if ( n >= 0) { shalenNumbersArray[0] = S0; }
        if ( n >= 1) { shalenNumbersArray[1] = S1; }
        if ( n >= 2) { shalenNumbersArray[2] = S2; }
    
        // Loop through non base case shalen numbers up to n
        for (int shalenNumber = 3; shalenNumber <= n; shalenNumber++){

            long summand = 0; // Current summand at the index of summation
            long summation = 0; // Total of all summands from lower to upper bounds of summation
            // Loop through index of summation from the lower bound of summation 0 to the upper bound of summation ( n - 2 ) / 2
            for (int index = 0; index <= ( shalenNumber - 2 ) / 2; index++){
            
                if (index % 2 != 0) 
                    summand = -1;   // If k is negative the summand will be a multiple of -1
                else               
                    summand = 1;    // If k is positive the summand will be a multiple of 1

                summand *= shalenNumbersArray[ (shalenNumber - 2) - 2 * index ]; // Find Shalen Number S((n-2)-2*k)
                summation += summand; //add the current summand to the total summation
            }  // end for

            //add Shalen Number [ S(n-1) + sum of ((-1)^k * S((n-2)-2*k)) for k = 0, 1, ..., (n-2)/2 ] to the list
            shalenNumbersArray[ shalenNumber ] = shalenNumbersArray[ shalenNumber - 1 ] + summation;
        } // end for

	    return shalenNumbersArray[n]; //return the n'th Shalen Number
    } // end method nonrecursiveShalen

} // end class Lab1
