package Grid;

import ColorsPaletteExtraction.Extractor;
import ColorsPaletteExtraction.Tracker;
import ImageTaker.GetMyImageAlongAxe;
import Selector.ColorSelector;
import Selector.LineSelector;
import Selector.SquareSelector;
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
    public Button loadAnother = new Button("Load another scene");
    public static Color c = Color.BLACK;
    public static ArrayList<myImagePosition> savedArray = new ArrayList<myImagePosition>();
    public static int [] createOption = {0,0};

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
                p1.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.BLUE, null, null)));
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
                p2.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.BLUE, null, null)));
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
        /*root.getChildren().add(loadAnother);
        root.getChildren().add(sameZ);
        root.getChildren().add(text);
        root.getChildren().add(con);
        root.getChildren().add(select);
        root.getChildren().add(cp);*/
        ScrollPane bigBox = new ScrollPane();
        Pane board = new Pane();
        connectGroups.setOnAction(actionEvent -> {
            makeConnection = !makeConnection;
            GridPage.createOption[0] = -1;
        });

/*
        Group info = new Group();
        Rectangle infoRect = new Rectangle(150,75);
        Text textInfoX = new Text("X:");textInfoX.setLayoutX(20);
        textInfoX.setFill(Color.RED);textInfoX.setLayoutY(30);
        textInfoX.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Text textInfoY = new Text("Y:");textInfoY.setLayoutX(20);
        textInfoY.setFill(Color.BLUE);textInfoY.setLayoutY(50);
        textInfoY.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Text textInfoZ = new Text("Z:");textInfoZ.setLayoutX(20);
        textInfoZ.setFill(Color.GREEN);textInfoZ.setLayoutY(70);
        textInfoZ.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Text textInfoColor = new Text("Col : ");textInfoColor.setLayoutX(70);
        textInfoColor.setFill(Color.WHITE);textInfoColor.setLayoutY(70);
        textInfoColor.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));

        info.setLayoutX(50);
        info.setLayoutY(50);
        infoRect.setFill(Color.GOLD);
        info.getChildren().add(infoRect);info.getChildren().add(textInfoX);
        info.getChildren().add(textInfoY);info.getChildren().add(textInfoZ);
        info.getChildren().add(textInfoColor);
        //board.getChildren().add(info);

        Rectangle cubeBoard = new Rectangle(1000,175);
        Text boardText = new Text("all cubes on board");
        boardText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        boardText.setFill(Color.WHITESMOKE);

        cubeBoard.setFill(Color.BLACK);

        bigBox.setLayoutX(50);
        bigBox.setLayoutY(410);
        board.getChildren().add(cubeBoard);board.getChildren().add(boardText);board.getChildren().add(info);
        boardText.setLayoutY(30);
        boardText.setLayoutX(20);
        bigBox.setContent(board);
        root.getChildren().add(bigBox);
*/
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
            connectGroups.setLayoutY(connectGroups.getLayoutY()+150);
            createOption[0] = 1;
            Group typeSelect = typeOfSelection(root,currGroups,connectGroups);
            typeSelect.setLayoutX(870);
            typeSelect.setLayoutY(100);
            currGroups.setLayoutY(250);
            root.getChildren().add(typeSelect);
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
    public static Node mergedPanes(Pane p1 , Pane p2,String p1Axis,String p2Axis){
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
                p1.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.BLUE, null, null)));
                p1.getChildren().add(toAdd);
            }else if(makeConnection){
                if(myConnector.size() < 2){
                    Tracker t = Tracker.findClosestTracker(theMainExtratorArr,e.getX(),e.getY(),p1Axis);
                    System.out.println(t.x+" "+t.y);
                    myConnector.add(theMainExtratorArr.indexOf(t));
                    if(myConnector.size() == 2) {
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
                p2.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.BLUE, null, null)));
                p2.getChildren().add(toAdd);
            }else if(makeConnection){
                if(myConnector.size() < 2){

                    Tracker t = Tracker.findClosestTracker(theMainExtratorArr,e.getX(),e.getY(),p2Axis);
                    System.out.println(t.x+" "+t.y);
                    myConnector.add(theMainExtratorArr.indexOf(t));
                    if(myConnector.size() == 2) {
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

        sameZ.setLayoutY(550);
        sameZ.setLayoutX(440);
        createGroups.setLayoutX(870);
        createGroups.setLayoutY(50);
        currGroups.setLayoutX(870);
        currGroups.setLayoutY(100);
        connectGroups.setLayoutX(870);
        connectGroups.setLayoutY(150);
        text.setLayoutY(550);
        text.setLayoutX(540);

        sameZ.setOnAction(value ->  {
            putZForSelect(selectedCubes, Integer.parseInt(text.getText()));
        });
        /*root.getChildren().add(loadAnother);
        root.getChildren().add(sameZ);
        root.getChildren().add(text);
        root.getChildren().add(con);
        root.getChildren().add(select);
        root.getChildren().add(cp);
        ScrollPane bigBox = new ScrollPane();
        Pane board = new Pane();*/

        connectGroups.setOnAction(actionEvent -> {
            makeConnection = !makeConnection;
            GridPage.createOption[0] = -1;
        });
/*
        Group info = new Group();
        Rectangle infoRect = new Rectangle(150,75);
        Text textInfoX = new Text("X:");textInfoX.setLayoutX(20);
        textInfoX.setFill(Color.RED);textInfoX.setLayoutY(30);
        textInfoX.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Text textInfoY = new Text("Y:");textInfoY.setLayoutX(20);
        textInfoY.setFill(Color.BLUE);textInfoY.setLayoutY(50);
        textInfoY.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Text textInfoZ = new Text("Z:");textInfoZ.setLayoutX(20);
        textInfoZ.setFill(Color.GREEN);textInfoZ.setLayoutY(70);
        textInfoZ.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Text textInfoColor = new Text("Col : ");textInfoColor.setLayoutX(70);
        textInfoColor.setFill(Color.WHITE);textInfoColor.setLayoutY(70);
        textInfoColor.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));

        info.setLayoutX(50);
        info.setLayoutY(50);
        infoRect.setFill(Color.GOLD);
        info.getChildren().add(infoRect);info.getChildren().add(textInfoX);
        info.getChildren().add(textInfoY);info.getChildren().add(textInfoZ);
        info.getChildren().add(textInfoColor);
        //board.getChildren().add(info);

        Rectangle cubeBoard = new Rectangle(1000,175);
        Text boardText = new Text("all cubes on board");
        boardText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        boardText.setFill(Color.WHITESMOKE);

        cubeBoard.setFill(Color.BLACK);

        bigBox.setLayoutX(50);
        bigBox.setLayoutY(410);
        board.getChildren().add(cubeBoard);board.getChildren().add(boardText);board.getChildren().add(info);
        boardText.setLayoutY(30);
        boardText.setLayoutX(20);
        bigBox.setContent(board);
        root.getChildren().add(bigBox);
*/
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
            connectGroups.setLayoutY(connectGroups.getLayoutY()+150);
            createOption[0] = 1;
            Group typeSelect = typeOfSelection(root,currGroups,connectGroups);
            typeSelect.setLayoutX(870);
            typeSelect.setLayoutY(100);
            currGroups.setLayoutY(250);
            root.getChildren().add(typeSelect);
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

        return root;
    }
    public static Pane exportPane(String axis){

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
                p1.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.BLUE, null, null)));
                p1.getChildren().add(toAdd);
            }else if(makeConnection){

                /**/if(myConnector.size() < 2){
                    Tracker t = Tracker.findClosestTracker(theMainExtratorArr,e.getX(),e.getY(),axis);
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
        addCubeToOneGrid(p1, currPos.axis);

        return p1;
    }
    public static void main(String[] args) {
        launch(args);
    }
    public static void QuickShort(Button b,Stage s){
        /**/GetMyImageAlongAxe.count += 1;
        if(GetMyImageAlongAxe.count <= 1){  //ACTIVATE AND EXECUTE AUTOMATICALLLY
            b.fire();
        }
    }
    /*
    public static Group addCubesToBoard(Group info){
        Rectangle infoRect = new Rectangle(150,75);
        Text textInfoX = new Text("X:");textInfoX.setLayoutX(20);
        textInfoX.setFill(Color.RED);textInfoX.setLayoutY(30);
        textInfoX.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Text textInfoY = new Text("Y:");textInfoY.setLayoutX(20);
        textInfoY.setFill(Color.BLUE);textInfoY.setLayoutY(50);
        textInfoY.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Text textInfoZ = new Text("Z:");textInfoZ.setLayoutX(20);
        textInfoZ.setFill(Color.GREEN);textInfoZ.setLayoutY(70);
        textInfoZ.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        Text textInfoColor = new Text("Col : ");textInfoColor.setLayoutX(70);
        textInfoColor.setFill(Color.WHITE);textInfoColor.setLayoutY(70);
        textInfoColor.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));

        info.setLayoutX(50);
        info.setLayoutY(50);
        infoRect.setFill(Color.GOLD);
        info.getChildren().add(infoRect);info.getChildren().add(textInfoX);
        info.getChildren().add(textInfoY);info.getChildren().add(textInfoZ);
        info.getChildren().add(textInfoColor);
        return info;
    }*/
    public static Group typeOfSelection(Group p,Button curr,Button connect){
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
                LineSelector addLineSection = new LineSelector();
                g.getChildren().add(addLineSection.myLin);
                addLineSection.ajouter.setOnAction(action1 -> {
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
                Group squareSelect = new SquareSelector().intiateLine();
                g.getChildren().add(squareSelect);
                connect.setLayoutY(connect.getLayoutY()+100);
                SquareSelector.done.setOnAction(actionEvent1 -> {
                    getAllCubeWithin(theMainExtratorArr,startX,startY,endX,endY);
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
        byColor.setLayoutY(25);byLine.setLayoutY(50);bySquare.setLayoutY(75);finish.setLayoutY(120);
        byColor.setLayoutX(25);byLine.setLayoutX(25);bySquare.setLayoutX(25);t.setLayoutX(10);finish.setLayoutX(0);
        finish.setOnAction(value ->  {
            p.getChildren().remove(g);
            curr.setLayoutY(100);
            createOption[0] = 0;
            createOption[1] = 0;
            connect.setLayoutY(connect.getLayoutY()-250);
        });
        return g;
    }
}

class myImagePosition{
    public ArrayList<Tracker> myCubes = new ArrayList<>();
    String axis = "";
}
