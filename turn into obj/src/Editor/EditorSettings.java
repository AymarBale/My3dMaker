package Editor;

import Grid.GridPage;
import ImageTaker.GetMyImageAlongAxe;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Grid.GridPage.theMainExtratorArr;
import static Grid.PositionUtility.*;
import static Utils3DCreation.com.Utils.Create3DObject;
import static Utils3DCreation.com.Utils.cubesXZ;

public class EditorSettings extends Application {
    public static ArrayList<Pane> spliter = new ArrayList<>();
    private static boolean listenForMerge = false;
    private static ArrayList<String> arrL = new ArrayList<>();
    public static TabPane tabPane = new TabPane();
    Button sub = new Button("SUBMIT");
    static Group g = new Group();
    public static Scene scene = new Scene(g, 600, 600);
    public static Button btn = new Button("+");
    public static AtomicReference<Tab> addedTab = new AtomicReference<>(new Tab());
    public static String mergedAxe = "";
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Multiple Tab Window");



        int i = 0;
        AtomicInteger finalI = new AtomicInteger(i);

        finalI.getAndIncrement();
        tabPane.setPrefSize(500,500);
        g.getChildren().add(tabPane);
        g.getChildren().add(btn);
        g.getChildren().add(sub);
        btn.setLayoutX(550);

        btn.setOnAction(value ->  {
            Tab tab = createTab(finalI,tabPane,primaryStage);
            finalI.getAndIncrement();
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);
        });
        sub.setOnAction(value ->  {

            callOn3dCreation();
            Create3DObject();

        });
        sub.setLayoutY(550);
        sub.setLayoutX(525);


        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static Tab createTab(AtomicInteger i,TabPane tabPane,Stage a){
        Tab tab = new Tab("Tab  "+ i);
        Stage s = new Stage();
        GetMyImageAlongAxe g = new GetMyImageAlongAxe();
        g.start(s);
        tab.setContent(new Pane());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem merge = new MenuItem("merge");
        MenuItem mSplit = new MenuItem("Split");



        merge.setOnAction((event) -> {
            listenForMerge = true;
        });
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (listenForMerge && (newTab != null)) {
                spliter.clear();
                Pane p1 = new Pane();
                Pane p2 = new Pane();
                String p1Axis = extractAxis(oldTab.getText());
                String p2Axis = extractAxis(newTab.getText());
                if ((oldTab.getContent() instanceof Pane) &&(newTab.getContent() instanceof Pane )){
                    p1 = (Pane)oldTab.getContent();
                    p2 = (Pane) newTab.getContent();
                }
                spliter.add(p1);
                spliter.add(p2);
                addedTab.set(getTabByIndex(tabPane, tabPane.getTabs().indexOf(newTab)));
                tabPane.getTabs().remove(tabPane.getTabs().indexOf(newTab));
                if(oldTab != null){
                    arrL.clear();
                    arrL.add(oldTab.getText());
                    arrL.add(newTab.getText());
                    tabPane.setPrefSize(1100,600);
                    oldTab.setText(oldTab.getText()+" + "+newTab.getText());
                    mergedAxe = extractCharacter(oldTab.getText())+extractCharacter(newTab.getText());
                    oldTab.setContent(new Pane());
                    oldTab.setContent(GridPage.mergedPanes(p1, p2,p1Axis,p2Axis));/**/
                    oldTab.setStyle("-fx-text-base-color: green;");
                    a.setWidth(1100);
                    btn.setLayoutX(1050);
                }
                listenForMerge = false;
            }
        });
        mSplit.setOnAction((event) -> {
            mergedAxe = "";
            int secondNumber = extractSecondNumber(tab.getText());
            tabPane.setPrefSize(500,600);
            tab.setContent((Node)spliter.get(0));
            tab.setText(arrL.get(0));
            tab.setStyle("-fx-text-base-color: black;");
            Tab otherTab = addedTab.get();
            otherTab.setText(arrL.get(1));
            otherTab.setContent((Node)spliter.get(1));
            tabPane.getTabs().add(secondNumber-1,otherTab);
            a.setWidth(600);
            btn.setLayoutX(550);
        });
        contextMenu.getItems().addAll(merge,mSplit);
        tab.setContextMenu(contextMenu);
        return tab;
    }
    private static int extractSecondNumber(String input) {
        Pattern pattern = Pattern.compile("\\b\\d+\\b");
        Matcher matcher = pattern.matcher(input);

        for (int i = 0; i < 2 && matcher.find(); i++) {
            if (i == 1) {
                return Integer.parseInt(matcher.group());
            }
        }
        return -1;
    }

    private static Tab getTabByIndex(TabPane tabPane, int index) {
        if (index >= 0 && index < tabPane.getTabs().size()) {
            return tabPane.getTabs().get(index);
        }
        return null;
    }

    private static String extractCharacter(String inputString) {
        int colonIndex = inputString.indexOf(':');

        if (colonIndex != -1 && colonIndex + 1 < inputString.length()) {
            // Check if ':' is found and there is a character after it
            return String.valueOf(inputString.charAt(colonIndex + 1));
        } else {
            // Handle the case where ':' is not found or it's the last character
            // You may choose to throw an exception, return a default value, or handle it differently based on your requirements
            throw new IllegalArgumentException("Invalid input string");
        }
    }

    public static String extractAxis(String inputString) {
        // Define the pattern for matching the AXIS:X format
        Pattern pattern = Pattern.compile("AXIS:(\\w+)");

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(inputString);

        // Check if the pattern is found
        if (matcher.find()) {
            // Return the matched axis value
            return matcher.group(1);
        } else {
            // Return null if no match is found
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

