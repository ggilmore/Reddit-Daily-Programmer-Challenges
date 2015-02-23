package markov_chain_error_detection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * T
 * 
 * @author gmgilmore
 *
 */
public class DocumentParser {

    private final File documentFile; // RI: must exist, not null
    private final Map<Character, Map<Character, Integer>> markovChainMatrix;
    /*
     * RI: must contain 26 entries, one for each letter in english alphabet
     */
    private final Map<Character, Map<Character, Double>> transitionProbability;
    /*
     * RI: must contain 26 entries, one for each letter in english alphabet,
     * each probability must be <=1
     */
    private final static String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private final static String FILE_LIST_NAME = "ParsedFiles.txt";
    
    private final static String MARKOV_CHAIN_MATRIX_NAME = "MarkovChainMatrixData.mcm";

    public DocumentParser(File file) {
        this.documentFile = file;
        this.markovChainMatrix = parseFile(this.documentFile);
        
        Map<Character, Map<Character, Double>> transitionProbabilitiesForEveryLetter = new HashMap<Character, Map<Character, Double>>();
        for (char letter : markovChainMatrix.keySet()) {
            Map<Character, Double> transitionProbabilitiesForOneLetter = this
                    .getTransitionProbabilities(letter);
            transitionProbabilitiesForEveryLetter.put(letter,
                    transitionProbabilitiesForOneLetter);
        }
        
        this.transitionProbability = Collections
                .unmodifiableMap(transitionProbabilitiesForEveryLetter);
        checkRep();
    }
    
