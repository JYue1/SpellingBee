// James Yue
// 3/21/24
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, James Yue
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    // Size of the entire dictionary
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // Call onto the method makeWords to generate all the permutations of words from the letters given
        // The parameters take in a holder and the rest of the letters
        makeWords("", letters);
    }

    public void makeWords(String holder, String letters) {
        // C.T. Understands operator precedence, including casting, boolean operators, pre- and post-increment, and method calls.
        // C.T. Can trace through a given recursive method to understand its behavior and the results returned when called.
        // C.T. Can write an algorithm to solve a problem recursively, including a base case and a recursive step.
        // Base case: If there are no letters
        if (letters.length() == 0) {
            words.add(letters);
            return;
        }
        // Going through the length of the letters to create all the permutations of words possible
        for (int i = 0; i < letters.length(); i++) {
            words.add(holder + letters.substring(i, i + 1));
            makeWords(holder + letters.substring(i, i + 1), letters.substring(0, i) + letters.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Call on the function mergeSort to sort the words created in alphabetical order
        words = mergeSort(words, 0, words.size() - 1);
    }

    private ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high) {
        // C.T. Can trace through a given recursive method to understand its behavior and the results returned when called.
        // C.T. Can write an algorithm to solve a problem recursively, including a base case and a recursive step.
        if (high - low == 0) {
            ArrayList<String> newArr = new ArrayList<String>();
            newArr.add(arr.get(low));
            return newArr;
        }
        int med = (high + low) / 2;
        ArrayList<String> arr1 = mergeSort(arr, low, med);
        ArrayList<String> arr2 = mergeSort(arr, med + 1, high);
        return merge(arr1, arr2);
    }

    // C.T. Understand how to use nesting to embed loops and conditionals inside of other loops and conditionals.
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {

        ArrayList<String> sol = new ArrayList<String>();
        int index1 = 0, index2 = 0;

        // C.T. Understand how to use nesting to embed loops and conditionals inside of other loops and conditionals.
        // C.T. Can write algorithms to traverse and modify Arrays and ArrayLists.
        // C.T. Can use ArrayList methods.
        // Finding whether the number of the first subarray is smaller than the number of the second subarray is second
        while (index1 < arr1.size() && index2 < arr2.size()) {
            if (arr1.get(index1).compareTo(arr2.get(index2)) < 0) {
                sol.add(arr1.get(index1++));
            }
            else {
                sol.add(arr2.get(index2++));
            }
        }
        // If there are elements left in one of the subarrays continue adding it to the main array until there are no more
        while (index1 < arr1.size()) {
            sol.add(arr1.get(index1++));
        }
        while (index2 < arr2.size()) {
            sol.add(arr2.get(index2++));
        }
        // Return the sorted array
        return sol;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // C.T. Understands operator precedence, including casting, boolean operators, pre- and post-increment, and method calls.
        // C.T. Can use ArrayList methods.
        // Calling on the found method and comparing the dictionary words with the words
        for (int i = 0; i < words.size(); i++) {
            // If the word is not a real word remove it from the list of words
            if (!found(words.get(i))) {
                // Shift over
                words.remove(i--);
            }
        }
    }

    public boolean found(String word) {
        // C.T. Understand how to use nesting to embed loops and conditionals inside of other loops and conditionals.
        // Binary sort
        int low = 0;
        int high = DICTIONARY_SIZE - 1;

        while (low <= high) {
            int mid = (high + low) / 2;
            int comparison = DICTIONARY[mid].compareTo(word);

            // Found the word
            if (comparison == 0) {
                return true;
            }
            // The word is smaller
            else if (comparison < 0) {
                low = mid + 1;
            }
            // The word is bigger
            else {
                high = mid - 1;
            }

        }
        return false;
    }


    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}