package Grid;

import ColorsPaletteExtraction.Extractor;
import ColorsPaletteExtraction.Tracker;
import Editor.EditorSettings;
import ImageTaker.GetMyImageAlongAxe;
import Selector.ColorSelector;
import Selector.LineSelector;
import Selector.SquareSelector;
import groupCreatorSyntax.CodingEditor;
import groupCreatorSyntax.SyntaxDetector;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static Grid.PositionUtility.*;
import static Selector.SquareSelector.*;
import static javafx.application.Application.launch;

public class GridPage extends Application{
    public static ArrayList <Tracker> theMainExtratorArr = new ArrayList<>();/**/
    public static ArrayList<ArrayList<Tracker>> arrLOfGroup = new ArrayList<>();
    public static ArrayList<String> allGroupName = new ArrayList<>();
    public Button loadAnother = new Button("Load another scene");
    public static Color c = Color.BLACK;
    public static ArrayList<myImagePosition> savedArray = new ArrayList<myImagePosition>();
    public static int [] createOption = {0,0};
    public static String axis1 = "";
    public static String axis2 = "";
    public void start(Stage Stage) throws Exception{
        Group root = new Group();
        CheckBox con = new CheckBox("Connect");
        CheckBox select = new CheckBox("Select");
        con.setOnAction(action -> {
            if(!con.isSelected()){
                connect = new int[][]{{},{}};
                System.out.println(Arrays.deepToString(connect));
            }
        });
        Pane p1 = makeGrid(40,1,con,select);
        Pane p2 = makeGrid(40,2,con,select);
        p2.setLayoutX(450);
        p1.setOnMouseClicked(e -> {
            int x = (int)e.getX()/10;
            int y = (int)e.getY()/10;
            if(GridPage.createOption[0] == 0){
                xorz = 1;
                DrawSquare(p1,(int)e.getX()/10,(int)e.getY()/10,con,select);
            }else if(GridPage.createOption[1] == 1){
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                int mouseX = (int) mouseLocation.getX();
                int mouseY = (int) mouseLocation.getY();

                // Capture a screenshot
                BufferedImage screenCapture = robot.createScreenCapture(new java.awt.Rectangle(mouseX, mouseY, 1, 1));
                java.awt.Color pixelColor = new java.awt.Color(screenCapture.getRGB(0, 0));

                ColorSelector.setColor(awtToJavaFXColor(pixelColor));
            }else if (GridPage.createOption[1] == 3){
                Pane toAdd = test(new Pane(), 400, 400, (int) p1.getLayoutX());

                p1.getChildren().add(toAdd);
            }else if(makeConnection){

                /*if(myConnector.size() < 2){
                    Tracker t = Tracker.findClosestTracker(theMainExtratorArr,e.getX(),e.getY());
                    myConnector.add(theMainExtratorArr.indexOf(t));
                    if(myConnector.size() == 2) {
                        Tracker.mergeTrackers(theMainExtratorArr, myConnector.get(0), myConnector.get(1));
                        makeConnection = false;
                        myConnector.clear();
                    }
                }*/
            }
        });
        p2.setOnMouseClicked(e -> {
            int x = (int)e.getX()/10;
            int y = (int)e.getY()/10;
            if(GridPage.createOption[0] == 0){
                xorz = 2;
                DrawSquare(p2,(int)e.getX()/10,(int)e.getY()/10,con,select);
            }else if(GridPage.createOption[1] == 1){
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                int mouseX = (int) mouseLocation.getX();
                int mouseY = (int) mouseLocation.getY();

                // Capture a screenshot
                BufferedImage screenCapture = robot.createScreenCapture(new java.awt.Rectangle(mouseX, mouseY, 1, 1));
                java.awt.Color pixelColor = new java.awt.Color(screenCapture.getRGB(0, 0));

                ColorSelector.setColor(awtToJavaFXColor(pixelColor));
            }else if (GridPage.createOption[1] == 3){
                Pane toAdd = test(new Pane(), 400, 400, (int) p2.getLayoutX());

                p2.getChildren().add(toAdd);
            }else if(makeConnection){
                /*if(myConnector.size() < 2){
                    Tracker t = Tracker.findClosestTracker(theMainExtratorArr,e.getX()+ p2.getLayoutX(),e.getY());
                    myConnector.add(theMainExtratorArr.indexOf(t));
                    if(myConnector.size() == 2) {
                        Tracker.mergeTrackers(theMainExtratorArr, myConnector.get(0), myConnector.get(1));
                        myConnector.clear();

                        makeConnection = false;
                    }
                }*/
            }
        });
        root.getChildren().add(p1);
        root.getChildren().add(p2);
        TextField text = new TextField();


        ColorPicker cp = new ColorPicker(Color.BLUE);
        Button sameZ = new Button("Apply same z");
        Button createGroups = new Button("Create Groups");
        Button currGroups = new Button("Check the current group");
        Button connectGroups = new Button("connect");

        sameZ.setLayoutY(550);
        sameZ.setLayoutX(440);
        createGroups.setLayoutX(870);
        createGroups.setLayoutY(50);
        currGroups.setLayoutX(870);
        currGroups.setLayoutY(100);
        connectGroups.setLayoutX(870);
        connectGroups.setLayoutY(150);
        loadAnother.setLayoutY(550);
        loadAnother.setLayoutX(740);
        text.setLayoutY(550);
        text.setLayoutX(540);
        loadAnother.setOnAction(event -> {
            try {
                Extractor.ResetVariables();
                Stage.close();
                Stage secondWindow = new Stage();
                GetMyImageAlongAxe e = new GetMyImageAlongAxe();
                e.start(secondWindow);
                secondWindow.setTitle("import Picture");
                secondWindow.show();
                GetMyImageAlongAxe.QuickSort(e.importer,secondWindow);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sameZ.setOnAction(value ->  {
            putZForSelect(selectedCubes, Integer.parseInt(text.getText()));
        });
        ScrollPane bigBox = new ScrollPane();
        Pane board = new Pane();
        connectGroups.setOnAction(actionEvent -> {
            makeConnection = !makeConnection;
            GridPage.createOption[0] = -1;
        });

        root.getChildren().add(createGroups);
        root.getChildren().add(currGroups);
        root.getChildren().add(connectGroups);
        cp.setLayoutX(25);
        cp.setLayoutY(550);
        con.setLayoutX(250);
        con.setLayoutY(550);
        select.setLayoutX(350);
        select.setLayoutY(550);

        createGroups.setOnAction(value ->  {
            /*connectGroups.setLayoutY(connectGroups.getLayoutY()+150);
            createOption[0] = 1;
            Group typeSelect = typeOfSelection(root,currGroups,p1,p2,b);
            typeSelect.setLayoutX(870);
            typeSelect.setLayoutY(100);
            currGroups.setLayoutY(250);
            root.getChildren().add(typeSelect);*/
        });
        currGroups.setOnAction(actionEvent -> {
            Button close = new Button("Close");
            close.setLayoutX(870);
            close.setLayoutY(550);
            Group lGroup = createGroups();
            root.getChildren().add(close);
            connectGroups.setLayoutY(close.getLayoutY());
            connectGroups.setLayoutX(close.getLayoutX()+50);
            close.setOnAction(actionEvent1 -> {
                connectGroups.setLayoutX(870);
                connectGroups.setLayoutY(150);
                root.getChildren().remove(lGroup);
                root.getChildren().remove(close);

            });
            root.getChildren().add(lGroup);
        });
        cp.setOnAction(new EventHandler() {
            public void handle(Event t) {
                c = cp.getValue();
            }
        });
        myImagePosition currPos = new myImagePosition();
        currPos.myCubes = Extractor.myCubes;
        currPos.axis = GetMyImageAlongAxe.chosenAxe;
        savedArray.add(currPos);
        addCubeToGrid(p1,p2);
        Scene scene = new Scene(root, 1100, 600);
        Stage.setTitle("grid");
        Stage.setScene(scene);
        //Stage.show();    Extractor line 302
    }
    public static Node mergedPanes(Pane p1 , Pane p2,String p1Axis,String p2Axis,int [] batches){
        Group root = new Group();
        CheckBox con = new CheckBox("Connect");
        CheckBox select = new CheckBox("Select");
        con.setOnAction(action -> {
            if(!con.isSelected()){
                connect = new int[][]{{},{}};
                System.out.println(Arrays.deepToString(connect));
            }
        });
        p2.setLayoutX(450);
        System.out.println(batches[0]+" | "+batches[1]);
        p1.setOnMouseClicked(e -> {
            int x = (int)e.getX()/10;
            int y = (int)e.getY()/10;
            if(GridPage.createOption[0] == 0){
                /*xorz = 1;
                DrawSquare(p1,(int)e.getX()/10,(int)e.getY()/10,con,select);*/
                System.out.println("X:"+(int)e.getX()/10+" Y:"+(int)e.getY()/10+" Axis"+axis1+" bacth: "+batches[0]);
            }else if(GridPage.createOption[1] == 1){
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                int mouseX = (int) mouseLocation.getX();
                int mouseY = (int) mouseLocation.getY();

                // Capture a screenshot
                BufferedImage screenCapture = robot.createScreenCapture(new java.awt.Rectangle(mouseX, mouseY, 1, 1));
                java.awt.Color pixelColor = new java.awt.Color(screenCapture.getRGB(0, 0));

                ColorSelector.setColor(awtToJavaFXColor(pixelColor));
            }else if (GridPage.createOption[1] == 3){
                Pane toAdd = test(new Pane(), 400, 400, (int) p1.getLayoutX());
                p1.getChildren().add(toAdd);
            }else if(makeConnection){
                if(myConnector.size() < 2){
                    Tracker t = Tracker.findClosestTracker(theMainExtratorArr,e.getX(),e.getY(),p1Axis,batches[0]);
                    myConnector.add(theMainExtratorArr.indexOf(t));
                    if(myConnector.size() == 2) {
                        addSquare(theMainExtratorArr.get(myConnector.get(0)),theMainExtratorArr.get(myConnector.get(1)),p1,p2,p1Axis);
                        Tracker.mergeTrackers(theMainExtratorArr, myConnector.get(0), myConnector.get(1));
                        makeConnection = false;
                        myConnector.clear();
                    }/**/
                }
            }
        });
        p2.setOnMouseClicked(e -> {
            int x = (int)e.getX()/10;
            int y = (int)e.getY()/10;
            if(GridPage.createOption[0] == 0){
                /*xorz = 2;
                DrawSquare(p2,(int)e.getX()/10,(int)e.getY()/10,con,select);*/

                System.out.println("X:"+(int)e.getX()/10+" Y:"+(int)e.getY()/10+" Axis"+axis2+" "+" bacth: "+batches[1]);
            }else if(GridPage.createOption[1] == 1){
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                int mouseX = (int) mouseLocation.getX();
                int mouseY = (int) mouseLocation.getY();

                // Capture a screenshot
                BufferedImage screenCapture = robot.createScreenCapture(new java.awt.Rectangle(mouseX, mouseY, 1, 1));
                java.awt.Color pixelColor = new java.awt.Color(screenCapture.getRGB(0, 0));

                ColorSelector.setColor(awtToJavaFXColor(pixelColor));
            }else if (GridPage.createOption[1] == 3){
                Pane toAdd = test(new Pane(), 400, 400, (int) p2.getLayoutX());
                p2.getChildren().add(toAdd);
            }else if(makeConnection){
                if(myConnector.size() < 2){
                    Tracker t = Tracker.findClosestTracker(theMainExtratorArr,e.getX(),e.getY(),p2Axis,batches[1]);
                    myConnector.add(theMainExtratorArr.indexOf(t));
                    if(myConnector.size() == 2) {
                        addSquare(theMainExtratorArr.get(myConnector.get(0)),theMainExtratorArr.get(myConnector.get(1)),p1,p2,p1Axis);
                        Tracker.mergeTrackers(theMainExtratorArr, myConnector.get(0), myConnector.get(1));
                        myConnector.clear();
                        makeConnection = false;
                    }/**/
                }
            }
        });
        root.getChildren().add(p1);
        root.getChildren().add(p2);
        TextField text = new TextField();
        ColorPicker cp = new ColorPicker(Color.BLUE);
        Button sameZ = new Button("Apply same z");
        Button createGroups = new Button("Create Groups");
        Button currGroups = new Button("Check the current group");
        Button connectGroups = new Button("connect");
        Button useEditor = new Button("Use Editor");

        sameZ.setLayoutY(550);
        sameZ.setLayoutX(440);
        createGroups.setLayoutX(870);
        createGroups.setLayoutY(50);
        currGroups.setLayoutX(870);
        currGroups.setLayoutY(100);
        connectGroups.setLayoutX(390);
        connectGroups.setLayoutY(450);
        useEditor.setLayoutX(200);
        useEditor.setLayoutY(450);
        text.setLayoutY(550);
        text.setLayoutX(540);

        sameZ.setOnAction(value ->  {
            putZForSelect(selectedCubes, Integer.parseInt(text.getText()));
        });

        connectGroups.setOnAction(actionEvent -> {
            makeConnection = !makeConnection;
            GridPage.createOption[0] = -1;
        });
        root.getChildren().add(createGroups);
        root.getChildren().add(currGroups);
        root.getChildren().add(connectGroups);
        root.getChildren().add(useEditor);
        cp.setLayoutX(25);
        cp.setLayoutY(550);
        con.setLayoutX(250);
        con.setLayoutY(550);
        select.setLayoutX(350);
        select.setLayoutY(550);

        createGroups.setOnAction(value ->  {
            createOption[0] = 1;
            axis1 = p1Axis;
            axis2 = p2Axis;
            Group typeSelect = typeOfSelection(root,currGroups,p1,p2,batches);
            typeSelect.setLayoutX(870);
            typeSelect.setLayoutY(100);
            currGroups.setLayoutY(280);
            root.getChildren().add(typeSelect);
        });
        currGroups.setOnAction(actionEvent -> {
            Button close = new Button("Close");
            close.setLayoutX(870);
            close.setLayoutY(root.getBoundsInLocal().getHeight() - (close.getHeight()-25));
            Group lGroup = createGroups();
            root.getChildren().add(close);
            close.setOnAction(actionEvent1 -> {
                root.getChildren().remove(lGroup);
                root.getChildren().remove(close);
            });
            root.getChildren().add(lGroup);
        });
        cp.setOnAction(new EventHandler() {
            public void handle(Event t) {
                c = cp.getValue();
            }
        });
        useEditor.setOnAction(openEditorEvent -> {
            /*Stage n = new Stage();
            SyntaxDetector s = new SyntaxDetector();
            s.start(n);*/
            Stage n = new Stage();
            CodingEditor s = new CodingEditor();
            s.start(n);
        });
        return root;
    }
    public static Pane exportPane(String axis, int batch){

        Group root = new Group();
        CheckBox con = new CheckBox("Connect");
        CheckBox select = new CheckBox("Select");
        con.setOnAction(action -> {
            if(!con.isSelected()){
                connect = new int[][]{{},{}};
                System.out.println(Arrays.deepToString(connect));
            }
        });
        Pane p1 = makeGrid(40,1,con,select);
        p1.setOnMouseClicked(e -> {
            int x = (int)e.getX()/10;
            int y = (int)e.getY()/10;
            if(GridPage.createOption[0] == 0){
                /**/xorz = 1;
                DrawSquare(p1,(int)e.getX()/10,(int)e.getY()/10,con,select);
            }else if(GridPage.createOption[1] == 1){
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                int mouseX = (int) mouseLocation.getX();
                int mouseY = (int) mouseLocation.getY();

                // Capture a screenshot
                BufferedImage screenCapture = robot.createScreenCapture(new java.awt.Rectangle(mouseX, mouseY, 1, 1));
                java.awt.Color pixelColor = new java.awt.Color(screenCapture.getRGB(0, 0));

                ColorSelector.setColor(awtToJavaFXColor(pixelColor));
            }else if (GridPage.createOption[1] == 3){
                Pane toAdd = test(new Pane(), 400, 400, (int) p1.getLayoutX());
                p1.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.BLUE, null, null)));
                p1.getChildren().add(toAdd);
            }else if(makeConnection){
                if(myConnector.size() < 2){
                    Tracker t = Tracker.findClosestTracker(theMainExtratorArr,e.getX(),e.getY(),axis,batch);
                    myConnector.add(theMainExtratorArr.indexOf(t));
                    if(myConnector.size() == 2) {
                        Tracker.mergeTrackers(theMainExtratorArr, myConnector.get(0), myConnector.get(1));
                        makeConnection = false;
                        myConnector.clear();
                    }
                }
            }
        });
        root.getChildren().add(p1);

        con.setLayoutX(250);
        con.setLayoutY(550);
        select.setLayoutX(350);
        select.setLayoutY(550);

        myImagePosition currPos = new myImagePosition();
        currPos.myCubes = Extractor.myCubes;
        currPos.axis = GetMyImageAlongAxe.chosenAxe;
        savedArray.add(currPos);
        addCubeToOneGrid(p1, currPos.axis, batch);

        return p1;
    }
    public static void main(String[] args) {
        launch(args);
    }
    public static void QuickShort(Button b,Stage s){
        GetMyImageAlongAxe.count += 1;
        if(GetMyImageAlongAxe.count <= 1){  //ACTIVATE AND EXECUTE AUTOMATICALLLY
            b.fire();
        }
    }
    public static Group typeOfSelection(Group p,Button curr,Pane p1,Pane p2,int [] batchs){
        Group g = new Group();
        Text t = new Text("Select your selector :");
        CheckBox byColor = new CheckBox("color");
        CheckBox byLine = new CheckBox("Line");
        CheckBox bySquare = new CheckBox("Square");
        Button finish = new Button("Finished");

        byColor.setOnAction(action -> {
            if(byColor.isSelected()){
                ColorSelector addColorSection = new ColorSelector();
                g.getChildren().add(addColorSection.myCol);
                addColorSection.addAllCubes.setOnAction(action1 -> {
                    g.getChildren().remove(addColorSection.myCol);
                    finish.setLayoutY(120);
                    curr.setLayoutY(250);
                    createOption[1] = 0;
                    byColor.setSelected(false);
                });
                curr.setLayoutY(350);
                finish.setLayoutY(215);
                createOption[1] = 1;
            }
        });
        byLine.setOnAction(actionEvent -> {
            if(byLine.isSelected()){
                TextField inputGroupName = new TextField();
                inputGroupName.setPromptText("Type something...");
                LineSelector addLineSection = new LineSelector();
                g.getChildren().add(inputGroupName);inputGroupName.setLayoutX(0);inputGroupName.setLayoutY(285);
                g.getChildren().add(addLineSection.myLin);
                addLineSection.ajouter.setOnAction(action1 -> {
                    allGroupName.add(inputGroupName.getText());
                    g.getChildren().remove(inputGroupName);
                    addLineSection.addGroup(addLineSection.polygon,p2,batchs);
                    g.getChildren().remove(addLineSection.myLin);
                    finish.setLayoutY(120);
                    curr.setLayoutY(250);
                    createOption[1] = 0;
                    byLine.setSelected(false);
                });
                curr.setLayoutY(350);
                finish.setLayoutY(215);
                createOption[1] = 2;
            }
        });
        bySquare.setOnAction(actionEvent -> {
            if(bySquare.isSelected()){
                TextField inputGroupName = new TextField();
                inputGroupName.setPromptText("Type something...");
                g.getChildren().add(inputGroupName);inputGroupName.setLayoutX(0);inputGroupName.setLayoutY(185);
                Group squareSelect = new SquareSelector().intiateLine();
                g.getChildren().add(squareSelect);
                SquareSelector.done.setOnAction(actionEvent1 -> {
                    allGroupName.add(inputGroupName.getText());
                    g.getChildren().remove(inputGroupName);
                    getAllCubeWithin(theMainExtratorArr,startX,startY,endX,endY,batchs);
                    g.getChildren().remove(squareSelect);
                    finish.setLayoutY(120);
                    curr.setLayoutY(250);
                    createOption[1] = 0;
                    bySquare.setSelected(false);
                });
                curr.setLayoutY(350);
                finish.setLayoutY(215);
                createOption[1] = 3;
            }
        });

        g.getChildren().add(t);g.getChildren().add(byColor);g.getChildren().add(byLine);
        g.getChildren().add(bySquare);g.getChildren().add(finish);
        byColor.setLayoutY(25);byLine.setLayoutY(50);bySquare.setLayoutY(75);finish.setLayoutY(140);
        byColor.setLayoutX(25);byLine.setLayoutX(25);bySquare.setLayoutX(25);t.setLayoutX(10);
        finish.setLayoutX(0);
        /**/
        finish.setOnAction(value ->  {
            p.getChildren().remove(g);
            curr.setLayoutY(100);
            createOption[0] = 0;
            createOption[1] = 0;
        });
        return g;
    }
}

class myImagePosition{
    public ArrayList<Tracker> myCubes = new ArrayList<>();
    String axis = "";
}
