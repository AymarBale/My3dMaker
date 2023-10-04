package ImageTaker;

import ColorsPaletteExtraction.Extractor;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.File;

public class GetMyImageAlongAxe extends Application {
    public static String chosenAxe = "";
    public static String path = "";
    @Override
    public void start(Stage stage){
        Group root = new Group();
        Rectangle line = new Rectangle(600,5);
        Button chooseFile = new Button("Choose the file you want ");
        Button importer = new Button(" IMPORT ");
        FileChooser fileChooser = new FileChooser();
        Button butX = new Button("AxeX");Button butY = new Button("AxeY");Button butZ = new Button("AxeZ");
        butX.setStyle("-fx-font-size: 2em; ");butY.setStyle("-fx-font-size: 2em; ");butZ.setStyle("-fx-font-size: 2em; ");
        butX.setLayoutY(250);butY.setLayoutY(250);butZ.setLayoutY(250);
        butX.setLayoutX(200);butY.setLayoutX(300);butZ.setLayoutX(400);
        chooseFile.setLayoutX(275);chooseFile.setLayoutY(500);
        line.setY(350);line.setX(50);
        importer.setLayoutY(575);importer.setLayoutX(300);
        chooseFile.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            path = selectedFile.getPath();
        });
        butX.setOnAction(event -> {
            butX.setLayoutY(375);
            chosenAxe += "X";
        });
        butY.setOnAction(event -> {
            butY.setLayoutY(375);
        });
        butZ.setOnAction(event -> {
            butZ.setLayoutY(375);
            chosenAxe += "Z";
        });
        importer.setOnAction(event -> {
            try {
                stage.close();
                Stage secondWindow = new Stage();
                Extractor e = new Extractor();
                Extractor.imagePath = path;
                e.start(secondWindow);
                secondWindow.setTitle("extractor page");
                secondWindow.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        root.getChildren().add(butX);/*root.getChildren().add(butY);*/root.getChildren().add(butZ);
        root.getChildren().add(line);
        root.getChildren().add(chooseFile);
        root.getChildren().add(importer);
        Scene scene = new Scene(root, 700, 700);
        stage.setScene(scene);
        stage.setTitle("Pixel Color Viewer with Rectangle");
        stage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
