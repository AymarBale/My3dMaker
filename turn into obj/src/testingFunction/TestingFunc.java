package testingFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestingFunc {

    public static String extractAxis(String inputString) {
        // Define the pattern for matching the AXIS:X format
        Pattern pattern = Pattern.compile("AXIS:(\\w+)");

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(inputString);

        // Check if the pattern is found
        if (matcher.find()) {
            // Return the matched axis value
            return matcher.group(1);
        } else {
            // Return null if no match is found
            return null;
        }
    }

    public static void main(String[] args) {
            String inputString = "Tab 1 AXIS:W";
            String axisValue = extractAxis(inputString);

            if (axisValue != null) {
                System.out.println("Axis value: " + axisValue);
            } else {
                System.out.println("No AXIS value found in the input string.");
            }

    }
}
