import java.io.*;
import java.util.*;

public class Lab5HollandEthan
{
  private static final int NUM_INSERTIONS = 10; // The number of insertions to do

  /***************************************************
  * main                                             *
  *                                                  *
  * Purpose: Construct a new, empty leaf-based       *
  *          2-3 tree and then insert random         *
  *          keys into the tree, searching for each  *
  *          one after it is inserted.               *
  ***************************************************/
  public static void main( String[] args )
  {
    TwoThreeTree tree = new TwoThreeTree();
    int key;
    int[] keysInserted = new int[NUM_INSERTIONS];
    boolean allFound;


    System.out.println( "COMP 2140 Lab 5  Fall 2021" );
    System.out.println( "Insertions into a leaf-based 2-3 tree." );
    System.out.println( );

    // Perform some insertions

    System.out.println( "Inserting " + NUM_INSERTIONS + " random values" );
    for ( int i = 0; i < NUM_INSERTIONS; i++ )
    {
      key = (int)( Math.random() * 10 * NUM_INSERTIONS );
      tree.insert( key );
      keysInserted[i] = key;
    }
    System.out.println( "Insertions completed" );

    // Validate tree
 
    if ( !tree.isValid() )
      System.out.println( "\n*** Error: Tree is not a valid 2-3 tree --- pointers are wrong somewhere or values are misplaced." );
    else
      System.out.println( "\nValidator did not find any problems with pointers or placement of values in the tree." );

    // Search for inserted values

    allFound = true;
    for ( int i = 0; i < NUM_INSERTIONS; i++ )
      if ( !tree.search( keysInserted[i] ) )
      {
        System.out.println( "\n*** Error: " + keysInserted[i]
          + " was inserted, but search doesn't find it." );
        allFound = false;
      }

    if ( allFound )
      System.out.println( "\nAll values inserted can be found in searches" );

    System.out.println( );
    System.out.println( "Program for Lab5 ended normally." );
    
  } // end main

} // end class Lab5

/******************************************************
* 2-3 tree node class for leaf-based 2-3 trees        *
******************************************************/
class TwoThreeNode
{
  public TwoThreeNode parent; // A pointer to the parent of this node.
  public int numValues; // The number of values in the node (1 or 2).
  public int[] value; // An array of (up to) two values.
  public TwoThreeNode[] child; // An array of (up to) three children pointers


  /******************************************************************
   * Constructor for a leaf                                         *
   *              (contains only ONE key and NO children)           *
   *                                                                *
   * Note: The one key is stored in value[0] in an array of         *
   *       length 1.  Since it has NO children the child array is   *
   *       null --- which is one indication of a leaf.              *
   *****************************************************************/
  public TwoThreeNode( int key,
                       TwoThreeNode newParent )
  {
    value = new int[1];
    child = null;
    numValues = 1;
    value[0] = key;
    parent = newParent;
  } // end TwoThreeNode constructor (for a leaf).

  /******************************************************************
   * Constructor for an internal node with                          *
   *                   - one index value and                        *
   *                   - two children                               *
   *                                                                *
   * Note: The one index value is stored in value[0] and its two    *
   *       children are stored in child[0] (the child containing    *
   *       values < value[0]) and child[1] (the child containing    *
   *       values >= value[0]).                                     *
   *****************************************************************/
  public TwoThreeNode( int indexValue,
                       TwoThreeNode left,
                       TwoThreeNode right,
                       TwoThreeNode newParent )
  {
    value = new int[2];
    child = new TwoThreeNode[3];
    numValues = 1;
    value[0] = indexValue;
    value[1] = Integer.MIN_VALUE;
    child[0] = left;
    child[1] = right;
    child[2] = null;
    parent = newParent;
  } // end TwoThreeNode constructor (1 index value, 2 children)

  /******************************************************************
   * Constructor for a node with two index values and 3 children    *
   *                                                                *
   * Note: The two index values are stored in sorted order in       *
   *       value[0] and value[1], that is, value[0] < value[1].     *
   *       child[0] contains keys < value[0]                        *
   *       child[1] contains values >= value[0] and < value[1]      *
   *       child[2] contains values >= value[1]                     *
   * Assumes: val0 < val1                                           *
   ******************************************************************/  
  public TwoThreeNode( int val0, int val1, 
                       TwoThreeNode left, 
                       TwoThreeNode middle,
                       TwoThreeNode right,
                       TwoThreeNode newParent )
  {
    value = new int[2];
    child = new TwoThreeNode[3];
    numValues = 2;
    value[0] = val0;
    value[1] = val1;
    child[0] = left;
    child[1] = middle;
    child[2] = right;
    parent = newParent;
  } // end TwoThreeNode constructor (2 values, 3 children)

