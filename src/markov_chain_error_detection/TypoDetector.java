package markov_chain_error_detection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TypoDetector {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private final DocumentParser parser;
    private final Map<Character, Map<Character, Double>> cachedTransitionProbabilities;

    public TypoDetector(File wordList) {
        this.parser = new DocumentParser(wordList);
        
        Map<Character, Map<Character, Double>> cachedTransitionProbabilities = new HashMap<Character, Map<Character, Double>>();
        for (char letter : LETTERS.toCharArray()) {
            Map<Character, Double> transitionProbabilityFromThisLetter = parser
                    .getTransitionProbilitesForThisLetter(letter);

            cachedTransitionProbabilities.put(letter,
                    transitionProbabilityFromThisLetter);
        }
        this.cachedTransitionProbabilities = cachedTransitionProbabilities;
    }

    public boolean isMisspelled(String word, double minConfidence) {
        assert minConfidence <= 1;

        if (word.length() <= 1) {
            return true;
        }

        // System.out.println(cachedTransitionProbabilities);
        String lowercaseWord = word.toLowerCase();

        char[] wordArray = lowercaseWord.toCharArray();
        for (int i = 0; i < wordArray.length - 1; i++) {
            char baseLetter = wordArray[i];
            char followingLetter = wordArray[i + 1];
            double transitionProbability = this.cachedTransitionProbabilities.get(
                    baseLetter).get(followingLetter);
            if (transitionProbability <= minConfidence) {
                 System.out
                 .println(word
                 +
                 " is probably misspelled. The first problem pair of characters that I encountered was: "
                 + baseLetter + "," + followingLetter
                 + "\nThe transition probability was: "
                 + transitionProbability +
                 ", while my minConfidence was set at: " + minConfidence);
                return true;
            }
        }

        System.out.println(word + " is probably spelled correctly. ");
        return false;

    }

    public static void main(String[] args) {
        File wordList = new File(
                "src/markov_chain_error_detection/Resources/enable1.txt");
       TypoDetector detector = new TypoDetector(wordList);
       detector.isMisspelled("jdogcx", 1E-5);
    }
}
