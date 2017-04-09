/**
 * Created by siber on 4/8/2017.
 */
import java.io.*;
import java.util.*;

public class Hopfield {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean quit = false;
        System.out.println("Welcome to my Discrete Hopfield Neural Network!");

        while(!quit) {
            //Menu
            System.out.println("Please choose from one of the options below\n");
            System.out.println("1. Train using a training image file");
            System.out.println("2. Test using a trained weight file");
            System.out.println("3. Exit\n");

            //get user input
            try {
                Integer choice = scan.nextInt();

                if (choice == 1) {
                    System.out.println("Please enter the name of your training sample file:  ");
                    String trainFile = scan.next();
                    List<Integer> trainIns = new Hopfield().readIns(trainFile);
                    System.out.println(trainIns);
                }

                if (choice == 3) {
                    System.out.println("Goodbye!");
                    quit = true;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter an integer menu option");
                e.printStackTrace();
            }
        }
        System.exit(1);
    }

    //Function to read testing file and store contents in int array
    private List<Integer> readIns(String inFile) {
        List<Integer> inVals = new ArrayList<Integer>();

        try {
            int count = 0;
            BufferedReader br = new BufferedReader(new FileReader((inFile)));
            String lines;
            while((lines = br.readLine()) != null) {
                //Reading and storing training inputs
                if (count > 1) {
                    //Check if line itself is new line
                    if (lines.equals("")) {
                        continue;
                    }
                    for (int i = 0; i < lines.length(); i++) {
                        //Cycle through each character in line
                        if (lines.charAt(i) == 'O' || lines.charAt(i) == '0') {
                            inVals.add(1);
                        }
                        if (lines.charAt(i) == ' ') {
                            inVals.add(-1);
                        }
                    }
                }
                else {
                    //Reading first two lines so just store in array
                    inVals.add(Integer.parseInt(lines));
                }
                count++; //increment count
            }
            br.close();
            return inVals;
        }
        catch (FileNotFoundException e) {
            System.out.println("File inputted is invalid");
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