  /******************************************************************
   * isLeaf                                                         *
   *                                                                *
   * Returns: true if the node is a leaf (i.e., it has no children  *
   *       and therefore the child array is null);                  *
   *       otherwise, it returns false.                             *
   ******************************************************************/  
  public boolean isLeaf( )
  {
    return child == null;
  }

}  // end class TwoThreeNode



/******************************************************
* A Leaf-based 2-3 tree class                         *
*******************************************************/

class TwoThreeTree
{

  private TwoThreeNode root;  // A 2-3 tree is just a pointer to the root.


  /********************************************************************
  * TwoThreeTree constructor: construct an empty leaf-based 2-3 tree. *
  ********************************************************************/
  public TwoThreeTree( )
  {
    root = null;
  } // end TwoThreeTree constructor



  /*****************************************************************
  * search                                                         *
  *                                                                *
  * Purpose: Search a leaf-based 2-3 tree for a given key.         *
  * Returns: True if the key is in the tree, false if it isn't.    *
  *                                                                *
  * Reminder: Data items are in the leaves only.                   *
  *           You must search all the way to a leaf before you can *
  *           know whether an item with the search key is in the   *
  *           tree or not.                                         *
  *****************************************************************/
  public boolean search( int key )
  {
    TwoThreeNode leaf = findToLeaf( key );
    boolean result = false;

    if ( leaf != null )
      result = leaf.value[0] == key;
      
    return result;
  }

  /*****************************************************************
  * findToLeaf                                                     *
  *                                                                *
  * Purpose: Search a leaf-based 2-3 tree for a given key.         *
  *          (can be used as the first step for search, insert or  *
  *          delete)                                               *
  * Returns: null if the tree is empty; otherwise, it returns      *
  *          the leaf where the search ends.                       *
  *                                                                *
  * Reminder: Data items are in the leaves only.                   *
  *           You must search all the way to a leaf before you can *
  *           know whether an item with the search key is in the   *
  *           tree or not.                                         *
  *****************************************************************/
  private TwoThreeNode findToLeaf( int key )
  {
    TwoThreeNode curr = null;
    int moveResult;

    if ( root != null )
    {
      curr = root;
      while ( !curr.isLeaf() )
      {
        moveResult = moveToChild( curr, key );
        curr = curr.child[moveResult];
      } // end while

    }

    return curr;
    
  } // end method findToLeaf



  /******************************************************************
  * moveToChild                                                     *
  *                                                                 *
  * Purpose: We are searching for key in the 2-3 tree and we're     *
  *          currently at node curr.  Figure out which child of     *
  *          curr we should move to next.                           *
  *          Return the index of the child to move to:              *
  *            - return 0 if key < curr.value[0]                    *
  *              that is, move to curr.child[0].                    *
  *            - return 1 if curr.value[0] <= key < curr.value[1]   *
  *              that is, move to curr.child[1]                     *
  *              (of course, must have curr.numValues == 2)         *
  *            - return 2 if curr.value[1] < key                    *
  *              that is, move to curr.child[2]                     *
  *              (again, must have curr.numValues == 2)             *
  *******************************************************************/
  private int moveToChild( TwoThreeNode curr, int key )
  {
    int returnValue = curr.numValues; 
      // Assume that we should move to curr.child[curr.numValues].

    // Because values and children pointers are in arrays, we can
    // use a for-loop to search within node curr for key.

    for ( int i = 0; (i < curr.numValues) && (returnValue == curr.numValues);
              i++ )
    {
      if (key < curr.value[i])
      {
        returnValue = i;
      }
    } // end for
    
    return returnValue;
    
  } // end moveToChild



