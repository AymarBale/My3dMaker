package groupCreatorSyntax;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxDetector extends Application {
    static ArrayList<MyGroup> groupList = new ArrayList<>();
    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(10);
        gridPane.setLayoutY(10);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        // Add labels and text fields for each attribute
        String[] attributes = {"GroupName", "x", "y", "z", "axis", "corner", "batch", "color"};
        TextField[] textFields = new TextField[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            Label label = new Label(attributes[i] + ":");
            label.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, FontPosture.REGULAR, 13));
            TextField textField = new TextField();
            textFields[i] = textField;
            gridPane.add(label, 0, i);
            gridPane.add(textField, 1, i);
        }
// Create TextArea
        TextArea textArea = new TextArea();
        textArea.setPrefSize(300, 270);
        // Create and add the Synthesize button
        Button synthesizeButton = new Button("Synthesize");
        synthesizeButton.setOnAction(e -> synthesizeGroups(textFields,attributes,textArea));
        gridPane.add(synthesizeButton, 0, attributes.length);

        // Create Scene


        // Create Button
        Button analyzeButton = new Button("Analyse and add to group");
        analyzeButton.setOnAction(e -> {
            String text = textArea.getText();
            System.out.println("Text in TextArea: ");
            System.out.println(detectGroups("GROUPANME(TEST1){\n" +
                    "    x:321;\n" +
                    "    y:123;\n" +
                    "    z:456;\n" +
                    "    axis:\"Z\";\n" +
                    "    corner:{[2,3],[4,5],[6,7],[8,9]};\n" +
                    "    batch:98;\n" +
                    "    color:#FF0990;\n" +
                    "},GROUPANME(TEST2){\n" +
                    "    x:135;\n" +
                    "    y:7910;\n" +
                    "    z:975;\n" +
                    "    axis:\"Y\";\n" +
                    "    corner:{[9,8],[7,6],[5,4],[3,2]};\n" +
                    "    batch:36;\n" +
                    "    color:#000000;\n" +
                    "},GROUPANME(RandomGroup1){\n" +
                    "    x:111;\n" +
                    "    y:222;\n" +
                    "    z:333;\n" +
                    "    axis:\"X\";\n" +
                    "    corner:{[1,2],[3,4],[5,6],[7,8]};\n" +
                    "    batch:99;\n" +
                    "    color:#ABCDEF;\n" +
                    "},GROUPANME(RandomGroup2){\n" +
                    "    x:444;\n" +
                    "    y:555;\n" +
                    "    z:666;\n" +
                    "    axis:\"Z\";\n" +
                    "    corner:{[10,11],[12,13],[14,15],[16,17]};\n" +
                    "    batch:88;\n" +
                    "    color:#123456;\n" +
                    "};"));
        });

        // Create Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(textArea, analyzeButton);
        layout.setLayoutX(300);
        layout.setLayoutY(20);
        // Create StackPane to center VBox
        Pane root = new Pane();
        root.getChildren().add(layout);
        root.getChildren().add(gridPane);
        // Create Scene
        Scene scene = new Scene(root, 640, 350);

        // Set Scene
        primaryStage.setScene(scene);
        primaryStage.setTitle("TextArea Example");
        primaryStage.show();
    }
    public static String detectGroups(String input) {
        String output = "";
        List<String> groups = extractGroups(input);
        for (String group : groups) {
            convertGroups(group);
        }
        for (int i = 0; i < groupList.size(); i++) {
            output += groupList.get(i).toString()+"\n";
        }
        return output;
    }
    private void synthesizeGroups(TextField[] textFields,String[] attributes,TextArea t) {
        StringBuilder sb = new StringBuilder();
        sb.append("GROUPANME(").append(textFields[0].getText()).append("){\n");
        for (int i = 1; i < textFields.length - 1; i++) {
            String attributeName = attributes[i];
            String attributeValue = textFields[i].getText();
            if (attributeName.equals("corner")) {
                // Format corner attribute value
                sb.append("    ").append(attributeName).append(": {")
                        .append(attributeValue).append("};\n");
            } else {
                sb.append("    ").append(attributeName).append(": ").append(attributeValue).append(";\n");
            }
        }
        // Append the color attribute
        sb.append("    ").append(attributes[attributes.length - 1]).append(": ")
                .append(textFields[textFields.length - 1].getText()).append(";\n}\n");
        t.setText(t.getText()+sb.toString());
        System.out.println(sb.toString());
    }
    public static void convertGroups(String input) {
        String pattern = "^GROUPANME\\(\\w+\\)\\{\\s*" +
                "x:\\s*(-?\\d+);\\s*" +
                "y:\\s*(-?\\d+);\\s*" +
                "z:\\s*(-?\\d+);\\s*" +
                "axis:\\s*\"(\\w+)\";\\s*" +
                "corner:\\{\\s*\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\]\\s*,\\s*\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\]\\s*,\\s*\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\]\\s*,\\s*\\[\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\]\\s*\\};\\s*" +
                "batch:\\s*(\\d+);\\s*" +
                "color:\\s*#([0-9A-Fa-f]{6});\\s*" +
                "}";

        // Create a Pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a Matcher object
        Matcher matcher = regex.matcher(input);

        // Check if the pattern is found
        if (matcher.find()) {
            String groupName = extractGroupName(input);
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            int z = Integer.parseInt(matcher.group(3));
            String axis = matcher.group(4);
            String corner = "[" + matcher.group(5) + "," + matcher.group(6) + "], [" +
                    matcher.group(7) + "," + matcher.group(8) + "], [" +
                    matcher.group(9) + "," + matcher.group(10) + "], [" +
                    matcher.group(11) + "," + matcher.group(12) + "]";
            int batch = Integer.parseInt(matcher.group(13));
            String color = "#" + matcher.group(14);
            MyGroup group = new MyGroup(groupName,x, y, z, axis, corner, batch, color);
            groupList.add(group);
            } else {
        }
    }
    public static ArrayList<String> extractGroups (String input){
        List<String> groupList = new ArrayList<>();

        // Split the input string by the comma outside curly braces
        String[] parts = input.split("(?<=})\\s*,\\s*");

        // Add each group to the list
        for (String part : parts) {
            // Extract group name
            int startIdx = part.indexOf("GROUPANME(");
            int endIdx = part.indexOf(")", startIdx);
            String groupName = part.substring(startIdx, endIdx + 1);

            // Add the opening curly brace back to the group
            String groupContent = part.substring(part.indexOf("{"));
            String group = groupName + groupContent;
            groupList.add(group);
        }

        return (ArrayList<String>) groupList;
    }
    public static String extractGroupName(String input) {
        // Define the prefix and suffix of the group name
        String prefix = "GROUPANME(";
        String suffix = ")";

        // Find the starting index of the group name
        int startIndex = input.indexOf(prefix);
        if (startIndex == -1) {
            return "Group name not found!";
        }

        // Adjust the starting index to exclude the prefix
        startIndex += prefix.length();

        // Find the ending index of the group name
        int endIndex = input.indexOf(suffix, startIndex);
        if (endIndex == -1) {
            return "Group name not found!";
        }

        // Extract and return the group name
        return input.substring(startIndex, endIndex);
    }
}
class MyGroup {
    String groupName;
    int x;
    int y;
    int z;
    String axis;
    String corner;
    int batch;
    String color;

    public MyGroup(String groupName,int x, int y, int z, String axis, String corner, int batch, String color) {
        this.groupName = groupName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.axis = axis;
        this.corner = corner;
        this.batch = batch;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Group{" +
                "GroupName:"+groupName+" "+
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", axis='" + axis + '\'' +
                ", corner='" + corner + '\'' +
                ", batch=" + batch +
                ", color='" + color + '\'' +
                '}';
    }
}

