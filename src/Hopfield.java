/**
 * Created by Brittany on 4/8/2017.
 */
import java.io.*;
import java.util.*;
import java.lang.*;

public class Hopfield {
    //variables for Hopfield
    private int pairs, dimensions, cols, rows;
    //int dimensions;
    private int[][][] samples;


    //Constructor
    public Hopfield(String fName) {
        try {
            List<Integer> dimpairs = readSome(fName);
            dimensions = dimpairs.get(0); //store first value as number of dimensions
            pairs = dimpairs.get(1); //second value as number of pairs
            rows = dimpairs.get(2);
            cols = dimpairs.get(3);
            samples = readIns(fName, pairs, rows, cols);
        }
        catch (NullPointerException e) {
            System.out.println("\nYou entered an invalid file format");
            System.exit(0);
        }
    }
    public Hopfield(String fName, int option) {
        readTests(fName);
    }
    /*
    Function to read the first two values of the training sample
    returns a list of integers with the first value being the # of dimensions
        and the second being the number of pairs we'll see
     */
    private List<Integer> readSome(String inFile) {
        List<Integer> inVals = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader((inFile)));
            //get number of dimensions
            inVals.add(Integer.parseInt(br.readLine()));
            //get number of pairs
            inVals.add(Integer.parseInt(br.readLine()));
            int check = 0, rows = 0, cols = 0;
            String lines;
            while((lines = br.readLine()) != null) {
                //Reading and storing training inputs
                    //Check if line itself is new line
                    if (lines.equals("")) {
                        check += 1;
                        if (check == 2) {
                            break;
                        }
                        continue;
                    }
                    rows+=1; //increment number of rows
                    cols = lines.length(); //store number of columns in each training pair
            }
            br.close();
            inVals.add(rows);
            inVals.add(cols);
            return inVals;
        }
        catch (FileNotFoundException e) {
            System.out.println("Please input an existing file");
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Function to read testing file and store contents in int array WORKS
    private int[][][] readIns(String inFile, int numPairs, int numRows, int numCols) {
        try {
            int curr = 0;
            int j = 0;
            BufferedReader br = new BufferedReader(new FileReader((inFile)));
            String lines;
            //now read and store in 3D matrix
            int[][][] samples = new int[numPairs][numRows][numCols];
            int index = 0; //for which training pair we're on
            while((lines = br.readLine()) != null) {
                if (curr > 2) { //we're past the first 2 variables and the blank space
                    if (lines.equals("")) { //check if we've reached the end of the pair
                        index++; //increment which matrix we're storing
                    }
                    System.out.println("This is lines.length: " + lines.length());
                    for (int k = 0; k < lines.length(); k++) {
                        //Cycle through each character in line
                        if (lines.charAt(k) == 'O' || lines.charAt(k) == '0') {
                            samples[index][j][k] = 1;
                        } else if (Character.isWhitespace(lines.charAt(k))) {
                            samples[index][j][k] = -1;
                        }
                    }
                    if (j == numRows) { //check if we've finished the last row in the pair
                        j = 0; //reset j and don't increment
                        continue;
                    }
                    else { //j < numrows
                        j++;
                    }
                }
                curr++;
            }
            br.close();
//            for (int i = 0; i < rows; i++) {
//                for (j = 0; j < cols; j++) {
//                    System.out.print(samples[0][i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
            return samples;
        }
        catch (FileNotFoundException e) {
            System.out.println("\nFile inputted is invalid");
            e.printStackTrace();
            return null;
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("\nFile is not of correct format");
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //Function to return a transposed matrix WORKS
    private int[][][] transpose(int[][][] regular, int cols, int rows, int pairs) {
        int[][][] transposed = new int[pairs][rows][cols];
        int index;
        for (index = 0; index < pairs; index++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < rows; k++) {
                    transposed[index][j][k] = regular[index][k][j];
                }
            }
        }
        return transposed;
    }


    //Function to get multiplied matrices (s1 * s1T, ...) WORKS
    public int[][][] multMatrix(int[][][] samples, int numPairs, int[][][] transposed) throws IllegalAccessException {
        //get transposed matrix
        System.out.println(samples[0].length);
        int[][][] weights = new int[numPairs][samples[0].length][transposed[0][0].length]; //for final weight matrix to return
        int i, j, k;
        //Make sure each sample row and transposed column of same length
        for (int matrix = 0; matrix < numPairs; matrix++) {
            if (samples[matrix].length != transposed[matrix][0].length) {
                throw new IllegalAccessException("The number of rows from the original matrix" +
                        "need to match the number of columns from the transposed matrix");
            }
            //assume both are equal
            for (i = 0; i < samples[matrix].length; i++) { //cycle through rows of samples
                for (j = 0; j < transposed[matrix][0].length; j++) {
                    for (k = 0; k < samples[matrix][0].length; k++) {
                        weights[matrix][i][j] += samples[matrix][i][k] * transposed[matrix][k][j];
                    }
                }
            }
        }
        return weights;
    }

    //Function to add the matrices to get one 2D matrix WORKING
    private int[][] finalMatrix(int[][][] mult, String outFile) {
        int[][] finalWeights = new int[rows][cols]; //to store final weight matrix
        int i, j;

        for(int index = 0; index < pairs; index++) {
            for(i = 0; i < rows; i++) {
                for(j = 0; j < cols; j++) {
                    if(i == j) { //No self connections
                        finalWeights[i][j] = 0;
                    }
                    else
                        finalWeights[i][j] += mult[index][i][j];
                }
            }
        }
        //Write to output file
        try {
            PrintWriter w = new PrintWriter(outFile);
            for (i = 0; i < rows; i++) {
                for (j = 0; j < cols; j++) {
                    w.print(finalWeights[i][j] + " ");
                }
                w.println();
            }
            w.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Can't find that file");
            e.printStackTrace();
            return null;
        }
        return finalWeights;
    }
    //Function to return the activation y
    private int activation(int y_in) {
        //Checks if y_in is > 0, < 0 or == 0
        return y_in >= 0 ? (y_in == 0 ? y_in: 1) : -1;
    }

    //Redundant function to read testing inputs
    private int[][][] readTests(String testFile) {
        try {
            List<Integer> testVals = new ArrayList<>();
            int curr = 0;
            int j = 0;
            int count = 0;
            BufferedReader br = new BufferedReader(new FileReader((testFile)));
            String lines;
            //get number of dimensions
            testVals.add(Integer.parseInt(br.readLine()));
            //get number of testing pairs
            testVals.add(Integer.parseInt(br.readLine()));
            //now read and store in 3D matrix
            int[][][] testins = new int[testVals.get(1)][][];
            int index = 0; //for which training pair we're on
            while ((lines = br.readLine()) != null) {
                if (curr > 2) {
                    for (int k = 0; k < lines.length(); k++) {
                        count++;
                        System.out.println(count);
                        //Cycle through each character in line
                        if (lines.charAt(k) == 'O' || lines.charAt(k) == '0') {
                            testins[index][j][k] = 1;
                        } else if (Character.isWhitespace(lines.charAt(k))) {
                            testins[index][j][k] = -1;
                        }

                    } if (j == lines.length()) {
                        j = 0;
                        continue;
                    }
                    else j++;
                }
                curr++;
                if (count == testVals.get(0)) {
                    index++;
                }
            }
            br.close();
            return testins;
        } catch (FileNotFoundException e) {
            System.out.println("File chosen does not exist");
            return null;
        } catch (IOException e) {
            System.out.println("Invalid file type");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
        Scanner scan = new Scanner(System.in);
        boolean quit = false;
        System.out.println("Welcome to my Discrete Hopfield Neural Network!");

        while(!quit) {
            //Menu
            System.out.println("\nPlease choose from one of the options below\n");
            System.out.println("1. Train using a training image file");
            System.out.println("2. Test using a trained weight file");
            System.out.println("3. Exit\n");

            //get user input
            try {
                System.out.print(">>> ");
                Integer choice = scan.nextInt();
                //Training
                if (choice == 1) {
                    System.out.println("\nPlease enter the name of your training sample file: ");
                    System.out.print(">>> ");
                    String inFile = scan.next();
                    System.out.println("\nPlease enter the filename to save trained weights: ");
                    System.out.print(">>> ");
                    String trainFile = scan.next();
                    Hopfield hopfield = new Hopfield(inFile); //initialize Hopfield
                    //get transposed matrix
                    int[][][] transposed = hopfield.transpose(hopfield.samples, hopfield.cols, hopfield.rows,hopfield.pairs);
                    //multiply matrices
                    int[][][] multMatrix = hopfield.multMatrix(hopfield.samples, hopfield.pairs, transposed);
                    //adding matrices to get one 2D matrix
                    hopfield.finalMatrix(multMatrix, trainFile);
                }
                //Testing
                if (choice == 2) {
                    System.out.println("\nPlease enter a testing sample file: ");
                    System.out.print(">>> ");
                    String testIns = scan.next();
                    System.out.println("\nPlease enter the name of a saved weights file to use: ");
                    System.out.print(">>> ");
                    String trained = scan.next();
                    System.out.println("\nEnter the max number of training epochs: ");
                    int epochs = scan.nextInt();
                    Hopfield testHop = new Hopfield(testIns, 2);
                    //testHop.readTests(testIns);

                }
                if (choice == 3) {
                    System.out.println("Goodbye!\n");
                    quit = true;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter an integer menu option");
                e.printStackTrace();
                break; //leave the loop for now
            }
        }
        System.exit(1);
    }


}