  /******************************************************************
  * insert                                                          *
  *                                                                 *
  * Purpose: Insert key as a new data item in a new leaf into       *
  *          TwoThreeTree this (i.e., does a leaf-based 2-3 tree    *
  *          insertion)                                             *
  *                                                                 *
  * Parameter:                                                      *
  *    key: The new data item to be added to the tree.              *
  *         (Data items are simply ints in this tree, with no       *
  *         other data associated with the key.)                    *
  *******************************************************************/
  public void insert( int key )
  {
    TwoThreeNode leaf;
    TwoThreeNode newLeaf;
    int newIndexValue;

    if ( root == null ) 
    {
      // Empty tree: add the first node

      root = new TwoThreeNode( key, null ); // create root (a leaf)
    }
    else if ( root.isLeaf() )
    {
      // Root is a leaf (tree contains 1 data item)
      // Just add a new leaf and a new root parent above.

      if ( key != root.value[0] ) // no duplicates!
      {
        // Create a new leaf for the new key

        leaf = root;
        newLeaf = new TwoThreeNode( key, null );

        // The larger of the two keys in the tree becomes the index value
        // in the (new) root parent.

        if ( newLeaf.value[0] < leaf.value[0] )
          root = new TwoThreeNode( leaf.value[0], newLeaf, leaf, null );
        else
          root = new TwoThreeNode( newLeaf.value[0], leaf, newLeaf, null );

       newLeaf.parent = root;
       leaf.parent = root;
      }
    }
    else
    {
      // Tree contains >= 2 keys already (root is not a leaf)
      // Do a leaf-based 2-3 tree insertion

      // First, search all the way to a leaf

      leaf = findToLeaf( key );

      // Insert only if not a duplicate

      if ( key != leaf.value[0] )
      {
        newLeaf = new TwoThreeNode( key, leaf.parent );
        splitsThenInsert( newLeaf, leaf );

      } // end if key is not a duplicate
    } // else tree contains >= 2 keys already (root is not a leaf)

  } // end insert





  /******************************************************************
  * splitsThenInsert                                                *
  *                                                                 *
  * Purpose: Insert newLeaf into the tree as a sibling right        *
  *          beside leafSibling.                                    *
  *          Problem: leafSibling's parent may not have room for    *
  *          a new child.                                           *
  *          Solution: Do as many splits as are necessary before    *
  *          you can resolve the insertion by either:               *
  *          (1) inserting a new child (and index value) into a     *
  *          parent that has room for the new child, OR             *
  *          (2) creating a new root above the old (split) root.    *
  *                                                                 *
  * Parameter:                                                      *
  *    newLeaf: The new leaf containing the new data item (simply   *
  *         an int key) that needs to be added to leafSibling's     *
  *         parent immediately beside leafSibling.                  *
  *    leafSibling: The leaf where the search for the new key       *
  *         ended --- the new leaf needs to be added to the tree    *
  *         immediately beside leafSibling.                         *
  *******************************************************************/
  private void splitsThenInsert( TwoThreeNode newLeaf, TwoThreeNode leafSibling )
  {
    TwoThreeNode curr = leafSibling; // Node that already exists in the tree.
    TwoThreeNode currSibling = newLeaf; // Node that must be added to the tree.
    // Value that differentiates curr and currSibling when navigating the tree.
    int separator = Math.max( curr.value[0], currSibling.value[0] );

    // While the current existing node is not the root, 
    // and the parent of curr is full.
    while (curr.parent != null && curr.parent.child[2] == null) {
      // There exists a parent of curr that is full, so currSibling cannot be added and curr parent must be split.
      // Create the node that will have half of curr.parent's data split into.
      TwoThreeNode currParentSibling = new TwoThreeNode( 0, null, null, null );
      // Save the parent of the current node.
      TwoThreeNode currParent = curr.parent;
      // Split curr.parent into currParentSibling and the remaining elements of curr.parent,
      // and return the median value of the parent values.
      separator = split( curr, currSibling, separator, curr.parent, currParentSibling );
      // Move up the tree to add the separator and currParentSibling to the tree.
      curr = currParent;
      currSibling = currParentSibling;
    }

    // If the current node is the root and there is no next parent to push the new node to.
    if (curr.parent == null) {
      // Make a new root pointing to the existing node and the new node.
      TwoThreeNode newRoot = new TwoThreeNode( separator, curr, currSibling, null );
      // Fix the parent pointer of the existing node.
      curr.parent = newRoot;
      // Fix the parent pointer of the new node.
      currSibling.parent = newRoot;
      root = newRoot;
    }
    // Else the there is a next parent node to push the new node to, and it is not full.
    else {
      // Add the new node up to curr.parent.
      addChildToParentWithRoom( curr, currSibling, separator );
    }
  } // end splitsThenInsert


