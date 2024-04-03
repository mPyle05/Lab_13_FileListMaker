import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    // Static ArrayList to store strings
    static ArrayList<String> myArrList = new ArrayList<>();
    // Static Scanner to take input from the user
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        // Flag to control the loop
        boolean quit = false;
        String fileName = "";
        boolean needsToBeSaved = false;

        // Loop until the user decides to quit
        while (!quit) {
            // Print the menu and get user input
            view(false); // Print the list before showing the menu
            String menuPick = SafeInput.getRegExString(in, """
                    Menu:
                     Add new item (A)
                     Delete item (D)
                     Clear List (C)
                     Open a list file (O)
                     View list (V)
                     Save List (S)
                     Quit (Q)
                    """, "[AaDdVvQqOoSsCc]").toUpperCase();

            // Switch statement to perform actions based on user input
            switch (menuPick) {
                case "A":// Add a new item and triggers the needsToBeSaved flag
                    add();
                    needsToBeSaved = true;
                    break;
                case "D": // Print the list with item numbers before letting the user delete an item and triggers the needsToBeSaved flag
                    view(true);
                    delete();
                    needsToBeSaved = true;
                    break;
                case "V":
                    break; // Do nothing, continue to next iteration of the loop where the list will be printed at the beginning
                case "Q":
                    needsToBeSaved = needsToBeSaved(needsToBeSaved, fileName);
                    quit = quit(); // Quit the program
                    break;
                case "O": // Checks if the current file needs saving then opens a new file, where the first line is the file name
                    needsToBeSaved = needsToBeSaved(needsToBeSaved, fileName);
                    myArrList = NIOFileEditing.openFile();
                    fileName = myArrList.get(0).replace(".txt", "");
                    myArrList.remove(0);
                    break;
                case "C": //Clears the list and triggers the needsToBeSaved flag
                    myArrList = new ArrayList<>();
                    needsToBeSaved = true;
                    break;
                case "S": //Saves and resets the needsToBeSaved flag
                    needsToBeSaved = save(fileName);
                    break;
                default: // Error message for invalid input (should never happen)
                    System.out.print("Something went wrong");
                    break;
            }
        }
    }

    /**
     * Method to add a new item to the list
     */
    private static void add() {
        myArrList.add(SafeInput.getNonZeroLenString(in, "Please enter an item "));
    }

    /** Method to delete an item from the list
     * First checks if the list is empty
     * Then prompts user for item number to delete and remove it from the list
     */
    private static void delete() {

        if (myArrList.isEmpty()) {
            System.out.println("Error, cannot delete an empty list");
        } else {
            myArrList.remove(SafeInput.getRangedInt(in, "Enter an item number to delete", 0, myArrList.size()) - 1);
        }
    }

    /** Method to print the list
     * Checks to see if the list should be printed with item numbers
     * @param numbered Checks to see if the list should be numbered
     */
    private static void view(Boolean numbered) {
        if (numbered) {
            System.out.println("List:");
            // Iterate over the list and print each item with its number
            for (int i = 0; i < myArrList.size(); i++) {
                System.out.println(" " + (1 + i) + ". " + myArrList.get(i));
            }
        } else {
            // Print the list without item numbers
            System.out.println("List:");
            for (String s : myArrList) {
                System.out.println(" " + s);
            }
        }
    }

    /** Method to confirm if the user wants to quit
     *
     * @return true or false based on SafeInput Y/N confirm
     */
    private static boolean quit() {
        return SafeInput.getYNConfirm(in, "Are you sure you want to quit? (y/n)");
    }

    /** Method to save the list to a file
     * If no file name provided, prompts user for it
     * Writes the list to the file using NIOFileEditing
     * @param fileName Name of the file to save the list
     * @return false after saving
     */
    private static boolean save(String fileName) {
        if (fileName.isEmpty()) {
            fileName = SafeInput.getNonZeroLenString(in, "Please enter a file name");
        }
        NIOFileEditing.writeFile(myArrList, fileName);
        return false;
    }

    /** Method to check if the current list needs to be saved
     * If yes, prompts user to save
     * @param needsToBeSaved Boolean indicating if list needs to be saved
     * @param fileName Name of the file where list is saved
     * @return boolean indicating whether list needs to be saved
     */
    private static boolean needsToBeSaved(boolean needsToBeSaved, String fileName) {
        if (needsToBeSaved){
            // Prompt if user wants to save current list
            if (SafeInput.getYNConfirm(in, "Would you like to save the file?")) {
                save(fileName);
                needsToBeSaved = false;
            }
        }
        return  needsToBeSaved;
    }
}
