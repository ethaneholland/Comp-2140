/*******************************************************
 *   Lab 3   COMP 2140   Fall 2021
 *
 *   Student name: Ethan Holland
 *   Student ID:   7865033
 *
 *******************************************************/

public class Lab3HollandEthan {

    private static final int N = 10;
    
    public static void main ( String[] args ) {
	System.out.println ( "\nLab 3 begins\n" );

	System.out.println( "Test number n is " + N );
		
	QueueCLL q = new QueueCLL();
		
	System.out.println( "Queue at beginning: " + q );
		
	for ( int i=0; i<N; i++ )
	    q.enqueue(i);
		
	System.out.println( "Queue after enqueuing 0 to n-1: " + q );
		
	for ( int i = N; i < 2*N; i++ ) {
	    System.out.println( "enqueued " + i );
	    q.enqueue(i);
	    System.out.println ( "dequeued " + q.dequeue() );
	    System.out.println( "peek returns " + q.peek() );
	} // end for
	
	System.out.println( "Queue at end: " + q );

	System.out.println( "\nLab 3 ends" );
	
    } // end main

} // end class Lab3

class QueueCLL {

	// Instance Variable:
	// tail points to the last Node in the queue,
	// the last Node has a pointer that points to the first Node in the queue.
	private Node tail;

    private class Node {
	public int item;
	public Node next;
	public Node ( int newItem , Node newNext ) {
	    item = newItem; 
	    next = newNext;
	} // end Node constructor
    } // end class Node
		
    public QueueCLL(){
		// PURPOSE: Creates an empty queue.

		tail = null;
	} // end QueueCLL constructor

	public int peek(){
		// PURPOSE: Returns the item of the first node in the queue.

		int firstItem;
		if ( tail != null )
			// If the queue has an item, view the first element.
			firstItem = tail.next.item;
		else
			// If the queue is empty then return minimum int.
			firstItem = Integer.MIN_VALUE;
		return firstItem;
	} // end method peak

	public void enqueue(int newItem){
		// PURPOSE: Creates a new Node containing newItem and adds it to the tail end of the queue.

		if ( tail != null ){
			// If the queue is not empty,
			// Create a new Node with the newItem and point it to the first Node in the queue,
			// Then point the previously last Node to this new Node.
			tail.next = new Node(newItem, tail.next);
			// Point tail to the new last Node in the queue.
			tail = tail.next;
		}
		else {
			// If the queue is empty,
			// Point tail to a new last Node in the queue that contains the newItem.
			tail = new Node(newItem, null);
			// The new Node is also the first Node in the queue, 
			// Point the new Node to itself.
			tail.next = tail;
		}
	} // end method enqueue

	public int dequeue(){
		// PURPOSE: Delete the first Node in the queue, and return its item.

		int removedItem;
		if ( tail == null )
			// If the queue is empty, return the minimum int.
			removedItem = Integer.MIN_VALUE;
		else{
			// If the queue is not empty,
			// Save the item from the first Node in the queue.
			removedItem = tail.next.item; 
			if ( tail.next == tail )
				// If the first Node was the only Node, the queue is now empty.
				tail = null;
			else{
				// If the first Node was not the only Node,
				// Point the tail Node to the next Node in the queue to make it the new first Node.
				tail.next = tail.next.next;
			}
		}
		return removedItem;
	} // end method dequeue

	public String toString(){
		// PURPOSE: Return contents of the queue in form: "< Node1.item Node2.item ... tail.item <"
		//			If there is no Nodes in queue, return will be of form: "< <".

		String contents = "< ";
		if ( tail != null ){
			// If the queue is not empty,
			// Add the first Node's item
			contents += tail.next.item + " ";
			// Track the Nodes after the first Node.
			Node current = tail.next.next;
			while ( current != tail.next ){
				// While the current Node is not the first Node, add the current Node.
				contents += current.item + " ";
				current = current.next;
			}
		}
		contents += "<"; // All Nodes in the queue have had their items added to the output.
		return contents;
	} // end method toString
    
} // end class QueueCLL
