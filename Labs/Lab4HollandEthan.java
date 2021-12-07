/*******************************************************
 *   Lab 4   COMP 2140   Fall 2021
 *
 *   Student name: Ethan Holland
 *   Student ID:   7865033
 *
 *******************************************************/


public class Lab4HollandEthan {

    // Constants that control the testing
    private static final int TREE_SIZE = 100;
    private static final int ITEM_LIMIT = 200;
    private static final int NUM_TREES = 10;
    

    public static void main( String[] args ) {

	System.out.println( "\n\nLab 4: Does a tree satisfy the BST property?" );

	testTrees();

	System.out.println( "\nLab 4 ends..." );
	
    } // end main
    

    private static void testTrees() {

	testValidBSTs();

	testNonBSTs();
	
    } // end testTrees
    

    private static void testValidBSTs() {
	
	BST tree;
	
	// Create NUM_TREES valid BSTs using the correct insertion algorithm
	// and verify that they satisfy the BST property.
	System.out.println( "\nTesting valid BSTs of roughly " + TREE_SIZE + " items" );
	System.out.println( "    Note: log( " + TREE_SIZE + " ) is roughly " +
			    (int)(Math.log( TREE_SIZE ) / Math.log( 2.0 )) );
	for ( int i = 0; i < NUM_TREES; i++ ) {
	    
	    // Create a BST containing <= TREE_SIZE random values,
	    // where the values are >= 0 and < ITEM_LIMIT.
	    tree = new BST();
	    for ( int j = 0; j < TREE_SIZE; j++ ) {
		tree.insert( (int)(Math.random() * ITEM_LIMIT) );
	    } // end for j

	    // Print out the tree and its height, and verify that it satisfies the BST property.
	    System.out.println( "BST " + i + " contains:" + tree );
	    System.out.println( "  BST " + i + " has height " + tree.height() );
	    if ( tree.satisfiesBSTProperty() ) {
		System.out.println( "  BST " + i + " satisfies the BST property." );
	    } else {
		System.out.println( "  ERROR: satisfyBSTProperty() says that BST " + i
				    + " does NOT satisfy the BST property." );
	    } // end if-else
	    
	} // end for i
    } // end testValidBSTs

    private static void testNonBSTs() {
	
	BST tree;
	
	// Create NUM_TREES trees using the "crazy" insertion algorithm
	// and verify that they do NOT satisfy the BST property.
	System.out.println( "\nTesting trees that don't satisfy the BST propery" );
	for ( int i = 0; i < NUM_TREES; i++ ) {
	    
	    // Create a tree containing <= TREE_SIZE random values,
	    // where the values are >= 0 and < ITEM_LIMIT.
	    tree = new BST();
	    for ( int j = 0; j < TREE_SIZE; j++ ) {
		tree.crazyInsert( (int)(Math.random() * ITEM_LIMIT) );
	    } // end for j

	    // Print out the tree and verify that it doesn't satisfy the BST property.
	    System.out.println( "Tree " + i + " contains:" + tree );
	    if ( tree.satisfiesBSTProperty() ) {
		System.out.println( "ERROR: satisfyBSTProperty() says that tree " + i
				    + " satisfies the BST property." );
	    } else {
		System.out.println( "   Tree " + i
				    + " does NOT satisfy the BST property." );
	    } // end if-else
	    
	} // end for i
    } // end testNonBSTs
    
} // end class Lab4


//=============================================================
// BST  - a binary search tree that stores ints (no duplicates)
//=============================================================

class BST {

    //=========================================================
    // BSTNode - a node in a binary search tree (stores an int)
    //=========================================================
    
    private class BSTNode {
	
	public int item;
	public BSTNode left;
	public BSTNode right;

	// The constructor makes a leaf (a node with no children)
	public BSTNode (int i) {
	    item = i;
	    left = right = null;
	} // end BSTNode constructor
	
    } // end class BSTNode

    //===========================================================

    private BSTNode root;

    // The constructor makes an empty tree (root is null)
    public BST() {
	root = null;
    } // end BST constructor

    
    // Do an inorder traversal to create a String representation of the BST.
    public String toString() {
	return toString( root );
    } // end toString public driver method

