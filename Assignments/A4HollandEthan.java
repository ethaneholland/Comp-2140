/**
 * A4HollandEthan
 * 
 * COMP 2140 SECTION A01
 * INSTRUCTOR   Shahin Kamali 
 * ASSIGNMENT   Assignment 4
 * @author      Ethan Holland, 7865033
 * @version     Nov 23, 2021
 * 
 * PURPOSE: Opens a dialog with the user to play rounds of 20 Questions until the user quits the game.
 *          A binary decision tree is used to store questions in the internal nodes and answers in the leaf nodes.
 *          The user is prompted each question and the 'yes' answers are stored in the left branch and the 'no' answers
 *          are stored in the right branch. When a wrong answer is guessed the user is asked for the correct answer and a 
 *          question that differentiates the wrong answer and the correct answer. The binary tree gets larger every round that
 *          it guesses incorrectly.
 *          The user can import a decision tree from an existing save file and save the current decision tree to a new save file.
 */

// IMPORTS
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

class A4HollandEthan {
    public static void main( String[] args ) {
        // Introduction to processing.
        JOptionPane.showMessageDialog( null, "Let's play 20 Questions!" );
        // Play rounds of 20 Questions until the user quits.
        play20Questions();
        // End of processing.
        JOptionPane.showMessageDialog( null, "20 Questions ends." );
    } // end method main