  /******************************************************************
  * addChildToParentWithRoom                                        *
  *                                                                 *
  * Purpose: Add childNew as a new child, right beside childOrig,   *
  *          to childOrig's parent.                                 *
  *          Also add newSeparator as a new index value to          *
  *          childOrig's parent.                                    *
  *                                                                 *
  * Parameters:                                                     *
  *    childOrig: Already is a child of the parent that childNew    *
  *          should become a child of.  New child childNew should   *
  *          be added as a child right beside childOrig.            *
  *    childNew: TwoThreeNode that should be added as a new child   *
  *          to childOrig's parent.                                 *
  *    newSeparator: The new index value that should be added to    *
  *          childOrig's parent to guide searches to the correct    *
  *          child (i.e., will help decide whether to move to       *
  *          childOrig or childNew in a search)                     *
  *                                                                 *
  * Guarantee: childOrig's parent has only two children and one     *
  *          index value at the moment, so it has room for the new  *
  *          child and new index value.                             *
  *******************************************************************/
  private void addChildToParentWithRoom( TwoThreeNode childOrig, 
                                        TwoThreeNode childNew, 
                                        int newSeparator )
  {
    TwoThreeNode cparent = childOrig.parent;
    if ( childOrig == cparent.child[0] )
    {
      if ( childOrig.value[0] < childNew.value[0] )
      {
        // childNew should become child[1] of its parent
        cparent.child[2] = cparent.child[1];
        cparent.child[1] = childNew;
        cparent.value[1] = cparent.value[0];
        cparent.value[0] = newSeparator;
      }
      else // childNew should become child[0] of its parent
      {
        cparent.child[2] = cparent.child[1];
        cparent.child[1] = cparent.child[0];
        cparent.child[0] = childNew;
        cparent.value[1] = cparent.value[0];
        cparent.value[0] = newSeparator;
      }
    }
    else // childOrig is cparent.child[1]
    {
      if ( childOrig.value[0] < childNew.value[0] )
      {
        // childNew should become child[2] of its parent
        cparent.child[2] = childNew;
        cparent.value[1] = newSeparator;
      }
      else
      {
        // childNew should become child[1] of its parent
        cparent.child[2] = cparent.child[1];
        cparent.child[1] = childNew;
        cparent.value[1] = cparent.value[0];
        cparent.value[0] = newSeparator;
      }
    }
    cparent.numValues = 2;
    childNew.parent = cparent;
  } // end addChildToParentWithRoom


