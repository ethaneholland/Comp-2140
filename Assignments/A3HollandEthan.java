/**
 * A3HollandEthan
 * 
 * COMP 2140 SECTION A01
 * INSTRUCTOR   Shahin Kamali 
 * ASSIGNMENT   Assignment 3
 * @author      Ethan Holland, 7865033
 * @version     Nov 2, 2021
 * 
 * PURPOSE: This program will ask for a text file input that contains Karon programs.
 *          Each Karon program will contain its own Dictionary of variables and their corresponding names and values,
 *          implemented by a hash table. Any errors caused by using a variable before its been declared 
 *          will be printed, then the full contents of the Karon programs variables will be printed.
 * 
 *          The first line of the text input will contain the number of Karon programs being run, 
 *          and a line containing only 'Q' will end the current Karon program and initiate the next Karon program instance.
 */

import java.io.*;
import java.util.Scanner;
import javax.swing.JFileChooser;


public class A3HollandEthan {
    public static void main( String[] args ) {
        System.out.println( "COMP 2140 Assignment 3: Executing Karon Programs" );
        executeKaronPrograms( chooseFile() ); // Run Karon programs found in the File that the user chooses.
        System.out.println( "Program ended normally." );
    } // end method main

    private static File chooseFile(){
        /**
         * PURPOSE: Uses JFileChooser for the user to return a chosen File.
         */
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory( new java.io.File(".") ); // Default to the directory that this program is in.
        chooser.setDialogTitle( "Choose a file with Karon programs to execute" );
        chooser.showOpenDialog( null );
        return chooser.getSelectedFile(); // Return the file that the user chose.
    } // end method getInput

    private static void executeKaronPrograms( File input ){
        /**
         * PURPOSE: Takes a text file containing Karon programs and runs the Karon programs contained.
         *          The first line determines the number of Karon programs that will be run.
         *          Each program executed will have a Karon instance created, and assignment lines 
         *          from the text file will be passed to the Karon instance. After passing assignments,
         *          the Karon instance will either update or insert the new variable data, print an error due to
         *          variables being used before being declared, or return a boolean to terminate the program
         *          if the assignment contains 'Q'. Variables stored will then be printed to the console.
         */
        try {
            BufferedReader karonReader = new BufferedReader( new FileReader( input ) );
            // First line of a valid text file will contain a single integer indicating how many Karon programs will be executed.
            int numOfPrograms = Integer.parseInt( karonReader.readLine() );
            // Loop through the number of Karon programs to be executed.
            for (int i=1; i <= numOfPrograms; i++){
                System.out.println( "\nKaron Program " + i );
                System.out.println( "----------------" );
                System.out.println( "Error Messages:\n" ); // Error messages will be printed by the karonProgram.
                Karon karonProgram = new Karon(); // New Karon program contains an empty Dictionary.
                boolean endProgram = false; // If endProgram is true, karonProgram has finished executing.
                // While there is still more assignments to execute for karonProgram.
                while (!endProgram){
                    // Pass the next line of the input text file to the karonProgram to compute.
                    String assignment = karonReader.readLine();
                    endProgram = karonProgram.executeAssignment( assignment );
                }
                // Before terminating the karonProgram, print the final contents of its variables.
                System.out.println( "\nFinal values of the variables:" );
                System.out.println( karonProgram );
            }
            karonReader.close();
        }
        catch ( NumberFormatException e ) {
            System.out.println( "Error: The chosen file does not state the number of karon programs to run." );
        }
        catch ( IOException e ) {
            System.out.println( "Error: The chosen file was invalid." );
        }
        catch ( NullPointerException e ) {
            System.out.println( "Error: File was improperly chosen." );
        }
    } // end method executeKaronPrograms

} // end class A3HollandEthan

class Karon {

    // Instance Variable(s)
    private Dictionary variableTable;

    public Karon() {
        variableTable = new Dictionary();
    } // end constructor Karon

