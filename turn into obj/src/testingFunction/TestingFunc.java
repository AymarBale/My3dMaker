package testingFunction;

import ColorsPaletteExtraction.Tracker;

import java.util.ArrayList;

import static groupCreatorSyntax.SyntaxDetector.*;
import static javafx.application.Application.launch;

public class TestingFunc {
    public  static ArrayList<String> getAllBatchs(ArrayList<Tracker> cTrackers, String input){
        String[] allLines = input.split("\n");
        ArrayList<String> individualBatchs = new ArrayList<>();
        // Loop through each line in the input
        for (int i = 0; i < allLines.length; i++) {
            if (allLines[i].contains("batchRp(")) {
                StringBuilder t = new StringBuilder();

                // Process the lines after the keyword
                for (int j = i; j < Math.min(i + 5 + 1, allLines.length); j++) {
                    t.append(allLines[j]).append("\n");
                }
                // Print or process the extracted lines
                individualBatchs.add(String.valueOf(t));
                batchRP(cTrackers, String.valueOf(t));
                // Remove the extracted lines from the input
                input = input.replace(t.toString(), "");
            }
        }
        return individualBatchs;
    }

    public static void main(String[] args) {
        String input = "batchRp(66){\n" +
                "  UAxis:Y;\n" +
                "  X:1;\n" +
                "  Z:-1;\n" +
                "  id:tyut90;\n" +
                "}\n" +
                "batchRp(55);{\n" +
                "  UAxis:Y;\n" +
                "  X:-2;\n" +
                "  Z:0;\n" +
                "  id:tyon90;\n" +
                "}\n" +
                "batchRp(22){\n" +
                "  UAxis:Y;\n" +
                "  X:-2;\n" +
                "  Z:0;\n" +
                "  id:tyut90;\n" +
                "}\n" +
                "batchRp(33){\n" +
                "  UAxis:Y;\n" +
                "  X:+1;\n" +
                "  Z:-1;\n" +
                "  id:BAut90;\n" +
                "}";
        ArrayList<Tracker> test = new ArrayList<>();
        ArrayList<String> splitStrings = getAllBatchs(test,input);

        // Print the results
        for (String str : idList) {

            System.out.println(str);
        }
    }
}

