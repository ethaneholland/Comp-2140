/*******************************************************
 *   Lab 2   COMP 2140   Fall 2021
 *
 *   Student name: Ethan Holland
 *   Student ID:   7865033
 *
 *******************************************************/

public class Lab2HollandEthan {

    private static final int MAX_ITEM = 64; // maximum int value in the list
    private static final int KEY = 42; // key to delete (construct runs of this key in test list!)
    private static final int RUN_LEN = 20; // construct runs of this length at front, middle, end of list
    
    public static void main( String[] args ) {
	System.out.println( "\n\nLab 2 COMP 2140 Solution: Simple Linked Lists\n" );
	
	testDeleteAll();

	System.out.println( "\nProgram ends successfully" );
    } // end main

    private static void testDeleteAll() {
	System.out.println( "Creating a list containing duplicates of " + KEY );
	LinkedList myList = createTestList();
	
	System.out.println( "\nList before deletions:" );
	System.out.println( myList );

	System.out.println( "\nDeleting all duplicates of " + KEY );
	myList.deleteAll( KEY );

	System.out.println( "\nList after deletions:" );
	System.out.println( myList );

	if ( myList.contains( KEY ) ) {
	    System.out.println( "\ndeleteAll failed: List still contains " + KEY );
	} else {
	    System.out.println( "\ndeleteAll succeeded: List no longer contains " + KEY );
	} // end if-else
    } // end testDeleteAll

	
    // Return a list of the following form:
    //
    // < copies of KEY > < random values > < copies of KEY > < random values > < copies of KEY >
    //
    // where < x > represents a run of nodes containing x.
    private static LinkedList createTestList() {
	LinkedList list = new LinkedList();

	// Note: Because insert() inserts a value at the front of the list,
	//       we are constructing the list backwards (last node is inserted first).

	// Construct a run of nodes containing KEY of length RUN_LEN at the end of the list.
	for ( int i = 0; i < RUN_LEN; i++ ) {
	    list.insert( KEY );
	} // end for

	// Construct a run of nodes containing random values in the range [1,...,MAX_ITEM]
	// The run should be of length (LENGTH-3xRUN_LEN)/2 and will be
	// after the middle run of KEY and before the last run of KEY in the list.
	for ( int i = 0; i < RUN_LEN; i++ ) {
	    list.insert(  1 + (int)(Math.random() * MAX_ITEM) );
	} // end for

	// Construct a run of nodes containing KEY of length RUN_LEN in the middle of the list.
	for ( int i = 0; i < RUN_LEN; i++ ) {
	    list.insert( KEY );
	} // end for

	// Construct a run of nodes containing random values in the range [1,...,MAX_ITEM]
	// The run should be of length (LENGTH-3xRUN_LEN)/2 and will be
	// before the middle run of KEY and after the first run of KEY in the list.
	for ( int i = 0; i < RUN_LEN; i++ ) {
	    list.insert(  1 + (int)(Math.random() * MAX_ITEM) );
	} // end for

	// Construct a run of nodes containing KEY of length RUN_LEN at the beginning of the list.
	for ( int i = 0; i < RUN_LEN; i++ ) {
	    list.insert( KEY );
	} // end for

	return list;
    } // end createTestList

} // end class Lab2


/****************************************************************
 * Linked list of ints (unordered, can contain duplicate values)
 ****************************************************************/

class LinkedList {

    //----------------------------------------------------
    // private Node class inside the LinkedList class
    //
    // Notice that the Node instance variables are PUBLIC,
    // so code inside the LinkedList class can access them
    // directly (no need for getters and setters).
    //-----------------------------------------------------

    private class Node {
	
	public int item;
	public Node next;
    
	public Node( int newItem, Node newNext ) {
	    item = newItem;
	    next = newNext;
	} // end Node constructor
	
    } // end class Node

    //--------------------------------------------
    // Instance variable in the LinkedList class
    //--------------------------------------------

    private Node top;  // Pointer to the first node in the list

    //----------------------------------------
    // Constructor for the LinkedList class
    //----------------------------------------
    
    public LinkedList() {
	top = null;    // The list is initially empty.
    } // end constructor

    //-------------------------------------------
    // Instance methods for the LinkedList class
    //-------------------------------------------

    // Return a String representation of the LinkedList.
    public String toString() {
	String result = "";

	if ( top != null ) {
	    Node curr = top;

	    while ( curr != null ) {
		result += curr.item + " ";
		curr = curr.next;
	    } // end while
	} else {
	    result = "<empty list>";
	} // end if-else
	
	return result;
    } // end toString