    private static void play20Questions(){
        /** 
         * PURPOSE: Uses class Questioner to play rounds of 20 Questions until the user wants to quit.
         *          After every wrong guess the decision tree is updated with the correct answer and a unique question
         *          to differentiate it from the other data. The user can import question and answer data from an exist
         *          file and after ending the game can save the new decision tree to a save file.
         */
        boolean keepPlaying = true; // Keep playing rounds of 20 Questions.
        Questioner game = new Questioner(); // New instance of 20 Questions.
        importData( game ); // Import a binary tree from an existing
        // While the user wants to play another round.
        while (keepPlaying){
            game.playRound(); // Play a single round of 20 Questions.
            // Ask if the user wants to play again.
            int answer = JOptionPane.showConfirmDialog( null, "I get better everytime I play, do you want to play again?", "20 Questions", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
            if (answer == JOptionPane.NO_OPTION)
                keepPlaying = false;
        }
        saveData( game ); // Save the current binary tree to a new file.
    } // end method play20Questions

    private static void importData( Questioner game ) {
        /** 
         * PURPOSE: Ask whether the user wants to start the game with an existing binary tree from a save file,
         *          otherwise the default Questioner binary tree will continue to be used.
         */ 
        int importTree = JOptionPane.showConfirmDialog( null, "Would you like to import knowledge from a file?", "20 Questions", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
        if (importTree == JOptionPane.YES_OPTION)
            game.readTree();
    } // end method importData

    private static void saveData( Questioner game ) {
        // PURPOSE: Ask whether the user wants to save the current games binary tree to a new text file.
        int saveTree = JOptionPane.showConfirmDialog( null, "Would you like to save this knowledge to a file?", "20 Questions", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
        if (saveTree == JOptionPane.YES_OPTION)
            game.writeTree();
    } // end method saveData

} // end class A4HollandEthan

class Questioner {

    // INSTANCE VARIABLE(S)
    private DTNode root; // Top of the binary tree.

    public Questioner(){
        // PURPOSE: Construct a Questioner instance with a simple default decision tree.
        // Answer data stored in the leaves.
        DTNode human = new DTNode( "Human" );
        DTNode shark = new DTNode( "Shark" );
        DTNode carrot = new DTNode( "Carrot" );
        DTNode diamond = new DTNode( "Diamond" );
        // Question data stored in the inner nodes.
        DTNode mammal = new DTNode( "Is it a mammal?", human, shark );
        DTNode plant = new DTNode( "Is it a plant?", carrot, diamond );
        // Root points to the first question in the tree.
        root = new DTNode( "Is it an animal?", mammal, plant );
    } // end constructor Questioner

    public void playRound() {
        /** 
         * PURPOSE: Moves through the tree by prompting the user to answer the yes/no questions at each inner node,
         *          and when a leaf is found the user confirms whether the answer stored is correct.
         *          If the answer is incorrect then method insertNewQuestion() is used to insert the correct answer into the tree.
         */
        DTNode curr = root; // Current node being looked at, start at the root node.
        int currAnswer; // Most recent user answer after being prompted the item in the current node.
        // While the current node being looked at is an internal node.
        while (!curr.isLeaf()) {
            // Save the user's yes or no answer from the prompted question stored in the item of the current node.
            currAnswer = JOptionPane.showConfirmDialog( null, curr.item, "20 Questions", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
            // If the question is true,
            if (currAnswer == JOptionPane.YES_OPTION) {
                // Move to the left node.
                curr = curr.left;
            }
            // If the question is false,
            else {
                // Move to the right node.
                curr = curr.right;
            }
        }
        // The curr node is a leaf,
        // store the user's yes or no answer after asking if the item of the current node is a correct guess.
        currAnswer = JOptionPane.showConfirmDialog( null, "Are you thinking of a/an " + curr.item, "20 Questions", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
        // If the guess is correct,
        if (currAnswer == JOptionPane.YES_OPTION) {
            // output victory message.
            JOptionPane.showMessageDialog( null, "I guessed right!" );
        }
        // If the guess is incorrect,
        else {
            // add this new object to the binary tree.
            insertNewQuestion( curr );
        }
    } // end method playRound

    private void insertNewQuestion( DTNode curr ) {
        /** 
         * PURPOSE: Prompts the user for the correct answer and a question that diffrentiates
         *          the correct answer from the incorrect answer, then modifies the tree to contain the new
         *          inner node question that has two leaf children containing the correct and incorrect answers.
         */
        // Ask for the new object not yes added.
        String correctAnswer = JOptionPane.showInputDialog( "What was your answer?" );
        // Ask for a question that differentiates the new object from the other objects.
        String innerNodeQuestion = JOptionPane.showInputDialog( "Provide a YES/NO question that would distinguish " + curr.item + "(YES answer) from " + correctAnswer + "(NO answer)" );
        // Insert the new question to new inner node.
        curr.item = innerNodeQuestion;
        // The incorrect object will be the guess made when the answer to innerNodeQuestion is yes.
        curr.left = new DTNode( curr.item );
        // The new object will be the guess made when the answer to innerNodeQuestion is no.
        curr.right = new DTNode( correctAnswer );  
    } // end method insertNewQuestion

    public void writeTree() {
        /** 
         * PURPOSE: Creates a new file containing the contents of the current decision tree,
         *          the user is prompted what to name this file and what directory to save it to.
         *          Uses a recursive helper method to move through the decision tree to add its contents to the new file.
         */
        // Prompt where to store the new save file.
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory( new java.io.File( "." ) ); // Default to the directory that this program is in.
        chooser.showSaveDialog( null );
        try {
            PrintWriter writeFile = new PrintWriter( chooser.getSelectedFile() );
            // Recursively move through the binary tree to copy data to the new save file.
            writeTree( writeFile, root );
            writeFile.close();
        } catch( FileNotFoundException e ){
            JOptionPane.showMessageDialog( null, "Error with the new save file. Process ends." );
            System.exit( 0 );
        }
    } // end driver method writeTree

    private void writeTree( PrintWriter writeFile, DTNode curr ) {
        /** 
         * PURPOSE: Recursive helper method that copies the item contents of the current DTNode to the saveFile.
         *          If the current Node is not a leaf, then recursively copy the contents of the NDTNodes
         *          to the left and the right of the current Node. Recursion ends when all leaves have been visited.
         */
        // Format the current Nodes item with a '<'.
        writeFile.println( "< " + curr.item );
        // If the current node is a internal node.
        if (!curr.isLeaf()){
            // Recursively copy the left and right branches to the save file.
            writeTree( writeFile, curr.left );
            writeTree( writeFile, curr.right );
            // Format the save file with a '>' after the current item, and the left and right branches have been handled.
            writeFile.println( ">" );
        }
        // If the current node is a leaf.
        else {
            // Format the save file with a '>' after the current item has been handled.
            writeFile.println( ">" );
        }
    } // end recursive helper method writeTree

    public void readTree() {
        /** 
         * PURPOSE: Prompts the user for an existing save file of a decision tree to copy into 
         *          this instances tree.Uses a recursive helper method to move through lines of the save file to copy.
         */
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory( new java.io.File( "." ) ); // Default to the directory that this program is in.
        chooser.setDialogTitle( "Choose a file with Karon programs to execute" );
        chooser.showOpenDialog( null );
        try{
            Scanner scanFile = new Scanner( chooser.getSelectedFile() );
            root = readTree( scanFile );
        } catch( FileNotFoundException e ){
            JOptionPane.showMessageDialog( null, "Error with the chosen file. Process ends." );
            System.exit( 0 );
        }
    } // end driver method readTree

    private DTNode readTree( Scanner scanFile ) {
        /** 
         * PURPOSE: Recursive helper method that scans the file and adds branches to the instances decision tree.
         *          If the next line starts with a "<" then a new DTNode is created with the following item, and
         *          the left and right DTNodes are found recursively. A pointer to the new DTNode is returned, 
         *          if the next line starts with a ">" then this spot in the decision tree should be void and will be returned. 
         */
        DTNode curr;
        // If the next character in the save file is a '<' then there is a new node in the current branch.
        if (scanFile.hasNext( "<" )){
            // Process the '<' character.
            scanFile.next(); 
            // Copy the item contents listed after the '<' character.
            String item = scanFile.nextLine();
            // Recursively process the left and right nodes of the current node
            // if the next line is a '>' then null is returned to the left and right pointers,
            // therefore there is no children and the branch ends.
            DTNode left = readTree( scanFile );
            DTNode right = readTree( scanFile );
            // Data for this node and its children are processed, 
            // process the remaining line containing '>'.
            scanFile.nextLine();
            // Return the new Node with its left and right branches.
            curr = new DTNode( item, left, right );
        }
        // If the next character in the save file is a '>' then there is no new nodes in the current branch.
        else {
            // Return null since there is no next node.
            curr = null;
        }
        return curr;
    } // end recursive helper method readTree

    private class DTNode {

        // INSTANCE VARIABLE(S)
        public String item; // Contents of the node.
        public DTNode left; // Left child branch.
        public DTNode right; // Right child branch.

        public DTNode( String newItem ) {
            // PURPOSE: Instantiate a new leaf node containing a String item.
            item = newItem;
            left = null;
            right = null;
        } // end constructor DTNode

        public DTNode( String newItem, DTNode newLeft, DTNode newRight ) {
            // PURPOSE: Instantiate a new internal node containing a String item and left and right children.
            item = newItem;
            left = newLeft;
            right = newRight;
        } // end constructor DTNode

        public boolean isLeaf() { 
            // PURPOSE: Returns true if this node has no children.
            // Since a DTNode can only have no children or two children, only one child pointer is checked.
            return left == null;
        } // end method isLeaf

    } // end class DTNode

} // end class Questioner