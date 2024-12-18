// Priyanka Kulkarni
// 8/16/2024
// CSE 123
// P3: JoeFeed Quiz
// TAs: Sahej Kocchar and Nicole Wang
import java.util.*;
import java.io.*;

// This class creates a BuzzFeed like quiz called a JoeFeed Quiz. It allows you to take
// the quiz, add questions to it, and export it to an external file.
public class QuizTree {
    private QuizTreeNode overallRoot;

    // This constructor establishes a JoeFeed quiz. It takes inputFile (the quiz) as 
    // a parameter
    public QuizTree(Scanner inputFile) {
        this.overallRoot = makeTree(inputFile);
    }

    // Behavior:
    //  - This method creates a JoeFeed quiz
    // Returns:
    //  - QuizTreeNode: the final quiz
    // Parameters:
    //  - inputFile: the quiz being read from an external file
    private QuizTreeNode makeTree(Scanner inputFile) {
        if(!inputFile.hasNextLine()) {
            return null;
        }

        String curr = inputFile.nextLine();
        if(curr.contains("END")) {
            return new QuizTreeNode(curr);
        }
        else if (curr.contains("/")) {
            QuizTreeNode node = new QuizTreeNode(curr);
            node.setLeft(makeTree(inputFile));
            node.setRight(makeTree(inputFile));
            return node;
        }

        return null;
    }
    
    // Behavior:
    //  - This method allows you to take the quiz 
    // Parameters:
    //  - console: the user's answers to the quiz 
    public void takeQuiz(Scanner console) {
        QuizTreeNode curr = overallRoot;
        quiz(console, curr);
    }

    // Behavior:
    //  - This method allows you to take the quiz 
    // Parameters:
    //  - console: the user's answers to the quiz 
    //  - node: quiz question options
    private void quiz(Scanner console, QuizTreeNode node) {
        QuizTreeNode curr = node;
        String data = curr.data;
        if(!curr.isLeaf()) {
            int slash = data.indexOf("/");
            System.out.print("Do you prefer " + data.substring(0,slash) + " or " +
                             data.substring(slash +1) + "? ");
            String answer = console.nextLine();
            if(answer.equalsIgnoreCase(data.substring(0,slash))) {
                quiz(console, curr.left);
            }
            else if(answer.equalsIgnoreCase(data.substring(slash +1))) {
                quiz(console, curr.right);
            }
            else {
                System.out.print("  Invalid response; try again.");
                quiz(console, curr);
            }
        }

        if(curr.isLeaf()) {
            System.out.println("Your result is: " + curr.data.substring("END:".length()));
        }
    }

    // Behavior:
    //  - This method exports the JoeFeed quiz to an external file
    // Returns:
    //  - PrintStream: the exported JoeFeed quiz
    // Parameters:
    //  - outputFile: the name of the file the JoeFeed quiz is being exported to
    public PrintStream export(PrintStream outputFile) {
        return export(outputFile, overallRoot);
    }

    // Behavior:
    //  - This method exports the JoeFeed quiz to an external file
    // Returns:
    //  - PrintStream: the exported JoeFeed quiz
    // Parameters:
    //  - outputFile: the name of the file the JoeFeed quiz is being exported to
    //  - node: the established quiz tree 
    private PrintStream export(PrintStream outputFile, QuizTreeNode node) {
        if(node.left == null && node.right == null) {
            outputFile.println(node.data);
        }
        else {
            outputFile.println(node.data);
            export(outputFile, node.getLeft());
            export(outputFile, node.getRight());
        }
        return outputFile;
    }

    // Behavior:
    //  - This method allows you to replace an answer to the JoeFeed quiz with a new
    //    question
    // Parameters:
    //  - toReplace: the answer you'd like to replace with a new question
    //  - leftChoice: one of the new question answer options
    //  - rightChoice: the other new question answer option
    //  - leftResult: an answer to the new question
    //  - rightResult: an answer to the new question 
    public void addQuestion(String toReplace, String leftChoice, String rightChoice, 
                            String leftResult, String rightResult) {
        QuizTreeNode top = this.overallRoot;
        String s = leftChoice + "/" + rightChoice;
        QuizTreeNode leftNode = new QuizTreeNode("END: " + leftResult);
        QuizTreeNode rightNode = new QuizTreeNode("END: " + rightResult);
        findNode(toReplace, s, top,leftNode, rightNode);

    }

    // Behavior:
    //  - This method allows you to replace an answer to the JoeFeed quiz with a new
    //    question
    // Parameters:
    //  - toReplace: the answer you'd like to replace with a new question
    //  - newValue: the new question 
    //  - curr: the question you're currently on 
    //  - left: an answer to the new question
    //  - right: an answer to the new question 
    private void findNode(String toReplace, String newValue, QuizTreeNode curr, QuizTreeNode left,
                          QuizTreeNode right) {
        if(curr != null && curr.data.toLowerCase().contains(toReplace.toLowerCase())) {   
            curr.data = newValue; 
            curr.setLeft(left);
            curr.setRight(right);
        }
        else if ( curr != null) {
            findNode(toReplace, newValue, curr.getLeft(), left, right);
            findNode(toReplace, newValue, curr.getRight(), left, right);
        }

    }

    // This class establishes a QuizTreeNode- a place where JoeFeed questions can be housed
    public static class QuizTreeNode {
        public String data;
        public QuizTreeNode left;
        public QuizTreeNode right;

        // This constructor establishes a QuizTreeNode with some data held inside of it
        // as a String 
        public QuizTreeNode(String data) {
            this(data, null, null);
        }

        // This constructor establishes a QuizTreeNode with data held inside it, that also has
        // a QuizTreeNode on its left and its right
        public QuizTreeNode(String data, QuizTreeNode left, QuizTreeNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        // This method determines if a QuizTreeNode is a leaf or not. Returns a boolean 
        // (true if the node is a leaf, false if it isn't)
        public boolean isLeaf() {
            if(this.left == null && this.right == null) {
                return true;
            }
            return false;
        }

        // This method returns a QuizTreeNode that is to the left of another
        public QuizTreeNode getLeft() {
            return left;
        }

        // This method returns a QuizTreeNode that is to the right of another
        public QuizTreeNode getRight() {
            return right;
        }

        // This method sets a QuizTreeNode to the left of another
        public void setLeft(QuizTreeNode left) {
            this.left = left;
        }

        // This method sets a QuizTreeNode to the left of another
        public void setRight(QuizTreeNode right) {
            this.right = right;
        }
    }
}