    public boolean executeAssignment( String assignment ) {
        /**
         * PURPOSE: Takes a string assignment, if the assignment contains a 'Q' then 
         *          the program is finished and returns true to terminate, otherwise returns false.
         *          The assignment is checked for a declaration statement of two (right side of a '=' if it exists)
         *          and if the declaration contains valid variables and integers and operator, the computation is used
         *          to update/insert into the variable given on the left side of the '='.
         */
        boolean endProgram = false; // Return value, If 'Q' is found then there is no more assignments to run and true is returned.
        // Split the assignment by a '=',
        // If the assignment is split into 2 parts then the right side is computed to update the variable on the left side,
        // if the assignment is split into 1 part, then the assignment is checked for a 'Q' indicated the end of the program.
        String[] splitAssignment = assignment.split( "=" );
        if (splitAssignment.length == 2){
            try {
                // Compute the value of the right side of the assignment,
                // if a variable on the right side is used without existing in the variableTable,
                // then an UndeclaredVariableException is thrown and this assignment is finished.
                int variableValue = computeDeclaration( splitAssignment[1] ); 
                // Try updating the variable on the left side of the assignment with the new declaration value.
                boolean variableExists = variableTable.updateValue( splitAssignment[0].trim(), variableValue );
                // If the variable of the left side does not exist, add it to the variableTable with the new declaration value.
                if (!variableExists)
                    variableTable.insert( splitAssignment[0].trim(), variableValue );
            } 
            catch( UndeclaredVariableException e ){
                // Declaration statement on the right side of the assignment used a undeclared variable,
                // add to the error message the variable on the left side and print.
                System.out.println( String.format( e.getMessage(), splitAssignment[0] ) );
            }
        }
        else if (splitAssignment.length == 1){
            // If the assignment is a 'Q' then return true, indicating the end of the Karon program.
            if (splitAssignment[0].trim().equals( "Q" ) )
                endProgram = true;
        }
        return endProgram;
    } // end method executeAssignment

    private int computeDeclaration( String declaration ) throws UndeclaredVariableException {
        /**
         * PURPOSE: Takes a String declaration statement containing two variables or integers and an operator between them.
         *          If any variables haven't been declared into the variableTable then a UndeclaredVariableException is thrown.
         *          If both elements are valid and the operator is one of '+', '-', '*', '/' then the computation is returned.
         */
        int value = 0; // Output computation of the declaration statement.
        Scanner readAssignment = new Scanner( declaration );
        // Default operator is addition, as the first element of the declaration will be added to the output value.
        char operator = '+'; 
        // If the token is a variable or integer, its value is saved to tokenValue.
        int tokenValue = 0; 
        // While there is more tokens of the declaration to be processed.
        while (readAssignment.hasNext()){
            String token = readAssignment.next();
            switch (token.charAt( 0 )) {
                // If the token starts with a valid operator, change the operator used to process a tokenValue.
                case '+':
                case '-':
                case '*':
                case '/':
                    operator = token.charAt( 0 );
                    break;
                default:
                    // If the token starts with a digit, parse the tokens integer value and contain it in tokenValue.
                    if (Character.isDigit( token.charAt( 0 ) )) {
                        try { 
                            tokenValue = Integer.parseInt( token );
                        }
                        catch( NumberFormatException e ){
                            tokenValue = 0;
                        }
                    }
                    // If the token does not start with a digit, then it is a variable.
                    else {
                        // Search for a variable with the name matching token.
                        VariableRecord record = variableTable.search( token );
                        // If there exists a match, contain the variables value in tokenValue.
                        if (record != null){
                            tokenValue = record.getValue();
                        }
                        // If there is not match, throw a UndeclaredVariableException and terminate declaration.
                        else {
                            // Create the structure of the error message with the declaration and undeclared token variable,
                            // and a %s format so that the left side of the assignment can be added.
                            throw new UndeclaredVariableException( "Error: In the statement %s = " + declaration + ": " + token + " was used before being declared." );
                        }
                    }
                    // Token is not a operator, process the tokenValue into the total declaration value depending on the current operator stored. 
                    switch (operator){
                        case '+':
                            value += tokenValue;
                            break;
                        case '-':
                            value -= tokenValue;
                            break;
                        case '*':
                            value *= tokenValue;
                            break;
                        case '/':
                            value /= tokenValue;
                            break;
                    }
            }
        }
        readAssignment.close();
        return value;
    } // end method computeDeclaration

    public String toString() {
        /**
         * PURPOSE: return the contents of the variableTable Dictionary.
         */
        return "" + variableTable;
    } // end method toString

} // end class Karon

class Dictionary {

    // Class Constant(s)
    private static final int TABLE_SIZE = 79;
    private static final int PRIME_CONSTANT = 23;
    // Instance Variable(s)
    private Node[] hashTable;

    public Dictionary() {
        hashTable = new Node[TABLE_SIZE];
    } // end constructor Dictionary

    private static int hash( String variableName ) {
        /**
         * PURPOSE: Takes a String and returns an integer polynomial hash code, computed using Horner's method.
         */
        int hashCode = 0; // Return integer of computed polynomial hash code.
        // if the input has at least one character.
        if (variableName.length() > 0)
            // Add to the output the integer value of the first character mod the size of the table.
            hashCode = (int)variableName.charAt( 0 ) % TABLE_SIZE;
        // Loop through the characters after the first character.
        for (int i=1; i < variableName.length(); i++) {
            // Current hash code is multiplied by a contant prime number.
            hashCode *= PRIME_CONSTANT;
            // Next character's integer value is added to the hash code mod the size of the table.
            hashCode += (int)variableName.charAt( i );
            hashCode %= TABLE_SIZE;
        } 
        return hashCode;
    } // end method hash

