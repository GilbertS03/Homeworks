//Gilbert Salazar
//HW1
//This program creates a class object of type MorseTranslator and populates its member variables (parallel arrayLists)
//with letters of the alphabet, or the corresponding morse code. The user may input morse code or a phrase, and it will
//translate it from english to morse code, or morse code to english.
import java.util.*;
import java.io.*;

class MorseTranslator{
    private ArrayList<Character> letter;
    private ArrayList<String> morse;
    private static MorseTranslator uniqueInstance;

    //private method that searches for the letter to get index in ArrayList
    private int findLetterIdx(char searchLetter){
        boolean found = false;
        int i = 0;
        while(i < letter.size() && !found){
            if(letter.get(i) == searchLetter)
                found = true;
            else
                i++;
        }
        if (found)
            return i;
        else
            return -1;
    }

    //private method that searches for the morse to get index in ArrayList
    private int findMorseIdx(String searchMorse){
        boolean found = false;
        int i = 0;
        while(i < letter.size() && !found){
            if(morse.get(i).equals(searchMorse))
                found = true;
            else
                i++;
        }
        if (found)
            return i;
        else
            return -1;
    }

    private MorseTranslator(){
        letter = new ArrayList<>();
        morse = new ArrayList<>();
    }

    //Since they're parallel arrayLists they have to have the same amount of items, hence one argument
    private MorseTranslator(int numItems){
        letter = new ArrayList<>(numItems);
        morse = new ArrayList<>(numItems);
    }

    public static MorseTranslator getInstance(){
        if (uniqueInstance == null)
            uniqueInstance = new MorseTranslator();
        return uniqueInstance;
    }

    public static MorseTranslator getInstance(int size){
        if (uniqueInstance == null)
            uniqueInstance = new MorseTranslator(size);
        return uniqueInstance;
    }

    //adds item to both letter and morse to remain parallel
    public void addCharMorse(char addLetter, String addMorse){
        letter.add(addLetter);
        morse.add(addMorse);
    }

    //removes item from both letter and morse to remain parallel, finds letter index
    public boolean removeCharMorse(char removedLetter){
        int idx = findLetterIdx(removedLetter);
        if(idx >= 0 && idx <= letter.size()){
            letter.remove(idx);
            morse.remove(idx);
            return true;
        }
        else
            return false;
    }

    //removes item from both letter and morse to remain parallel, finds morse index
    public boolean removeMorseChar(String removedMorse){
        int idx = findMorseIdx(removedMorse);
        if(idx >= 0 && idx <= letter.size()){
            letter.remove(idx);
            morse.remove(idx);
            return true;
        }
        else
            return false;
    }

    public boolean modifyMorse(String oldMorse, String newMorse){
        int idx = findMorseIdx(oldMorse);
        if(idx >= 0 && idx < morse.size()){
            morse.set(idx, newMorse);
            return true;
        }
        else
            return false;
    }
    //Maybe modify morse by finding the letter if that's what they want too


    public char getLetter(int idx){
        if(idx >= 0 && idx < letter.size())
            return letter.get(idx);
        else
            return 0;
    }

    public String getMorse(int idx){
        if(idx >= 0 && idx < morse.size())
            return morse.get(idx);
        else
            return "";
    }

    //Finds the letter input by user and takes the index of the letter and uses the parallel arrayList structure to
    //change the letter into morse
    public String findLetterToMorse(char findLetters){
        int idx = findLetterIdx(findLetters);
        if(idx == -1){
            return "";
        }
        else
            return morse.get(idx);
    }

    //Finds the morse code input by user and takes the index of the morse and uses the parallel ArrayList structure to
    //change the letter into morse
    public char findMorseToLetter(String findMorse){
        int idx = findMorseIdx(findMorse);
        if(idx == -1)
            return 0;
        else
            return letter.get(idx);
    }

    //Gets the size of the arrayList. Only need one method because it of the parallel ArrayList structure
    public int size() {
        return letter.size();
    }
}

public class Main {
    //Populates the letters and morse ArrayLists in the MorseTranslator Object
    public static void populate(MorseTranslator translator){
        //Using try catch block instead of throws IOException so user does not get red text
        try{
            Scanner infile = new Scanner(new File("morse.txt"));
            while(infile.hasNext()){
                translator.addCharMorse(infile.next().charAt(0), infile.next());
            }
            infile.close();
        }
        catch (Exception e) {
            System.out.println("Sorry, there was an error");
        }
    }

    //Function to change morse code to the alphabet
    public static void morseToAlpha(Scanner scan, MorseTranslator translator){
        System.out.println("Enter words in morse (add spaces between letters in morse): ");
        String morseInput = scan.nextLine();
        String[] morseWords = morseInput.split(" ");
        for(int i = 0; i <morseWords.length; i++){
            System.out.print(translator.findMorseToLetter(morseWords[i]) + " ");
        }
        System.out.println();
    }

    //Function to change the letters into morse code
    public static void alphaToMorse(Scanner scan, MorseTranslator translator){
        System.out.println("Enter in a word to read into morse (no spaces between letters): ");
        String wordInput = scan.nextLine();
        //Changes word to uppercase to make char comparison easier
        wordInput = wordInput.toUpperCase();
        for(int i = 0; i < wordInput.length(); i++){
            System.out.print(translator.findLetterToMorse(wordInput.charAt(i)) + " ");
        }
        System.out.println();
    }

    //Prints the menu for the user
    public static void menu(){
        System.out.println("1. English to Morse");
        System.out.println("2. Morse to English");
        System.out.println("3. Quit");
    }

    public static void main(String[] args){
        System.out.println("Hello!");
        Scanner scan =  new Scanner(System.in);
        String choice;
        MorseTranslator translator = MorseTranslator.getInstance();
        populate(translator);
        menu();
        choice = scan.nextLine();

        //Input validation
        while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3")){
            System.out.println("Invalid, try again");
            choice = scan.nextLine();
        }

        while(!choice.equals("3")){
            if(choice.equals("1"))
                alphaToMorse(scan, translator);
            else if(choice.equals("2"))
                morseToAlpha(scan, translator);
            menu();
            choice = scan.nextLine();
        }
        System.out.println("Goodbye");

    }
}