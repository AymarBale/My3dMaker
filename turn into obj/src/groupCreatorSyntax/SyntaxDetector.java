package groupCreatorSyntax;


import ColorsPaletteExtraction.Tracker;
import Grid.GridPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxDetector extends Application {
    static ArrayList<MyTextGroup> groupList = new ArrayList<>();
    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(10);
        gridPane.setLayoutY(10);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        // Add labels and text fields for each attribute
        String[] attributes = {"GroupName", "valAxis", "axis", "Corner", "batch", "color"};
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
        // Create and add the Synthesize button
        Button synthesizeButton = new Button("Synthesize");
        synthesizeButton.setOnAction(e ->{
                synthesizeGroups(textFields,attributes,CodingEditor.text);
                primaryStage.close();}
        );
        gridPane.add(synthesizeButton, 0, attributes.length);



        // Create Layout
        VBox layout = new VBox(10);
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

    private void synthesizeGroups(TextField[] textFields, String[] attributes, TextArea t) {
        String input = "GROUPNAME("+textFields[0].getText()+"){\n" +
                "valAxis: "+textFields[1].getText()+";\n" +
                "axis: "+textFields[2].getText()+";\n" +
                "Corner: {"+textFields[3].getText()+"};\n"+
                "batch: "+textFields[4].getText()+";\n" +
                "color: "+textFields[5].getText()+";\n" +
                "}";
        t.setText(t.getText()+"\n"+input);
    }
    public static MyTextGroup convertGroups(String input) {
        // Check if the input contains the necessary substrings
        if (!input.contains("GROUPNAME(") || !input.contains("valAxis:") || !input.contains("axis:")
                || !input.contains("Corner:") || !input.contains("batch:") || !input.contains("color:")) {
            // Handle the case where any of the required substrings are missing
            throw new IllegalArgumentException("Input string does not contain all required components.");
        }

        Pattern pattern = Pattern.compile("GROUPNAME\\((\\w+)\\)");
        Matcher matcher = pattern.matcher(input);
        String groupName = "";
        if(matcher.find()){
            groupName = matcher.group(1);
        }
        // Extract other relevant information using string manipulation
        int valAxisStartIndex = input.indexOf("valAxis:") + 8; // Start index of valAxis
        int valAxisEndIndex = input.indexOf(";", valAxisStartIndex); // End index of valAxis
        int valAxis = Integer.parseInt(input.substring(valAxisStartIndex, valAxisEndIndex).trim());

        int axisStartIndex = input.indexOf("axis:") + 5; // Start index of axis
        int axisEndIndex = input.indexOf(";", axisStartIndex); // End index of axis
        String axis = input.substring(axisStartIndex, axisEndIndex).trim();

        String corner = extractCornerLine(input);

        int batchStartIndex = input.indexOf("batch:") + 6; // Start index of batch
        int batchEndIndex = input.indexOf(";", batchStartIndex); // End index of batch
        int batch = Integer.parseInt(input.substring(batchStartIndex, batchEndIndex).trim());

        String color = ""; // Initialize color as an empty string
        if (input.contains("color:")) {
            int colorStartIndex = input.indexOf("color:") + 6; // Start index of color
            int colorEndIndex = input.indexOf(";", colorStartIndex); // End index of color
            color = input.substring(colorStartIndex, colorEndIndex).trim();
        }

        MyTextGroup t = new MyTextGroup(groupName, valAxis, axis, corner, batch, color);

        return t;
    }
    public static String extractCornerLine(String input) {
        String pattern = "Corner:\\s*\\{\\s*((\\[\\s*-?\\d+\\s*,\\s*-?\\d+\\s*\\]\\s*,?\\s*)*)\\};\\s*";

        // Create a Pattern object
        Pattern regex = Pattern.compile(pattern, Pattern.DOTALL); // Enable DOTALL mode to match across lines

        // Create a Matcher object
        Matcher matcher = regex.matcher(input);

        // Check if the pattern is found
        if (matcher.find()) {
            return matcher.group(1); // Return the first capturing group which contains all the points
        } else {
            return "Corner line not found in the input.";
        }
    }
    public static ArrayList<String> extractGroupNames(String input) {
        ArrayList<String> groupNames = new ArrayList<>();
        // Define the pattern to extract group names
        Pattern pattern = Pattern.compile("GROUPNAME\\((\\w+)\\)");

        // Create a Matcher object
        Matcher matcher = pattern.matcher(input);

        // Find all occurrences of the pattern
        while (matcher.find()) {
            groupNames.add(matcher.group(1)); // Add the captured group name to the list
        }
        return groupNames;
    }
    public static void createCirclesFromCoordinates(String coordinates, ArrayList<Circle> points) {
        String[] pairs = coordinates.split("\\],\\[");

        for (String pair : pairs) {
            pair = pair.replaceAll("[\\[\\]]", "");
            String[] coords = pair.split(",");

            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            //Corner line not found in the input.
            Circle circle = new Circle(x, y, 3); // Create a circle with radius 3
            points.add(circle);
        }

    }
    public static Polygon createPolyWithText(ArrayList<Circle> Points) {
        // Create a new polygon
        Polygon polygon = new Polygon();

        // Add points from the circles to the polygon
        for (Circle point : Points) {
            double x = point.getCenterX();
            double y = point.getCenterY();
            polygon.getPoints().addAll(x, y);
        }

        return polygon;
    }
    public static boolean isTrackerInsidePolygon(Tracker tracker, Polygon polygon) {
        double x = tracker.x;
        double y = tracker.y;
        double trackerSize = 10; // Assuming the tracker size is 10

        // Count intersections of the polygon edges with a line parallel to the x-axis
        int intersections = 0;
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            double x1 = polygon.getPoints().get(i);
            double y1 = polygon.getPoints().get(i + 1);
            double x2 = polygon.getPoints().get((i + 2) % polygon.getPoints().size());
            double y2 = polygon.getPoints().get((i + 3) % polygon.getPoints().size());

            // Check if the line crosses the horizontal line at y
            if ((y1 <= y + trackerSize && y - trackerSize < y2) || (y2 <= y + trackerSize && y - trackerSize < y1)) {
                // Calculate the x-coordinate of the intersection point
                double intersectionX = (x2 - x1) * (y - y1) / (y2 - y1) + x1;
                // If the intersection point is to the right of the test point, count it
                if (x <= intersectionX)
                    intersections++;
            }
        }

        // If the number of intersections is odd, the tracker is inside the polygon
        return intersections % 2 == 1;
    }

}
