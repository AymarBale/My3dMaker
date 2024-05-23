package groupCreatorSyntax;

import ColorsPaletteExtraction.Tracker;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTextGroup {
    String groupName;
    int valAxis;
    String axis;
    String corner;
    int batch;
    String color;

    public MyTextGroup() {

    }

    public MyTextGroup(String groupName, int valAxis, String axis, String corner, int batch, String color) {
        this.groupName = groupName;
        this.valAxis = valAxis * 10;
        this.axis = axis;
        this.corner = corner;
        this.batch = batch;
        this.color = color;
    }



    public static ArrayList<String> getAllGroups(String input) {
        String[] allLines = input.split("\n");
        ArrayList<String> groupNames = new ArrayList<>();
        // Loop through each line in the input
        for (int i = 0; i < allLines.length; i++) {
            if (allLines[i].contains("G")) {
                StringBuilder t = new StringBuilder();

                // Process the lines after the keyword
                for (int j = i; j < Math.min(i + 6 + 1, allLines.length); j++) {
                    t.append(allLines[j]).append("\n");
                }

                // Print or process the extracted lines
                groupNames.add(String.valueOf(t));

                // Remove the extracted lines from the input
                input = input.replace(t.toString(), "");
            }
        }
        return groupNames;
    }

    @Override
    public String toString() {
        return "Group{" +"\n"+
                "GroupName:" + groupName + "\n" +
                "valAxis=" + valAxis +"\n"+
                "axis=" + axis + '\n' +
                "corner={" + corner + "};\n" +/**/
                "batch=" + batch +"\n"+
                "color=" + color + ";\n" +
                '}';/**/
    }
}
