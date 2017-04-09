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
}
