package parserPractice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    /*
     * A mapping of command types to the amount of tokens that comprise the
     * whole valid command
     */
    private final Map<String, Integer> commandToLengthMapping = Collections
            .unmodifiableMap(new HashMap<String, Integer>() {
                {
                    put("look", 1);
                    put("dig", 3);
                    put("flag", 3);
                    put("deflag", 3);
                    put("help", 1);
                    put("bye", 1);
                }
            });

    public List<String> checkValidCommand(String inputCommand) {
        List<String> splitString = Arrays.asList(inputCommand.split(" "));

        String message = splitString.get(0);

        if (commandToLengthMapping.containsKey(message)) {
            if (commandToLengthMapping.get(message) == splitString.size()) {
                return parseArguments(splitString);
            }
        }

        return new ArrayList<String>();
    }

    private List<String> parseArguments(List<String> inputCommand) {

        String message = inputCommand.get(0);

        boolean hasCoordinates = (commandToLengthMapping.get(message) == 3);

        if (hasCoordinates) {
            int firstCandidate;
            int secondCandidate;

            try {
                firstCandidate = Integer.parseInt(inputCommand.get(1));
                secondCandidate = Integer.parseInt(inputCommand.get(2));
                return inputCommand;
            } catch (NumberFormatException e) {
                return new ArrayList<String>();
            }

        } else {
            return Arrays.asList(message);
        }

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
