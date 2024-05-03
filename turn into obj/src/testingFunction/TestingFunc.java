package testingFunction;

import ColorsPaletteExtraction.Tracker;
import Utils3DCreation.com.Rules;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static groupCreatorSyntax.SyntaxDetector.*;

public class TestingFunc{



    public static void main(String[] args) {
        String input = "GROUPNAME(test){\n" +
                "valAxis: 3;\n" +
                "axis: X;\n" +
                "Corner: {[20,20],[20,40],[20,40],[20,10]};\n" +
                "batch: 11;\n" +
                "color: #FFFFFF;\n" +
                "rules: {[1 ++ all|1,2|3,4],[1 ++ all|5,6|7,8]}\n" +
                "}\n";
        detectRule(input);
    }
    public static void detectRule(String input){
        String pattern = "rules:\\s*\\{([^{}]*)\\}";

        // Create a Pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a Matcher object
        Matcher matcher = regex.matcher(input);

        // Find and print the rule line content if present
        if (matcher.find()) {
            String ruleLine = matcher.group(1).trim();
            input = matcher.replaceAll(""); // Replace the matched rule line with an empty string
            int lastIndex = input.lastIndexOf("\n");
            if (lastIndex != -1) {
                input = input.substring(0, lastIndex) + input.substring(lastIndex + 1);
            }
            String[] rules = ruleLine.split("]");
            for (String r : rules) {
                applyRuleForText(new ArrayList<>(), r);
            }
        }
        convertGroups(input);
    }
    public static void applyRuleForText(ArrayList<Tracker> trackers, String rule) {
        while ((rule.charAt(0) == ',')||(rule.charAt(0) == '[')){
            StringBuilder remChar = new StringBuilder(rule);
            remChar.deleteCharAt(0);
            rule = String.valueOf(remChar);

        }
        StringBuilder remChar = new StringBuilder(rule);
        int endIndex = rule.indexOf('|');
        if (0 < endIndex) {
            remChar.delete(endIndex ,rule.length() ); // Delete from 'e' to 'o', inclusive
        }
        String ruleString = String.valueOf(remChar);
        String numericVal = rule.replace(ruleString,"");
        ArrayList<Integer> val = findIntegers(numericVal);
        Rules myRule = new Rules(ruleString,val.get(0),val.get(1),val.get(2),val.get(3));
        System.out.println(myRule.toString());
    }

    public static ArrayList<Integer> findIntegers(String stringToSearch) {
        Pattern integerPattern = Pattern.compile("-?\\d+");
        Matcher matcher = integerPattern.matcher(stringToSearch);

        ArrayList<Integer> integerList = new ArrayList<>();
        while (matcher.find()) {
            integerList.add(Integer.valueOf(matcher.group()));
        }

        return integerList;
    }


}
