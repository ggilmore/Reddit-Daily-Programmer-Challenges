package acronym_expander;

import java.io.ObjectOutputStream.PutField;
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
    private static final Pattern lol = Pattern.compile("lol");
    private static final Pattern dw = Pattern.compile("dw");
    private static final Pattern hf = Pattern.compile("hf");
    private static final Pattern gg = Pattern.compile("gg");
    private static final Pattern brb = Pattern.compile("brb");
    private static final Pattern g2g = Pattern.compile("g2g");
    private static final Pattern wtf = Pattern.compile("wtf");
    private static final Pattern wp = Pattern.compile("wp");
    private static final Pattern gl = Pattern.compile("gl");
    private static final Pattern imo = Pattern.compile("imo");

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
     * replaced by their expanded forms (described below)
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
        String[] splitInputArray = inputString.split(" "); // split up the input
                                                           // into the different
                                                           // parts that are
                                                           // separated by
                                                           // spaces
        
        StringJoiner joiner = new StringJoiner(" "); // used to re-create the
                                                     // input string (with all
                                                     // the acronyms replaced)
        
        for (int i = 0; i < splitInputArray.length; i++) {
            String partOfInput = splitInputArray[i];
            for (Pattern acronym : acroynmMapping.keySet()) {
                Matcher acyronymMatcher = acronym.matcher(partOfInput);
                if (acyronymMatcher.find()) { // if the acronym is a substring
                                              // of partOfInput, the substring
                                              // will replaced with it's
                                              // expanded form
                    String expandedAcronym = acroynmMapping.get(acronym);
                    String newPartOfInput = acyronymMatcher
                            .replaceAll(expandedAcronym);
                    splitInputArray[i] = newPartOfInput;
                    break;
                }
            }
            joiner.add(splitInputArray[i]);
        }
        return joiner.toString();
    };

    public static void main(String[] args) {
        System.out.println(replaceAcryonm("imo that was wp. Anyway I've g2g"));

    }

}
