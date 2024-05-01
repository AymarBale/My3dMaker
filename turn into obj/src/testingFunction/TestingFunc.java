package testingFunction;

import groupCreatorSyntax.MyTextGroup;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static groupCreatorSyntax.SyntaxDetector.*;

public class TestingFunc extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a Polygon with the specified points
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
                15.0, 15.0,
                15.0, 35.0,
                35.0, 35.0,
                35.0, 15.0


        );

        // Set the fill color and stroke color
        polygon.setFill(Color.TRANSPARENT);
        polygon.setStroke(Color.BLACK);

        // Coordinates of the tracker
        double trackerX = 90.0;
        double trackerY = 30.0;

        // Check if the tracker is inside the polygon
        boolean inside = polygon.contains(trackerX, trackerY);

        // Print the result
        System.out.println("Is tracker inside polygon? " + inside);


    }

    public static void main(String[] args) {
        launch(args);
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
            System.out.println(input);
            convertGroups(input);
        } else {
            System.out.println("No rule line found.");
        }
    }



}
