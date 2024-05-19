package testingFunction;

import ImageTaker.QuickImport;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;

import static javafx.application.Application.launch;

public class TestingFunc extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create a button
        Button copyButton = new Button("Copy Text");

        // Set up the button action
        copyButton.setOnAction(event -> {
            // The string you want to copy to the clipboard
            String textToCopy = "Hello, this text will be copied to the clipboard!";

            // Create a ClipboardContent object
            ClipboardContent content = new ClipboardContent();
            content.putString(textToCopy);

            // Get the system clipboard
            Clipboard clipboard = Clipboard.getSystemClipboard();

            // Set the content to the clipboard
            clipboard.setContent(content);
        });

        // Create a layout and add the button to it
        VBox root = new VBox(10, copyButton);

        // Create a scene with the layout
        Scene scene = new Scene(root, 300, 200);

        // Set up the stage
        primaryStage.setTitle("Copy to Clipboard Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