  /*****************************************************************
  * split                                                          *
  *                                                                *
  * Purpose: currParent can't hold another child and index value   *
  *          so this method splits currParent:                     *
  *          - the smallest index value (of the 3 index values)    *
  *            and the two leftmost children (of the 4 children)   *
  *            are put into currParent.                            *
  *          - the largest index value (of the 3 index values)     *
  *            and the two rightmost children (of 4 children)      *
  *            are put into currParentSibling.                     *
  *          Then this method returns the median of the three      *
  *          index values.                                         *
  *                                                                *
  * Details: currSibling needs to be curr's sibling beside curr    *
  *          in currParent, but currParent already has 3 children. *
  *          Split the four children (currParent's 3 children plus *
  *          currSibling) and three index values (currParent's 2   *
  *          index values plus separator) between currParent and   *
  *          currParentSibling (which is a new node created in     *
  *          the calling method just before calling split), then   *
  *          return the median of the three index values (the      *
  *          value that should be pushed up to the parent of       *
  *          currParent).                                          *
  *                                                                *
  * Parameters:                                                    *
  *   currParent: The node that needs to be split                  *
  *   currParentSibling: A newly-created node to contain some of   *
  *          the children and index values from the split.         *
  *          (It should be created in insert just before calling   *
  *          split, with two null children and some dummy index    *
  *          value.  Its children and index value will be          *
  *          correctly assigned in split.  This node needs to be   *
  *          created in insert because split can't return both the *
  *          median index value of the 3 index values AND the new  *
  *          sibling of currParent.)                               *
  *   curr: A TwoThreeNode that is already a child of currParent,  *
  *          and would be the adjacent sibling of currSibling,     *
  *          if currParent had room for currSibling.               *
  *   currSibling: A TwoThreeNode that needs to be added as child  *
  *          of currParent (except that currParent doesn't have    *
  *          room for it) --- currSibling would be added as a      *
  *          child beside curr, with index value separator used    *
  *          to decide which of curr or currSibling to move to     *
  *          in searches.  Instead, currSibling and separator      *
  *          can't fit and currParent must be split.               *
  *   separator: An index value that would allow a search to       *
  *           decide whether the search should move to curr or     *
  *           currSibling next (excpet there's no room for         *
  *           currSibling and the separator index value).          *
  *                                                                *
  * Returns: The median of the three index values, which must be   *
  *          pushed up to the parent of currParent with            *
  *          new child currParentSibling (and potentially cause    *
  *          a split at the parent of currParent).                 *
  *                                                                *
  * What must happen AFTER this method returns (hint for you):     *
  *           Both the median index value (that this method        *
  *           returns) and currParentSibling should be added to    *
  *           currParent's parent. Node currParentSibling should   *
  *           be added to currParent's parent as a new child       *
  *           beside currParent with medianValue as the separator  *
  *           index value between them. The parent of currParent   *
  *           may not have room for the new index value and the    *
  *           new child, so you may have to do another split       *
  *           after this one.                                      *
  *****************************************************************/
  private int split( TwoThreeNode curr, TwoThreeNode currSibling, 
                     int separator, TwoThreeNode currParent, 
                     TwoThreeNode currParentSibling )
  {
    int medianValue = Integer.MIN_VALUE;

    if ( curr == currParent.child[2] )
    {
      // curr and currSibling will be currParentSibling's children
      medianValue = currParent.value[1];
      currParentSibling.value[0] = separator;
      curr.parent = currParentSibling;
      currSibling.parent = currParentSibling;
      if ( curr.value[0] < currSibling.value[0] )
      {
        currParentSibling.child[0] = curr;
        currParentSibling.child[1] = currSibling;
      }
      else
      {
        currParentSibling.child[0] = currSibling;
        currParentSibling.child[1] = curr;
      }
    }
    else if ( curr == currParent.child[0] )
    {
      // curr and currSibling will be currParent's children
      medianValue = currParent.value[0];
      currParentSibling.value[0] = currParent.value[1];
      currParent.value[0] = separator;
      currSibling.parent = currParent;
      currParentSibling.child[0] = currParent.child[1];
      currParent.child[1].parent = currParentSibling;
      currParentSibling.child[1] = currParent.child[2];
      currParent.child[2].parent = currParentSibling;
      if ( curr.value[0] < currSibling.value[0] )
      {
        // currParent.child[0] already is curr (where curr should stay)
        currParent.child[1] = currSibling;
      }
      else
      { 
        currParent.child[1] = curr;
        currParent.child[0] = currSibling;
      }
    }
    else // curr is currParent.child[1].
         // one of curr and currSibling goes to currParent
         // and the other goes to currParentSibling
    {
      medianValue = separator;
      currParentSibling.child[1] = currParent.child[2];
      currParent.child[2].parent = currParentSibling;
      currParentSibling.value[0] = currParent.value[1];
      if ( curr.value[0] < currSibling.value[0] ) // curr stays where it is
      {
        currParentSibling.child[0] = currSibling;
        currSibling.parent = currParentSibling;
      }
      else // curr goes to currParentSibling
      {
        currParentSibling.child[0] = curr;
        curr.parent = currParentSibling;
        currParent.child[1] = currSibling;
        currSibling.parent = currParent;
      }
    }
    currParent.numValues = 1;
    currParentSibling.numValues = 1;
    currParentSibling.parent = currParent.parent;

    return medianValue;
  } // end split




  /******************************************************************
  * isValid                                                         *
  *                                                                 *
  * Purpose: Returns true if every node n has n.numValue valid      *
  *          children that point at n with their parent pointers    *
  *          AND that index values are set up properly to guide     *
  *          searches:                                              *
  *          the maximum key in the subtree immediately to the left *
  *          of an index value in n                                 *
  *             < the index value <=                                *
  *          the minimum key in the subtree immediately to the      *
  *          right of the index value in n.                         *
  *          So "valid" is about pointers being set up properly     *
  *          and also numValue holding a proper count of the        *
  *          number of index values (and, therefore, of the         *
  *          children) AND about index values being set up          *
  *          correctly.                                             *
  *                                                                 *
  * Returns: True if pointers and index values are set up correctly;*
  *          false, otherwise.                                      *
  *******************************************************************/
  public boolean isValid()
  {
    boolean valid = true;

    if ( root != null )
    {
      // Make sure that each child points at its parent
      valid = validParentPointers( root );

      // Make sure that the index values are appropriate
      valid = valid && validIndexValues( root );
    }
    else
    {
      // Tree should not be empty
      valid = false;
    }
    return valid;
  } // end isValid





