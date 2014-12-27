package acronym_expander;

import static org.junit.Assert.*;

import org.junit.Test;

public class AcronymExpanderTest {
    /**
     * Testing strategy:
     * 
     * -blank input string
     * 
     * -string with no acronyms
     * 
     * -string with acronyms that are embedded inside other words
     * 
     * -string with acronyms that aren't one of the ones listed
     * 
     * -string with included acronyms are free of punctuation
     * 
     * -string with included acronyms that contain punctuation
     */

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void testExpanderBlankString() {
        String stringToTest = "";
        String result = AcronymExpander.replaceAcryonm(stringToTest);
        assert result.isEmpty();

    }

    @Test
    public void testExpanderNoAcronyms() {
        String stringToTest = "this is a test string";
        String result = AcronymExpander.replaceAcryonm(stringToTest);
        assertEquals(result, "this is a test string");
    }

    @Test
    public void testExpanderAcronymsInsideOtherWords() {
        String stringToTest = "egg";
        String result = AcronymExpander.replaceAcryonm(stringToTest);
        assertEquals(result, "egg");
    }

    @Test
    public void testExpanderAcronymsNotOneListed() {
        String stringToTest = "rofl";
        String result = AcronymExpander.replaceAcryonm(stringToTest);
        assertEquals(result, "rofl");
    }

    @Test
    public void testExpanderAcronymsFreeOfPunctuation() {
        String stringToTest = "gl all hf";
        String result = AcronymExpander.replaceAcryonm(stringToTest);
        assertEquals(result, "good luck all have fun");
    }

    @Test
    public void testExpanderAcronymsContainsPunctuation() {
        String stringToTest = "imo that was wp.gg.Anyway, I've g2g";
        String result = AcronymExpander.replaceAcryonm(stringToTest);
        assertEquals(result,
                "in my opinion that was well played.good game.Anyway, I've got to go");
    }

}