    private String toString( BSTNode curr ) {
	String res = "";
	
	if ( curr != null ) {
	    res += toString( curr.left );
	    res += curr.item + " ";
	    res += toString( curr.right );
	} // end if

	return res;
    } // end toString recursive helper method

    
    // Normal iterative insertion into a BST
    // (the insertion maintains the BST property).
    public void insert( int newItem ) {
	BSTNode prev = null;
	BSTNode curr = root;
	BSTNode newNode;
	boolean found = false;

	if (root == null) {
	    // The tree is empty --- the new node will be the root!
	    root = new BSTNode(newItem);
	} else {
	    // The tree is NOT empty

	    // First, find the parent of the new node.
	    while ( curr != null && ! found ) {
		prev = curr;
		if ( newItem == curr.item )
		    found = true;
		else if ( newItem < curr.item)
		    curr = curr.left;
		else
		    curr = curr.right;
	    } // end while

	    // Now create the new node and link it in
	    // (only if newItem was NOT found: no duplicates!)
	    if ( !found ) {
		newNode = new BSTNode( newItem );
		// prev should be the parent of the new node.
		if ( newItem < prev.item )
		    prev.left = newNode;
		else
		    prev.right = newNode;
	    } // end if not found
	} // end else the tree is not empty
    } // end insert



    public void crazyInsert( int newItem ) {
		/** 
		 * PURPOSE: Traverses the tree by moving left or right at random and inserts the newItem
		 * 			as a random leaf that is very unlikely to preserve the binary search tree property.
		 * 			If there is no root BSTNode, then the newItem is inserted as the root.
		 */

		if (root != null){
			BSTNode curr = root;
			BSTNode prev = root;
			boolean moveLeft = false; // Traverse left of the curr BSTNode if true.
			while (curr != null){
				// curr points to a BSTNode.
				prev = curr; // Remember the curr BSTNode.
				moveLeft = Math.random() < 0.5; // Randomly traverse left or right of the curr BSTNode.
				if (moveLeft) 
					curr = curr.left;
				else 
					curr = curr.right;
			}
			// curr is null and is the new position of our newItem,
			// test if we last traversed left or right of prev to get to the curr position, and insert the newItem.
			if (moveLeft)
				prev.left = new BSTNode( newItem );
			else 
				prev.right = new BSTNode( newItem );
		}
		// If the root is null, insert the newItem as the new root.
		else
			root = new BSTNode( newItem );
    } // end crazyInsert

    public boolean satisfiesBSTProperty() {
		/** 
		 * PURPOSE: Driver method to traverse the Tree and test that none of the BSTNodes
		 * 			break the BST property. Starts the recursive at the root BSTNode.
		 */

		return satisfiesBSTProperty( root, Integer.MIN_VALUE, Integer.MAX_VALUE );
    } // end satisfiesBSTProperty public driver method

    // Recursive helper method's task:
    // Ensure that curr and all its descendants satisfy the BST propery
    // and that their values are all > min and < max.
    private boolean satisfiesBSTProperty( BSTNode curr, int min, int max ) {
		/** 
		 * PURPOSE: Recursive helper method to traverse the Tree and test that none of the BSTNodes
		 * 			break the BST property.
		 * 			If the curr BSTNode is null, then the BST properties are still preserved.
		 * 			If the curr BSTNode exists, then BST properties are preserved if its value is 
		 * 			greater than the min and less than the max values.
		 * 			If the curr BSTNode does not break the BST properties, recursively test the left and right BSTNodes.		
		 */

		// Output defaults to true if the curr Node is null,
		// or if none of the branches starting at curr break the BST properties.
		boolean satisfied = true; 
		if (curr != null){
			// If the curr value is between the min and max then the BST properties are not broken.
			if (curr.item <= min || curr.item >= max)
				satisfied = false;
			// If the BSTNode on the left is bigger than the min and less than the curr value, 
			// and the BSTNode on the right is less than the max and bigger than the curr value,
			// the BST properties are broken.
			else if (!satisfiesBSTProperty( curr.left, min, curr.item ) || !satisfiesBSTProperty( curr.right, curr.item, max ))
				satisfied = false;
		}
		return satisfied;

    } // end satisfiesBSTProperty private recursive helper method

	public int height(){
		/** 
		 * PURPOSE: Driver method that returns the longest path from the root to any of its leafs, 
		 * 			inclusive of the leaf and root BSTNode.
		 */
		return height( root );
	} // end height public driver method

	private int height( BSTNode curr ){
		/** 
		 * PURPOSE: Recursive helper method that returns the longest path from the curr BSTNode
		 * 			to any of its leafs, inclusive of the leaf and the curr BSTNode.
		 */

		int currHeight = 0; // Output height is default 0 when the curr Node is null.
		if (curr != null)
			// Recursively find the height of the left and right branches started at the next BSTNode down,
			// Take the bigger of the two, and add 1 to include the curr Node in the total height.
			currHeight = Math.max( height( curr.left ), height( curr.right ) ) + 1;
		return currHeight;
	} // end height private recursive helper method

} // end class BST