  /******************************************************************
  * validParentPointers                                             *
  *                                                                 *
  * Purpose: Do the work of isValid to check parent pointers        *
  *          --- a recursive traversal to check that parents and    *
  *          children point at each other properly.                 *
  *                                                                 *
  * Parameter:                                                      *
  *    curr: The TwoThreeNode that the traversal is currently       *
  *         working on.                                             *
  *                                                                 *
  * Returns: True if curr and its children are properly pointing    *
  *          at each other (and the children and their children,    *
  *          and so on); false, otherwise.                          *
  *******************************************************************/
  private boolean validParentPointers( TwoThreeNode curr )
  {
    boolean valid = true;

    if ( (curr != null) && !curr.isLeaf() )
    {
      for ( int i = 0; ( i <= curr.numValues ) && valid; i++ )
      {
        if ( curr.child[i] == null )
        {
          System.out.println( "*** Error: interior node with "
            + (curr.numValues + 1) + " children is missing child"
            + "[" + i + "]" );
          valid = false;
        }
        else
        {
          valid = (curr.child[i].parent == curr) 
                  && validParentPointers( curr.child[i] );
        }
      } // end for
    } // end if curr is not null and not a leaf

    return valid;
  } // end validParentPointers


  /******************************************************************
  * validParentPointers                                             *
  *                                                                 *
  * Purpose: Do the work of isValid to check index values           *
  *          --- a recursive traversal to check that an index value *
  *          is properly chosen to guide searches to the children   *
  *          on either side of it:                                  *
  *          the maximum key in the subtree immediately to the left *
  *          of an index value                                      *
  *             < the index value <=                                *
  *          the minimum key in the subtree immediately to the      *
  *          right of the index value                               *
  *                                                                 *
  * Parameter:                                                      *
  *    curr: The TwoThreeNode that the traversal is currently       *
  *         working on.                                             *
  *                                                                 *
  * Returns: True if curr's and its descendant's index values are   *
  *          correct; false, otherwise.                             *
  *******************************************************************/
  public boolean validIndexValues( TwoThreeNode curr )
  {
    boolean valid = true;

    if ( (curr != null) && !curr.isLeaf() )
    {
      for ( int i = 0; ( i < curr.numValues ) && valid; i++ )
      {
        if ( curr.child[i] == null )
        {
          System.out.println( "*** Error: Index value " + curr.value[i]
            + " has a null child pointer immediately to its left" );
          valid = false;
     
        }
        if ( curr.value[i] <= maxValue( curr.child[i] ) )
        {
          System.out.println( "*** Error: Index value " + curr.value[i]
            + " is less than the max value "
            + maxValue( curr.child[i] )
            + " in the child immediately to its left" );
          valid = false;
        }
        else if ( curr.child[i+1] == null )
        {
          System.out.println( "*** Error: Index value " + curr.value[i]
            + " has a null child pointer immediately to its right" );
          valid = false;
     
        }
        else if ( minValue( curr.child[i+1] ) < curr.value[i] )
        {
          System.out.println( "*** Error: Index value " + curr.value[i]
            + " is greater than the min value "
            + minValue( curr.child[i+1] )
            + " in the child immediately to its right" );
          valid = false;
        }
      } // end for
    }  // end if curr is not null and not a leaf

    return valid;
  } // end validIndexValues


  // Helper method for validIndexValues:
  // Returns the minimum key stored in the descendants of curr,
  // which is stored in the leftmost leaf descendant.
  private int minValue( TwoThreeNode curr )
  {
    TwoThreeNode n;
    int result = Integer.MAX_VALUE;

    if ( curr != null )
    {
      n = curr;
      while ( !n.isLeaf() )
      {
        n = n.child[0]; // go to the leftmost child!
      }
      result = n.value[0];
    }

    return result;
  }


  // Helper method for validIndexValues:
  // Returns the maximum key stored in the descendants of curr,
  // which is stored in the rightmost leaf descendant.
  private int maxValue( TwoThreeNode curr )
  {
    TwoThreeNode n;
    int result = Integer.MIN_VALUE;

    if ( curr != null )
    {
      n = curr;
      while ( !n.isLeaf() )
      {
        n = n.child[n.numValues]; // go to the rightmost child!
      }
      result = n.value[0];
    }

    return result;
  }

} // end class TwoThreeTree
