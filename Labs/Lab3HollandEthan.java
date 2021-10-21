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
		tail = null;
	} // end QueueCLL constructor

	public int peek(){
		int firstItem;
		if ( tail != null )
			firstItem = tail.next.item;
		else
			firstItem = Integer.MIN_VALUE;
		return firstItem;
	} // end method peak

	public void enqueue(int newItem){
		if ( tail != null ){
			tail.next = new Node(newItem, tail.next);
			tail = tail.next;
		}
		else {
			tail = new Node(newItem, null);
			tail.next = tail;
		}
	} // end method enqueue

	public int dequeue(){
		int removedItem;
		if ( tail == null )
			removedItem = Integer.MIN_VALUE;
		else{
			removedItem = tail.item;
			if ( tail.next == tail )
				tail = null;
			else{
				tail.next = tail.next.next;
			}
		}
		return removedItem;
	} // end method dequeue

	public String toString(){
		String contents = "< ";
		if ( tail != null ){
			contents += tail.next.item + " ";
			Node current = tail.next.next;
			while ( current != tail.next ){
				contents += current.item;
			}
		}
		contents += "<";
		return contents;
	}
    
} // end class QueueCLL
