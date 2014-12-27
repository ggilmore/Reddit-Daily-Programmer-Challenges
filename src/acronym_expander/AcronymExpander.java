package acronym_expander;

import java.io.ObjectOutputStream.PutField;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class AcronymExpander {

    /*
     * Set up all the regex's
     */
    private static final Pattern lol = Pattern.compile("\\blol\\b");
    private static final Pattern dw = Pattern.compile("\\bdw\\b");
    private static final Pattern hf = Pattern.compile("\\bhf\\b");
    private static final Pattern gg = Pattern.compile("\\bgg\\b");
    private static final Pattern brb = Pattern.compile("\\bbrb\\b");
    private static final Pattern g2g = Pattern.compile("\\bg2g\\b");
    private static final Pattern wtf = Pattern.compile("\\bwtf\\b");
    private static final Pattern wp = Pattern.compile("\\bwp\\b");
    private static final Pattern gl = Pattern.compile("\\bgl\\b");
    private static final Pattern imo = Pattern.compile("\\bimo\\b");

    /*
     * set up the mapping from acronyms to their expanded forms
     */
    private static final Map<Pattern, String> acroynmMapping = new HashMap<Pattern, String>() {
        {
            put(lol, "laugh out loud");
            put(dw, "don't watch");
            put(hf, "have fun");
            put(gg, "good game");
            put(brb, "be right back");
            put(g2g, "got to go");
            put(wtf, "what the fuck");
            put(wp, "well played");
            put(gl, "good luck");
            put(imo, "in my opinion");
        }
    };

    /**
     * If the input string contains any acronyms (listed below), they will
     * replaced by their expanded forms (described below).
     * 
     * (acroynm) - (thing the acroynm will be replaced by)
     * 
     * "lol" - "laugh out loud"
     * 
     * "dw" "don't watch"
     * 
     * "hf" - "have fun"
     * 
     * "gg" - "good game"
     * 
     * "brb" - "be right back"
     * 
     * "g2g" - "got to go"
     * 
     * "wtf" - "what the fuck"
     * 
     * "wp" - "well played"
     * 
     * "gl" - "good luck"
     * 
     * "imo" - "in my opinion"
     * 
     * @param inputString
     *            the string that is getting it's acroynms replaced, if any
     * @return inputString with all of it's acroynms replaced, if any
     */
    public static String replaceAcryonm(String inputString) {
        StringBuilder outputString = new StringBuilder();
        outputString.append(inputString); // preload the strinbuilder with the
                                          // whole string
        /*
         * Now we iterate through every acronym and see if it exists inside the
         * given inputString. If so, we replace the acronym with it's expanded
         * form inside the stringBuilder.
         * 
         */
        for (Pattern acronym : acroynmMapping.keySet()) {
            Matcher acronymMatcher = acronym.matcher(outputString.toString());
            while (acronymMatcher.find()) {
                int beginningOfAcronymLocation = acronymMatcher.start();
                int endOfAcronymLocation = acronymMatcher.end();
                String expandedAcronym = acroynmMapping.get(acronym);
                outputString.replace(beginningOfAcronymLocation, endOfAcronymLocation, expandedAcronym);
            }
        }
        return outputString.toString();
    }

    public static void main(String[] args) {
        System.out
                .println(replaceAcryonm("imo that was wp.gg.Anyway, I've g2g"));

    }

}
