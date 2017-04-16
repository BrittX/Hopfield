/**
 * Created by Brittany on 4/8/2017.
 */
import java.io.*;
import java.util.*;

public class Hopfield {
    //variables for Hopfield
    int pairs, dimensions, cols, rows;
    //int dimensions;
    int[][][] samples;


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

    //Function to read testing file and store contents in int array
    private int[][][] readIns(String inFile, int numPairs, int numRows, int numCols) {
        try {
            int curr = 0;
            int j = 0;
            BufferedReader br = new BufferedReader(new FileReader((inFile)));
            String lines;
            //now read and store in 2D matrix
            int[][][] samples = new int[numPairs][numRows][numCols];
            int index = 0;
            while((lines = br.readLine()) != null) {
                if (curr > 2) { //we're past the first 2 variables and the blank space
                    if (lines.equals("")) { //check if we've reached the end of the pair
                        index++; //increment which matrix we're storing
                    }
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
                    j++;
                }
                curr++;
            }
            br.close();
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
    //Function to return a transposed matrix
    private int[][][] transpose(int[][][] regular, int cols, int rows, int pairs) {
        int[][][] transposed = new int[pairs][rows][cols];
        int index = 0;
        for (index = 0; index < pairs; index++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < rows; k++) {
                    System.out.println("Index: " + index + " j: " + j + " k: " + k);
                    transposed[index][j][k] = regular[index][k][j];
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                System.out.print(transposed[4][i][j] + " ");
            }
            System.out.println();
        }
        return transposed;
    }
    /*
    //Function to get weight matrix
    public int[][] weightMatrix(int[][][] samples, int numPairs) {
        int [][] weights; //for final weight matrix to return
        //System.out.println(weightSamples[i][j]);
        int j, r = 0;

        for(j = 0; j < samples.size(); j++) {
            for (int i = 0; i < this.pairs; i++) { //for the weightSample[i][]
                //System.out.println(j);
                weightSamples[i][j] = samples.get(j);
                //System.out.println(weightSamples[i][j]);
                if (j == this.dimensions - 1) {
                    System.out.println("we;re here");
                    j+=1;
                }
            }
        }
        return weights;
    }
    */

    public static void main(String[] args) {
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

                if (choice == 1) {
                    System.out.println("\nPlease enter the name of your training sample file: ");
                    System.out.print(">>> ");
                    String trainFile = scan.next();
                    Hopfield hopfield = new Hopfield(trainFile); //initialize Hopfield
                    //hopfield.weightMatrix(hopfield.samples, hopfield.pairs);
                    hopfield.transpose(hopfield.samples, hopfield.cols, hopfield.rows,hopfield.pairs);
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