    public VariableRecord search( String variableName ) {
        /**
         * PURPOSE: Takes a String name and checks every element of the Dictionary for a match.
         *          If a variable is found matching the input, then its corresponding VariableRecord is returned,
         *          otherwise a null is returned.
         */
        VariableRecord match = null; // Return value defaults to no match found.
        int hashCode = hash( variableName );
        // Start at the Node in the index of the hashTable where the variable would be stored.
        Node curr = hashTable[hashCode];
        // If the Node at the correct index of the hashTable is not empty.
        if (curr != null){
            boolean foundMatch = false;
            // While a match has not been found and there is another Node to check.
            while (!foundMatch && curr != null){
                // If the current Nodes variable name is the same as the input name, a match has been found.
                if (curr.getVariableRecord().getName().equals( variableName )) {
                    foundMatch = true;
                    match = curr.getVariableRecord();
                }
                curr = curr.getNext();
            }
        }
        return match;
    } // end method search

    public void insert( String variableName, int variableValue ) {
        /**
         * PURPOSE: Takes a new variable name and value and creates a new VariableRecord.
         *          A polynomial hash code is generated through hash() and is used to insert the VariableRecord
         *          at the end of the Linked List in the corresponding hashed index of the hashTable.
         */
        int hashCode = hash( variableName ); 
        // Start at the Node in the index of the hashTable where the variable will be stored.
        Node curr = hashTable[hashCode];
        // If the Node at the correct index of the hashTable is not empty.
        if ( curr != null ) {
            // Get the Node at the end of the Linked List.
            while (curr.getNext() != null)
                curr = curr.getNext();
            // Add the new variable to the end of the Linked List.
            curr.setNext( new Node( new VariableRecord( variableName, variableValue ), null ) );
        }
        // If the Linked List is empty, the new variable is the top Node.
        else 
            hashTable[hashCode] = new Node( new VariableRecord( variableName, variableValue ), null );
    } // end method insert

    public boolean updateValue( String variableName, int variableValue ) {
        /**
         * PURPOSE: Takes the name of a variable and the new value to change if the variable exists in the hashTable.
         *          If the variable exists and its VariableRecord is updated with the new value, then true is returned,
         *          otherwise the variable does not exists and returns false.
         */
        boolean variableExists = false; // Return value defaults to no match found and updated.
        VariableRecord record = search( variableName ); // Search for the VariableRecord corresponding to variableName.
        // If a VariableRecord was found, change the variables value to the new variableValue.
        if (record != null){
            record.setValue( variableValue );
            variableExists = true; // Match was found and updated.
        }
        return variableExists;
    } // end method updateValue

    public String toString() {
        /**
         * PURPOSE: Returns a String containing the name and value of every variable in the hashTable.
         *          Each variable is on its own line.
         */
        String output = "";
        // Loop through the elements of the hashTable.
        for (int i=0; i < TABLE_SIZE; i++){
            Node curr = hashTable[i];
            // If there exists a top Node at the current position in the hashTable.
            while (curr != null){
                // Add the variable's name and value on its own line of the output.
                VariableRecord record = curr.getVariableRecord();
                output += record.getName() + " = " + record.getValue() + "\n";
                curr = curr.getNext();
            }
        }
        return output;
    } // end method toString

} // end class Dictionary

class Node {

    // Instance Variable(s)
    private Node next;
    private VariableRecord record;

    public Node( VariableRecord newRecord, Node newNext) {
        record = newRecord;
        next = newNext;
    } // end constructor Node

    public VariableRecord getVariableRecord() { return record; } // end accessor method method getVariableRecord

    public Node getNext() { return next; } // end accessor method getNext

    public void setNext(Node newNext) { next = newNext; } // end mutator method setNext

} // end class Node

class VariableRecord {

    // Instance Variable(s)
    private String name;
    private int value;

    public VariableRecord( String newName, int newValue ) {
        name = newName;
        value = newValue;
    } // end constructor VariableRecord

    public String getName() { return name; } // end accessor method getName

    public int getValue() { return value; } // end accessor method getValue

    public void setName( String newName ) { name = newName; } // end mutator method setName

    public void setValue( int newValue ) { value = newValue; } // end mutator method setValue

} // end class VariableRecord

class UndeclaredVariableException extends Exception {

    // Instance Variable(s)
    private String message;

    public UndeclaredVariableException( String newMessage ){
        message = newMessage;
    } // end constructor UndeclaredVariableException

    public String getMessage(){ return message; } // end accessor method getMessage

} // end class UndeclaredVariableException

