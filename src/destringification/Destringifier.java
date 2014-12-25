package destringification;

import java.util.Arrays;

/**
 * This is my solution to the 12/22/2014 Reddit Daily Programmer Challenge.
 * 
 * Link: https://www.reddit.com/r/dailyprogrammer/comments/2q2xnc/
 * 20141222_challenge_194_easy_destringification/
 * 
 * @author gmgilmore
 *
 */
public class Destringifier {

    /**
     * these are just here to improve readability 
     */
    private final static char BACKSLASH = '\\';
    private final static char NEWLINE = 'n';
    private final static char SINGLE_QUOTE = '\'';
    private final static char DOUBLE_QUOTE = '\"';
    private final static char CARRIAGE_RETURN = 'r';
    private final static char BACKSPACE = 'b';
    private final static char TAB = 't';
    private final static char FORMFEED = 'f';

    /*
     * Internal state of the reader. 
     */
    private enum State {
        OUTSIDE_SEQUENCE, INSIDE_SQUENCE
    }
    
    /**
     * The reader's status is outside_sequence if we aren't currently within an escape sequence, is inside_sequence otherwise 
     */
    private State status = State.OUTSIDE_SEQUENCE;
    
    /**
     * Creates a new Destringifier instance. 
     */
    public Destringifier() {

    }

    private String removeEscapeSequencesFromStringLiteral(String inputString) {
        StringBuilder outputString = new StringBuilder();
        char[] stringArray = inputString.toCharArray();
        System.out.println("char array" + Arrays.toString(stringArray));
        for (char character : stringArray) {
            System.out.println("looking at character: " + character);
            if (this.status == State.OUTSIDE_SEQUENCE) { // if we are not in an
                                                         // escape sequence
                                                         // currently
                if (character != BACKSLASH) {
                    outputString.append(character); // if the character isn't a
                                                    // backslash, then just
                                                    // append it
                } else {
                    this.status = State.INSIDE_SQUENCE; // if the character is a
                                                        // backslash, now we are
                                                        // inside an escape
                                                        // sequence
                }
            } else { // if we are inside an escape sequence, we need to check
                     // for all the valid escape sequences
                switch (character) {
                case BACKSLASH:
                    outputString.append(BACKSLASH);
                    this.status = State.OUTSIDE_SEQUENCE;
                    break;
                case NEWLINE:
                    outputString.append('\n');
                    this.status = State.OUTSIDE_SEQUENCE;
                    break;
                case SINGLE_QUOTE:
                    outputString.append('\'');
                    this.status = State.OUTSIDE_SEQUENCE;
                    break;
                case DOUBLE_QUOTE:
                    outputString.append(DOUBLE_QUOTE);
                    this.status = State.OUTSIDE_SEQUENCE;
                    break;
                case CARRIAGE_RETURN:
                    outputString.append('\r');
                    this.status = State.OUTSIDE_SEQUENCE;
                    break;
                case BACKSPACE:
                    outputString.append('\b');
                    this.status = State.OUTSIDE_SEQUENCE;
                    break;
                case TAB:
                    outputString.append('\t');
                    this.status = State.OUTSIDE_SEQUENCE;
                    break;
                case FORMFEED:
                    outputString.append('\f');
                    this.status = State.OUTSIDE_SEQUENCE;
                    break;
                default: // if this isn't one of the escape sequences that Java
                         // supports, throw an exception:
                    this.status = State.OUTSIDE_SEQUENCE;
                    throw new RuntimeException(
                            "Invalid escape sequence. You gave me \\"
                                    + character);
                }
            }

        }
        if (this.status == State.INSIDE_SQUENCE) { // if we reach the end of the
                                                   // string and we are still
                                                   // inside an escape sequence,
                                                   // the escape sequence wasn't
                                                   // closed properly and so we
                                                   // must throw an exception.
            this.status = State.OUTSIDE_SEQUENCE;
            throw new RuntimeException("Did not close ecape sequence properly.");
        }
        this.status = State.OUTSIDE_SEQUENCE;
        return outputString.toString();
    }
    
    public static void main(String[] args) {
        Destringifier destringifier = new Destringifier();
        System.out.println(destringifier.removeEscapeSequencesFromStringLiteral("lorem ipsum dolor sit amet \\\\"));
    }
}
