package groupCreatorSyntax;


import ColorsPaletteExtraction.Tracker;
import Grid.GridPage;
import Utils3DCreation.com.Rules;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.TextArea;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;

import static groupCreatorSyntax.SyntaxDetector.*;

public class CodingEditor extends Application {
    public static int width = 400;
    public static int height = 700;
    private double anchorX,anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    public static TextArea text = new TextArea();
    @Override
    public void start(Stage primaryStage) {
        Group trackerGroup = new Group();
        Camera cam = new PerspectiveCamera();
        SubScene subScene = new SubScene(trackerGroup, width, height, true, SceneAntialiasing.BALANCED);
        initMouseControl(trackerGroup,subScene,primaryStage);
        subScene.setCamera(cam);
        subScene.setLayoutX(width);
        subScene.setLayoutY(10);
        subScene.setFill(Color.SILVER);
        Pane pane = new Pane();
        pane.getChildren().add(subScene);
        Button visualize = new Button("visualize");
        Button createGroup = new Button("Create Group");

        visualize.setOnAction(EventTest -> {
            ArrayList<Tracker> ruleArr = new ArrayList<>();
            ArrayList<Tracker> copyArrayList = new ArrayList<>();
            for (Tracker tracker : GridPage.theMainExtratorArr) {
                copyArrayList.add(deepCopyTracker(tracker));
            }
            if(!(text.getText().equals(""))){

                ArrayList<Circle> points = new ArrayList<>();
                String textVal = text.getText();
                createCirclesFromCoordinates(extractCornerLine(textVal),points);
                Polygon poly = createPolyWithText(points);

                MyTextGroup textGroup = convertGroups(textVal);
                for (int i = 0; i < GridPage.theMainExtratorArr.size(); i++) {
                    if(poly.contains(GridPage.theMainExtratorArr.get(i).x, GridPage.theMainExtratorArr.get(i).y)&&GridPage.theMainExtratorArr.get(i).batch == textGroup.batch){
                        GridPage.theMainExtratorArr.get(i).z = textGroup.valAxis/10;
                        copyArrayList.get(i).z = textGroup.valAxis;
                        ruleArr.add(GridPage.theMainExtratorArr.get(i));
                    };
                }
            }
            //Rules.ApplyRuleForText(ruleArr,"1 ++ only",new Line(20,25,30,25));
            trackerGroup.getChildren().clear();
            for (Tracker tracker : copyArrayList) {
                if(tracker.axis.equals("X")){
                    Box box = new Box(10, 10, 10); // Set the size of the box as per your requirement
                    PhongMaterial material = new PhongMaterial();
                    material.setDiffuseColor(tracker.col); // Use the color of the tracker
                    box.setMaterial(material);
                    box.setTranslateX((width/2)+tracker.x); // Set the position of the box
                    box.setTranslateY((height/2)+tracker.y);
                    box.setTranslateZ(tracker.z);
                    trackerGroup.getChildren().add(box);
                }else if (tracker.axis.equals("Z")){
                    Box box = new Box(10, 10, 10); // Set the size of the box as per your requirement
                    PhongMaterial material = new PhongMaterial();
                    material.setDiffuseColor(tracker.col); // Use the color of the tracker
                    box.setMaterial(material);
                    box.setTranslateX((width/2)+tracker.z); // Set the position of the box
                    box.setTranslateY((height/2)+tracker.y);
                    box.setTranslateZ(tracker.x);
                    trackerGroup.getChildren().add(box);
                }
            }

        });
        pane.getChildren().add(visualize);
        pane.getChildren().add(createGroup);

        text.setStyle("-fx-control-inner-background: #ffd700;");
        text.setStyle("-fx-control-inner-background:#352F44; -fx-font-family: Monospace; -fx-highlight-fill: #ffd700; -fx-highlight-text-fill: #000000; -fx-text-fill: #ffd700; ");
        text.setPadding(new Insets(5));
        text.setPrefWidth(380);
        text.setPrefHeight(690);
        text.setLayoutX(10);
        text.setLayoutY(10);
        visualize.setLayoutX(width);
        visualize.setLayoutY(height+20);
        createGroup.setLayoutX(width/2);
        createGroup.setLayoutY(height+20);
        createGroup.setOnAction( EventHandle -> {
            Stage n = new Stage();
            SyntaxDetector s = new SyntaxDetector();
            s.start(n);
        });
        pane.getChildren().add(text);

        Scene scene = new Scene(pane,(width*2)+20,height+50);

        primaryStage.setTitle("TEST");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void initMouseControl(Group group, SubScene scene,Stage stage){
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0,Rotate.X_AXIS),
                    yRotate = new Rotate(0,Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        scene.setOnMousePressed(mouseEvent -> {
            anchorX = mouseEvent.getSceneX();
            anchorY = mouseEvent.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            angleX.set(anchorAngleX - (anchorY - mouseEvent.getSceneY()));
            angleY.set(anchorAngleY - (anchorX - mouseEvent.getSceneX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL , event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });
    }
    public static Tracker deepCopyTracker(Tracker original) {
        if (original == null) {
            return null;
        }

        Tracker copy = new Tracker();
        copy.x = original.x;
        copy.y = original.y;
        copy.z = original.z;
        copy.axis = original.axis;
        copy.col = original.col;
        copy.batch = original.batch;

        return copy;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
