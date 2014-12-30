package markov_chain_error_detection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

public class DocumentParser {

    private final File documentFile; // RI: must exist, not null
    private final Map<Character, Map<Character, Integer>> markovChainMatrix; // RI:
                                                                             // must
                                                                             // contain
                                                                             // 26
                                                                             // entries,
                                                                             // one
                                                                             // for
                                                                             // each
                                                                             // letter
                                                                             // in
                                                                             // the
                                                                             // english
                                                                             // alphabet;
    private final Map<Character, Map<Character, Double>> transitionProbability; // RI:
                                                                                // must
                                                                                // contain
                                                                                // 26
                                                                                // entries,
                                                                                // one
                                                                                // for
                                                                                // each
                                                                                // letter
                                                                                // in
                                                                                // english
                                                                                // alphabet,
                                                                                // all
                                                                                // probabilities
                                                                                // must
                                                                                // be
                                                                                // <=1
    private final static String LETTERS = "abcdefghijklmnopqrstuvwxyz";

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
//                System.out.println("word: " + word);
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
