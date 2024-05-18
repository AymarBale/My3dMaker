package groupCreatorSyntax;


import ColorsPaletteExtraction.Tracker;
import Grid.GridPage;
import Utils3DCreation.com.Rules;
import Utils3DCreation.com.Utils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static groupCreatorSyntax.SyntaxDetector.*;

public class CodingEditor extends Application {
    public static int width = 400;
    public static int height = 700;
    private double anchrX;
    private double anchrY;
    private double anchr_angX = 0;
    private double anchr_angY = 0;
    private final DoubleProperty anglX = new SimpleDoubleProperty();
    private final DoubleProperty anglY = new SimpleDoubleProperty();
    /* <----------------------------------------------------------------> */
    public static TextArea text = new TextArea();
    @Override
    public void start(Stage primaryStage) {
        Group trackerGroup = new Group();
        Camera cam = new PerspectiveCamera();
        SubScene subScene = new SubScene(trackerGroup, width, height, true, SceneAntialiasing.BALANCED);
        //initMouseControl(trackerGroup,subScene,primaryStage);
        initmouse(trackerGroup,subScene,primaryStage);
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
            ArrayList<Tracker> vizualiseRuleArr = new ArrayList<>();
            for (Tracker tracker : GridPage.theMainExtratorArr) {
                copyArrayList.add(deepCopyTracker(tracker));
            }
            if(!(text.getText().equals(""))){
                //Utils.UpdateSquares();
                getAllBacth(copyArrayList,text.getText());
                if(text.getText().contains("GROUPNAME")){
                    ArrayList<Circle> points = new ArrayList<>();
                    String textVal = text.getText();
                    createCirclesFromCoordinates(extractCornerLine(textVal),points);
                    Polygon poly = createPolyWithText(points);

                    MyTextGroup textGroup = convertGroups(textVal);

                    for (int i = 0; i < GridPage.theMainExtratorArr.size(); i++) {
                        if ((poly.contains(GridPage.theMainExtratorArr.get(i).x, GridPage.theMainExtratorArr.get(i).y) |
                                (poly.contains(GridPage.theMainExtratorArr.get(i).x + 5, GridPage.theMainExtratorArr.get(i).y + 5))|
                                (poly.contains(GridPage.theMainExtratorArr.get(i).x - 5, GridPage.theMainExtratorArr.get(i).y - 5))|
                                (poly.contains(GridPage.theMainExtratorArr.get(i).x + 5, GridPage.theMainExtratorArr.get(i).y - 5))|
                                (poly.contains(GridPage.theMainExtratorArr.get(i).x - 5, GridPage.theMainExtratorArr.get(i).y + 5))) &&
                                GridPage.theMainExtratorArr.get(i).batch == textGroup.batch){
                            GridPage.theMainExtratorArr.get(i).z = textGroup.valAxis / 10;
                            copyArrayList.get(i).z = textGroup.valAxis;
                            ruleArr.add(GridPage.theMainExtratorArr.get(i));
                            vizualiseRuleArr.add(copyArrayList.get(i));
                        }else {
                            if(GridPage.theMainExtratorArr.get(i).batch == textGroup.batch){
                                System.out.println("Not Inside --->:"+GridPage.theMainExtratorArr.get(i).x +" "+GridPage.theMainExtratorArr.get(i).y+"<---");
                            }
                        }
                    }
                    detectRule(textVal,ruleArr,vizualiseRuleArr);
                }
            }
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
                }else if (tracker.axis.equals("Y")){
                    Box box = new Box(10, 10, 10); // Set the size of the box as per your requirement
                    PhongMaterial material = new PhongMaterial();
                    material.setDiffuseColor(tracker.col); // Use the color of the tracker
                    box.setMaterial(material);
                    box.setTranslateX((width/2)+tracker.x); // Set the position of the box
                    box.setTranslateY((height/2)+tracker.z);
                    box.setTranslateZ(tracker.y);
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
    public static void detectRule(String input,ArrayList<Tracker> arr,ArrayList<Tracker> visualizeTracker){
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
                applyRuleForText(arr, r,visualizeTracker);
            }
        }
    }
    public static void applyRuleForText(ArrayList<Tracker> trackers, String rule,ArrayList<Tracker> visualizeTracker) {
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
        Rules.ApplyRule(trackers,myRule.rule,new Line(myRule.initX,myRule.initY,myRule.endX,myRule.endY));
        Rules.ApplyTextRule(visualizeTracker,myRule.rule,new Line(myRule.initX,myRule.initY,myRule.endX,myRule.endY));
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

    private void initmouse(Group root,SubScene scene,Stage stage){
        Rotate xrotate;
        Rotate yrotate;
        root.getTransforms().addAll(xrotate=new Rotate(0,Rotate.X_AXIS),yrotate=new Rotate(0,Rotate.Y_AXIS));
        xrotate.angleProperty().bind(anglX);
        yrotate.angleProperty().bind(anglY);
        xrotate.setPivotX(width/2); // Set the desired x-coordinate of the pivot point
        xrotate.setPivotY(height/2); // Set the desired y-coordinate of the pivot point

        yrotate.setPivotX(width/2);
        yrotate.setPivotY(height/2);
        scene.setOnMousePressed(mouseEvent -> {
            anchrX = mouseEvent.getScreenX();
            anchrY = mouseEvent.getScreenY();
            anchr_angX = anglX.get();
            anchr_angY = anglY.get();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            anglX.set(anchr_angX - (anchrY - mouseEvent.getScreenY()));
            anglY.set(anchr_angY + (anchrX - mouseEvent.getScreenX()));
        });
        stage.addEventHandler(ScrollEvent.SCROLL , event -> {
            double delta = event.getDeltaY();
            root.translateZProperty().set(root.getTranslateZ() + delta);
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