    // Is the LinkedList empty (storing no values)?
    public boolean isEmpty() {
	return top == null;
    } // end isEmpty

    // Does the LinkedList contain key?
    public boolean contains( int key ) {
	Node curr = top;
	boolean hasIt = false;

	while ( curr != null && !hasIt ) {
	    hasIt = curr.item == key;
	    curr = curr.next;
	} // end while

	return hasIt;
    } // end contains

    // Insert newItem at the front of the list (unordered insert).
    public void insert( int newItem ) {
	top = new Node( newItem, top );
    } // end insert
    
    // Delete all nodes containing key from the list.
    public void deleteAll( int key ) {
	Node curr = top;
	Node prev = null;
	Node lastInRun;
	 
	// Search for key --- i.e., find the start of the first run (if there is one)
	// This is just setting up prev and curr for the main while-loop that deletes runs.
	while ( curr != null && curr.item != key ) {
	    prev = curr;
	    curr = curr.next;
	} // end while

	// Delete runs until there aren't any more copies of key in the list
	while ( curr != null ) {
	    // Delete the run of keys that we found.
	    // Note: curr points to the first node in the run, and
	    // prev points to the node before the first node in the run.
       
	    // Set lastInRun to point to the last node of the run of keys
	    // (prev and curr are not changed).
	    lastInRun = lastNodeInRun( curr, key );
	   
	    // Now: prev points to the node before the start of the run of keys,
	    // and lastInRun points to the last node of the run of keys.
	   
	    // Delete the run.
	    deleteOneRun( prev, lastInRun );

	    // Move prev and curr along in the linked list.
	    // We know:
	    // - lastInRun.next (if it exists) doesn't contain key,
	    // - if lastInRun.next doesn't exist, we've deleted to
	    //    the end of the list --- there are no more
	    //    nodes to check.
	   
	    if ( lastInRun.next != null ) {
		// Didn't delete to the end of the list ...
		// there's at least one more node to process.
		prev = lastInRun.next;
		curr = prev.next;
	    } else {
		// No more unprocessed nodes in the list
		curr = null;
	    } // end inner if

	    // Search for key --- i.e., find the start of the next run (if there is one)
	    while ( curr != null && curr.item != key ) {
		prev = curr;
		curr = curr.next;
	    } // end inner while
	    
	} // end outer while
    } // end deleteAll

    // Return a pointer to the last node in a run of nodes containing key
    //   (i.e., consecutive nodes containing key).
    //
    // curr points to the first node in the run of nodes containing key.
    //
    // Assumes: curr points to a node containing key.
    //
    private Node lastNodeInRun( Node curr, int key ) {
		//PURPOSE:	Assumes that Node curr's item matches the int key,
		//		   	and loops through the linked list until a Node doesn't match the key, 
		//		  	then the previous Node that matched the key is returned.
		
		Node nextNode = curr.next; // next Node after curr is checked for if its item matches the key.
		while ( nextNode != null && nextNode.item == key ){
			// While the next node exists (curr isn't the end of the list),
			// and the next node's item matches the key,
			// move to the next Node.
			curr = nextNode; 
			nextNode = curr.next;
		} // end while
		
		// Next node is either null or doesn't match the key,
		// curr is the last Node that matches the key.
		return curr; 
    } // end lastNodeInRun

    //  Delete one run of nodes containing key (i.e., consecutive nodes containing key).
    //
    //  Prev and lastInRun indicate a run of nodes in the linked list that contain key:
    //  - prev points to the node before the run
    //     (if there is a node before the run; otherwise, prev is null), and
    //  - lastInRun points to the last node of the run (this last node always
    //     exists).
    //
    // Delete the run from the linked list by changing the appropriate pointer.
    //
    //  Assumes: A run exists and lastInRun points to its last node.
    //
    private void deleteOneRun( Node prev, Node lastInRun ) {
		//PURPOSE:	Deletes a run of selected items from the linked list.
		//			Changes the pointer of the Node that comes before the run to the Node after the run,
		//			If the run is at the beginning of the list,
		//			then the linked list points to the Node that comes after the run as the first Node.

		// If there exists a previous Node before the run,
		if ( prev != null ) 
			// set the previous Node's pointer to the Node that comes after the last Node to be deleted.
			prev.next = lastInRun.next;
		else
			// There is no Node before the Run,
			// set the top pointer to the Node that comes after the last Node to be deleted.
			top = lastInRun.next;
    } // end deleteOneRun


} // end class LinkedList
