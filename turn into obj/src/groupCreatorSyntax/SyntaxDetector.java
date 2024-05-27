package groupCreatorSyntax;


import ColorsPaletteExtraction.Tracker;
import Grid.GridPage;
import Selector.LineSelector;
import Utils3DCreation.com.Utils;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxDetector extends Application {
    static ArrayList<MyTextGroup> groupList = new ArrayList<>();
    public static ArrayList<String> idList = new ArrayList<>();
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

        Pane root = new Pane();
        Button synthesizeButton = new Button("Synthesize");
        Button useViewer = new Button("use corner Viewer");
        LineSelector l = new LineSelector(0,0);


        useViewer.setOnAction(e ->{
            if(!(textFields[2].getText().trim().isEmpty())&&!(textFields[4].getText().trim().isEmpty())){
                ArrayList<Tracker> myCubes = getArrayListForCornerViewer(textFields[2].getText(),Integer.parseInt(textFields[4].getText()));
                Pane smallPane = GridPage.exportPane(textFields[2].getText(),Integer.parseInt(textFields[4].getText()));
                Group remake = new Group();
                remake = remakeForViewer(myCubes,remake,10.270);
                smallPane.setLayoutX(280);

                root.setLayoutY(getLowestY(myCubes)+20);
                smallPane.setLayoutY(smallPane.getLayoutY()-root.getLayoutY());
                l.myLin.setLayoutY(smallPane.getLayoutY()-root.getLayoutY());
                root.getChildren().add(smallPane);
                root.getChildren().add(remake);
                root.getChildren().add(l.myLin);
            }
        });
        l.ajouter.setOnAction(actionEvent -> {
            textFields[3].setText(convertPolygonToString(l.polygon));
            root.getChildren().remove(l.myLin);
        });
        synthesizeButton.setOnAction(e ->{
            boolean anyEmpty = false;
            for (TextField textField : textFields) {
                if (textField.getText().isEmpty()) {
                    anyEmpty = true;
                    break;
                }
            }
            if(!anyEmpty) {
                synthesizeGroups(textFields, attributes, CodingEditor.text);
                root.getChildren().clear();
                primaryStage.close();
            }
        });
        Text t = new Text("Vous devez remplir \nle champs axis  et batch\n avant d'utiliser le viewer");
        t.setLayoutY(280);
        t.setLayoutX(10);
        gridPane.add(synthesizeButton, 0, attributes.length);
        gridPane.add(useViewer, 1, attributes.length);

        // Create Layout
        VBox layout = new VBox(10);
        layout.setLayoutX(300);
        layout.setLayoutY(20);
        // Create StackPane to center VBox

        root.getChildren().add(layout);
        root.getChildren().add(gridPane);
        root.getChildren().add(t);
        // Create Scene
        Scene scene = new Scene(root, 740, 550);

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

    public  static void getAllBatchs(ArrayList<Tracker> cTrackers,String input){
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
    }

    public static void batchRP(ArrayList<Tracker> cTrackers,String input) {
        // Check if the input contains the necessary substrings
        ArrayList<String> characters = getFirstLetterAfterSecondSemicolon(input);


        // Extract groupName
        int groupNameStartIndex = input.indexOf("batchRp(") + 8; // Start index of batchRp(
        int groupNameEndIndex = input.indexOf(")", groupNameStartIndex); // End index of batchRp(
        String batch = input.substring(groupNameStartIndex, groupNameEndIndex);

        // Extract valAxis
        int valAxisStartIndex = input.indexOf("UAxis:") + 6; // Start index of UAxis:
        int valAxisEndIndex = input.indexOf(";", valAxisStartIndex); // End index of UAxis:
        String valAxis = input.substring(valAxisStartIndex, valAxisEndIndex).trim();

        // Extract X
        int xStartIndex = input.indexOf(characters.get(0)+":") + 2; // Start index of X:
        int xEndIndex = input.indexOf(";", xStartIndex); // End index of X:
        String x = input.substring(xStartIndex, xEndIndex).trim();

        // Extract Y
        int yStartIndex = input.indexOf(characters.get(1)+":") + 2; // Start index of Y:
        int yEndIndex = input.indexOf(";", yStartIndex); // End index of Y:
        String y = input.substring(yStartIndex, yEndIndex).trim();

        int idStartIndex = input.indexOf("id:")+3;
        int isEndIndex = input.indexOf(";",idStartIndex);
        String id = input.substring(idStartIndex, isEndIndex).trim();
        if(!idList.contains(id)){
            idList.add(id);
            updateTrackersWithBatch(cTrackers,valAxis,Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(batch));
            Utils.updateSquares(Integer.parseInt(batch));
        }
    }

    public static ArrayList<String> getFirstLetterAfterSecondSemicolon(String input) {
        ArrayList<String> axisBatchP = new ArrayList<>();
        int firstSemicolonIndex = input.indexOf(';');
        if (firstSemicolonIndex == -1) {
            throw new IllegalArgumentException("Semicolon not found in input string.");
        }
        while (firstSemicolonIndex < input.length() - 1 && Character.isWhitespace(input.charAt(firstSemicolonIndex + 1))) {
            firstSemicolonIndex++;
        }
        axisBatchP.add(String.valueOf(input.charAt(firstSemicolonIndex+1)));

        // Find the index of the second semicolon
        int secondSemicolonIndex = input.indexOf(';', firstSemicolonIndex + 1);
        if (secondSemicolonIndex == -1) {
            throw new IllegalArgumentException("Second semicolon not found in input string.");
        }

        // Get the letter after the second semicolon
        while (secondSemicolonIndex < input.length() - 1 && Character.isWhitespace(input.charAt(secondSemicolonIndex + 1))) {
            secondSemicolonIndex++;
        }/**/
        axisBatchP.add(String.valueOf(input.charAt(secondSemicolonIndex+1)));
        return axisBatchP;
    }

    public static void updateTrackersWithBatch(ArrayList<Tracker> copyTrackers,String uAxisValue, int xValue, int yValue, int batchValue) {
        for (int i = 0; i < GridPage.theMainExtratorArr.size();i++) {
            Tracker tracker = GridPage.theMainExtratorArr.get(i);
            Tracker copyTracker = copyTrackers.get(i);
            if (uAxisValue.equals("Z") && tracker.batch == batchValue) {
                tracker.x += xValue*10;
                tracker.y += yValue*10;
                copyTracker.x += (xValue*10);
                copyTracker.y += (yValue*10);
            }else if (uAxisValue.equals("X") && tracker.batch == batchValue) {
                tracker.x += xValue*10;
                tracker.y += yValue*10;
                copyTracker.x += (xValue*10);
                copyTracker.y += (yValue*10);
            }else if (uAxisValue.equals("Y") && tracker.batch == batchValue) {
                tracker.x += xValue*10;
                tracker.y += yValue*10;
                copyTracker.x += (xValue*10);
                copyTracker.y += (yValue*10);
            }

        }
    }

    public static String extractCornerLine(String input) {
        // Find the start index of the "Corner" section
        int cornerStartIndex = input.indexOf("Corner:");
        if (cornerStartIndex == -1) {
            return "Corner line not found in the input.";
        }

        // Find the start index of the opening brace '{' after "Corner:"
        int braceStartIndex = input.indexOf("{", cornerStartIndex);
        if (braceStartIndex == -1) {
            return "Opening brace for Corner section not found.";
        }

        // Find the end index of the closing brace '}' for the "Corner" section
        int braceEndIndex = input.indexOf("}", braceStartIndex);
        if (braceEndIndex == -1) {
            return "Closing brace for Corner section not found.";
        }

        // Extract the substring containing the points
        String pointsString = input.substring(braceStartIndex + 1, braceEndIndex).trim();

        // Return the extracted points string
        return pointsString;
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
            //Corner line not found in the input
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

    public static double getLowestY(ArrayList<Tracker> trackers) {
        double lowestY = Double.MAX_VALUE; // Initialize with maximum possible value
        for (Tracker tracker : trackers) {
            System.out.println(tracker.y);
            if (tracker.y < lowestY) {
                lowestY = tracker.y; // Update lowestY if current tracker's y is lower
            }
        }
        return lowestY; // Return the lowest y value found
    }

    public static ArrayList<Tracker> getArrayListForCornerViewer(String axe,int batch){
        ArrayList<Tracker> myCubes = new ArrayList<>();
        for (int i = 0; i < GridPage.theMainExtratorArr.size(); i++) {
            if((GridPage.theMainExtratorArr.get(i).axis.equals(axe))&&
            (GridPage.theMainExtratorArr.get(i).batch == batch)){
                myCubes.add(GridPage.theMainExtratorArr.get(i));
            }
        }
        return myCubes;
    }

    public static Group remakeForViewer(ArrayList<Tracker> myCubes, Group remake, double additiveX){
        for (int i = 0; i < myCubes.size(); i++){
            Rectangle r = new Rectangle(9,9);
            r.setX(myCubes.get(i).x+280);
            r.setY(myCubes.get(i).y);
            r.setFill(myCubes.get(i).col);
            remake.getChildren().add(r);
        }
        return remake;
    }

    public static String convertPolygonToString(Polygon polygon) {
        StringBuilder corners = new StringBuilder();

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            if (i % 2 == 0) { // Even index means X-coordinate
                corners.append("[");
                corners.append((Double.valueOf(polygon.getPoints().get(i) - 285)).intValue()).append(",");
            } else { // Odd index means Y-coordinate
                corners.append((Double.valueOf(polygon.getPoints().get(i) - 5)).intValue());
                corners.append("],");
            }
        }
        String modifiedString = corners.substring(0, corners.length() - 1);
        return modifiedString;
    }
}
