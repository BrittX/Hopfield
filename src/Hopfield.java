/**
 * Created by Brittany on 4/8/2017.
 */
import java.io.*;
import java.util.*;

public class Hopfield {
    //variables for Hopfield
    //String fName;
    int pairs;
    int dimensions;
    int[][][] samples;

    //Constructor
    public Hopfield(String fName) {
        try {
            List<Integer> dimpairs = readSome(fName);
            dimensions = dimpairs.get(0); //store first value as number of dimensions
            pairs = dimpairs.get(1); //second value as number of pairs
            samples = readIns(fName, pairs);
            //System.out.println(samples[3][1][1]);
        }
        catch (NullPointerException e) {
            System.out.println("\nYou entered an invalid file format");
            //e.printStackTrace();
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
    private int[][][] readIns(String inFile, int numPairs) {
        try {
            int count = 0, rows = 0, cols = 0, check = 0, curr = 0;
            BufferedReader br = new BufferedReader(new FileReader((inFile)));
            String lines;
            while((lines = br.readLine()) != null) {
                //Reading and storing training inputs
                if (count > 1) {
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
                count++; //increment count
            }
            br.close();
            BufferedReader brr = new BufferedReader(new FileReader(inFile));
            //now read and store in 2D matrix
            int[][][] samples = new int[numPairs][rows][cols];
            int index = 0;
            while((lines = brr.readLine()) != null) {
                if (curr > 2) {
                    if (lines.equals("")) {
                        index++;
                    }
                        for (int j = 0; j < rows; j++) {
                            for (int k = 0; k < lines.length(); k++) {
                                //Cycle through each character in line
                                if (lines.charAt(k) == 'O' || lines.charAt(k) == '0') {
                                    samples[index][j][k] = 1;
                                }
                                else if (lines.charAt(k) == ' ') {
                                    samples[index][j][k] = -1;
                                }
                            }
                        }
                }
                curr++;
            }
            brr.close();
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
    /*
    //Function to get weight matrix
    public double[][] weightMatrix(List<Integer> samples) {
        double [][]weightSamples = new double[this.pairs][this.dimensions];
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
        return weightSamples;
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
                    Hopfield hopfield = new Hopfield(trainFile);
                    //hopfield.weightMatrix(hopfield.tSamples);
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