    private boolean isFileParsed(String filePathToCheck){
        File parsedFiles = new File(
                "src/markov_chain_error_detection/Resources/DocumentParserData/"
                        + FILE_LIST_NAME);
        if (parsedFiles.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(parsedFiles));
                String fileLine = reader.readLine();
                while (fileLine != null){
                    String trimmedFileLine = fileLine.trim();
                    if (trimmedFileLine.equals(filePathToCheck)){
                        return true;
                    }
                }
                return false;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new RuntimeException("FileNotFoundExcepion: "+e);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new RuntimeException("IOException: "+e);
            }
        }
        else{
            return false;
        }
    }
    
    private void updateParsedFileList(String filePathToAdd){
        File parsedFiles = new File(
                "src/markov_chain_error_detection/Resources/DocumentParserData/"
                        + FILE_LIST_NAME);
        if (parsedFiles.exists()){
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(parsedFiles, true));
                out.newLine();
                out.write(filePathToAdd);
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(parsedFiles));
                out.write(filePathToAdd);
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                
            }
            
        }
    }
    
    

    /**
     * asserts the rep invariants stated above
     */
    private void checkRep() {
        assert this.documentFile.exists();
        for (char letter : LETTERS.toCharArray()) {
            assert this.markovChainMatrix.keySet().contains(letter);
            assert this.transitionProbability.keySet().contains(letter);
            for (char letterToTransitionTo : LETTERS.toCharArray()) {
                double probability = this.transitionProbability.get(letter)
                        .get(letterToTransitionTo);
                assert probability <= 1;
            }

        }

    }

    /**
     * Returns a mapping between char
     * 
     * @param letterToTransitionFrom
     *            the letter that we are transitioning from
     * @return a map, where each key that is a letter (letterToTransitionTo) in
     *         the English alphabet, and its corresponding value is the
     *         probability of transitioning from "letterToTransitionFrom" to
     *         "letterToTransitionTo". There are 26 key-value pairs in this map,
     *         1 for every letter in the english alphabet.
     * 
     */
    public Map<Character, Double> getTransitionProbilitesForThisLetter(
            char letterToTransitionFrom) {
        assert Arrays.asList(this.LETTERS.toCharArray()).contains(
                letterToTransitionFrom);
        return this.transitionProbability.get(letterToTransitionFrom);
    }

    /**
     * Reads the file "document"
     * 
     * @param document
     * @return
     */
    private Map<Character, Map<Character, Integer>> parseFile(File document) {
        Pattern wordPattern = Pattern.compile("\\b(\\w+)\\b");
        char[] letterArray = LETTERS.toCharArray();

        /*
         * instantiate the matrix with all zeroes for the letter counts for now
         */
        Map<Character, Map<Character, Integer>> markovChainMatrix = new HashMap<Character, Map<Character, Integer>>();
        for (Character letterY : letterArray) {
            Map<Character, Integer> letterCount = new HashMap<Character, Integer>();
            markovChainMatrix.put(letterY, letterCount);
            for (Character letterX : letterArray) {

                markovChainMatrix.get(letterY).put(letterX, 0);

            }
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(document));
            String fileLine = reader.readLine();

            while (fileLine != null) {
                String trimmedFileLine = fileLine.trim();
                Matcher wordPatternMatcher = wordPattern
                        .matcher(trimmedFileLine);
                while (wordPatternMatcher.find()) {
                    String word = wordPatternMatcher.group();
                    if (word.length() <= 1) {
                        continue;
                    }
                    char[] wordArray = word.toCharArray();
                    for (int i = 0; i < wordArray.length - 1; i++) {

                        char letterIAmCurrentlyLookingAt = wordArray[i];
                        char theLetterAheadOfMe = wordArray[i + 1];

                        Integer currentLetterCount = markovChainMatrix.get(
                                letterIAmCurrentlyLookingAt).get(
                                theLetterAheadOfMe);
                        Integer newLetterCount = new Integer(
                                currentLetterCount.intValue() + 1);
                        markovChainMatrix.get(letterIAmCurrentlyLookingAt)
                                .replace(theLetterAheadOfMe, newLetterCount);

                    }

                }

                fileLine = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(markovChainMatrix);
        for (char xLetter : markovChainMatrix.keySet()) {
            Map<Character, Integer> letterCount = markovChainMatrix
                    .get(xLetter);
            Map<Character, Integer> unmodifiableLetterCount = Collections
                    .unmodifiableMap(letterCount);
            markovChainMatrix.replace(xLetter, unmodifiableLetterCount);
        }
        Map<Character, Map<Character, Integer>> unmodifiableMarkovChainMatrix = Collections
                .unmodifiableMap(markovChainMatrix);
        return unmodifiableMarkovChainMatrix;
    }

    public static void main(String[] args) {

        File file = new File(
                "src/markov_chain_error_detection/Resources/enable1.txt");
        TypoDetector detector = new TypoDetector(file);

        Pattern wordPattern = Pattern.compile("\\b(\\w+)\\b");
        List<String> wordList = new ArrayList<String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String fileLine = reader.readLine();

            while (fileLine != null) {
                String trimmedFileLine = fileLine.trim();
                Matcher wordPatternMatcher = wordPattern
                        .matcher(trimmedFileLine);
                while (wordPatternMatcher.find()) {
                    String word = wordPatternMatcher.group();
                    wordList.add(word);
                }

                fileLine = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("You should see me once.");
        boolean needToRestart = true;
        double probability = 1;
        while (needToRestart) {
            // System.out.println("need to restart at top of loop: " +
            // needToRestart);
            outer: for (String word : wordList) {
                // System.out.println("word: " + word);
                boolean badProbability = detector.isMisspelled(word,
                        probability);
                if (badProbability) {
                    probability = probability / 10.0;
                    System.out.println("decreasing probability");
                    continue outer;
                }

            }
            needToRestart = false;
            // System.out.println("needToRestart is false now: " +
            // needToRestart);
            //
            //
            // System.out.println("working prob : " +probability);
            // System.out.println("still in loop");
            break;
        }
        System.out.println("FINAL: " + probability);

    }

    /**
     * Calculates the # of times that "letterThatComesAfterBaseLetter" comes
     * after "baseLetter" when both of them are in the same word for this
     * DocumentParser's stored file
     * 
     * @param baseLetter
     *            a character that is any given lowercase letter in the english
     *            alphabet,
     * @param letterThatComesAfterBaseLetter
     *            a character that is any given lowercase letter in the english
     *            alphabet
     * @return the # of times that "letterThatComesAfterBaseLetter" comes after
     *         "baseLetter" when both of them are in the same word for this
     *         DocumentParser's stored file
     */
    public int getLetterCount(char baseLetter,
            char letterThatComesAfterBaseLetter) {
        return this.markovChainMatrix.get(baseLetter).get(
                letterThatComesAfterBaseLetter);
    }

    /**
     * private helper method used to make sure that inputs to various methods
     * are lowercase english letters
     * 
     * @param inputCharacter
     *            the character to test
     * @return true if the character is a lowercase english character, false
     *         otherwise
     */
    private static boolean checkInputLowercaseLetter(char inputCharacter) {
        return Arrays.asList(LETTERS.toCharArray()).contains(inputCharacter);
        // assert input is a lowercase alphabet
        // letter, fail fast
    }

    private Map<Character, Double> getTransitionProbabilities(
            char letterToTransitionFrom) {
        assert checkInputLowercaseLetter(letterToTransitionFrom);
        Map<Character, Integer> letterCount = this.markovChainMatrix
                .get(letterToTransitionFrom);
        int sumOfLetterCounts = 0;
        for (int count : letterCount.values()) {
            sumOfLetterCounts += count;
        }
        Map<Character, Double> transitionProbabilities = new HashMap<Character, Double>();
        for (char letterToTransitionTo : letterCount.keySet()) {
            int thisLetterCount = letterCount.get(letterToTransitionTo);
            double probability = (double) thisLetterCount / sumOfLetterCounts;
            transitionProbabilities.put(letterToTransitionTo, probability);
        }
        Map<Character, Double> unmodifiableTransitionProbabilities = Collections
                .unmodifiableMap(transitionProbabilities);
        return unmodifiableTransitionProbabilities;
    }

}
