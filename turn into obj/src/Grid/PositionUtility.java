package Grid;

import ColorsPaletteExtraction.Extractor;
import ColorsPaletteExtraction.Tracker;
import Editor.EditorSettings;
import Selector.ColorSelector;
import Utils3DCreation.com.Rules;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static Grid.GridPage.*;
import static Selector.SquareSelector.test;
import static Utils3DCreation.com.Utils.*;


public class PositionUtility {
    public static int [][] connect = {{},{}};
    public static int [][] selectedCubes = {};
    public static int xorz = 0;
    public static boolean makeConnection = false;
    public static ArrayList<Integer> myConnector = new ArrayList<>();
    public static Pane makeGrid(int n, int x_z, CheckBox c,CheckBox s){
        double width = 10;
        Pane pane = new Pane();
        Rectangle[][] rec = new Rectangle [n][n];

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                rec[i][j] = new Rectangle();
                rec[i][j].setX(i * width);
                rec[i][j].setY(j * width);
                rec[i][j].setWidth(width);
                rec[i][j].setHeight(width);
                rec[i][j].setFill(null);
                rec[i][j].setStroke(Color.BLACK);
                pane.getChildren().add(rec[i][j]);
            }
        }
        return pane;
    }
    public static Pane makeGridForMini(int n){
        double width = 10;
        Pane pane = new Pane();
        Rectangle[][] rec = new Rectangle [n][n];

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                rec[i][j] = new Rectangle();
                rec[i][j].setX(i * width);
                rec[i][j].setY(j * width);
                rec[i][j].setWidth(width);
                rec[i][j].setHeight(width);
                rec[i][j].setFill(null);
                rec[i][j].setStroke(Color.BLACK);
                pane.getChildren().add(rec[i][j]);
            }
        }
        return pane;
    }


    public static Pane DrawSquare(Pane a,int x,int y,CheckBox con,CheckBox sel){
        Rectangle rect = new Rectangle(8,8);
        rect.setFill(c);
        rect.setX((x+0.1)*10);
        rect.setY((y+0.1)*10);
        a.getChildren().add(rect);
        rect.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        if(con.isSelected()){
                            int [] d = {(int)e.getX()/10,(int)e.getY()/10};
                            connect= PutInArray(connect,d);

                        }else if(sel.isSelected()){
                            int [] d = {(int)e.getX()/10,(int)e.getY()/10};
                            selectedCubes = AddCubesToArray(selectedCubes,d[0],d[1],0);
                            System.out.println(Arrays.deepToString(selectedCubes));
                        }
                    }
        });
/*
        if(xorz == 1){
            cubesX = AddCubesToArray(cubesX,x,y,0);
        }
        if(xorz == 2){
            cubesZ = AddCubesToArray(cubesZ,x,y,0);
        }*/
        return a;
    }

    public static int [][] AddCubesToArray(int [][]data,int x,int y,int z){
        int [] d = new int[]{x,y,z};
        int [][] arr = new int[data.length+1][3];
        for (int i = 0; i < data.length; i++) {
            arr[i] = data[i];
        }
        arr[data.length] = d;
        return arr;
    }

    public static int[] PutZValue(int [][]arr){
        int [] arra = {arr[0][0],arr[0][1],arr[1][0]};
        return arra;
    }

    public static int[][] PutInArray(int [][]data,int []d){
        if(data[0].length == 0) {
            data[0] = d;
        }else if(data[1].length == 0){
            data[1] = d;
            int [] f = PutZValue(connect);
            cubesX = AddCubesToArray(cubesX,f[0],f[1],f[2]);
        }
        return data;
    }

    public static int[][] putZForSelect(int [][]data,int z){
        for(int i = 0 ; i < data.length ; i++){
            data[i][2] = z;
        }
        return data;
    }

    public static void addCubeToGrid(Pane p1,Pane p2){
        theMainExtratorArr.clear();
        allColArr.clear();

        for (int j = 0; j < savedArray.size(); j++) {
            Pane p = p1;
            int selectedAxis = 0;
            ArrayList <Tracker> receivedCubes = savedArray.get(j).myCubes;
            if (savedArray.get(j).axis.equals("Z")) {
                p = p2;
                selectedAxis += p.getLayoutX();

            }
            for (int i = 0; i < receivedCubes.size(); i++) {

                c = receivedCubes.get(i).col;
                int plusForEarPig = 0;
                if(selectedAxis != 0){
                    plusForEarPig = 10;
                }
                p.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, transformPosition(savedArray.get(j).myCubes).get(i).x + selectedAxis, transformPosition(savedArray.get(j).myCubes).get(i).y+plusForEarPig, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
                arrCol.add(c);
                allColArr.add(receivedCubes.get(i).col);
                theMainExtratorArr.add(new Tracker(transformPosition(savedArray.get(j).myCubes).get(i).x + selectedAxis,transformPosition(savedArray.get(j).myCubes).get(i).y+plusForEarPig,0,c));
            }
            arrCol = (ArrayList<Color>) uniqueColors(arrCol);
        }
    }

    public static void addCubeToOneGrid(Pane p1,String axis,int id){
        int count = 0;
        Pane p = p1;
        int selectedAxis = 0;
        ArrayList <Tracker> receivedCubes = savedArray.get(savedArray.size()-1).myCubes;

        for (int i = 0; i < receivedCubes.size(); i++) {
            c = receivedCubes.get(i).col;
            int plusForEarPig = 0;
            if(selectedAxis != 0){
                plusForEarPig = 10;
            }
            p.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, transformPosition(savedArray.get(savedArray.size()-1).myCubes).get(i).x + selectedAxis, transformPosition(savedArray.get(savedArray.size()-1).myCubes).get(i).y+plusForEarPig, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
            arrCol.add(c);
            allColArr.add(receivedCubes.get(i).col);
            theMainExtratorArr.add(new Tracker(transformPosition(savedArray.get(savedArray.size()-1).myCubes).get(i).x + selectedAxis,transformPosition(savedArray.get(savedArray.size()-1).myCubes).get(i).y+plusForEarPig,0,c,axis,id));
            count += 1;
        }
        arrCol = (ArrayList<Color>) uniqueColors(arrCol);
    }

    public static ArrayList<Tracker> transformPosition(ArrayList<Tracker> receivedCubes){
        ArrayList<Tracker> newCubes = new ArrayList<>();
        ArrayList<Integer> arrX = new ArrayList<Integer>();
        ArrayList<Integer> arrY = new ArrayList<Integer>();
        for (int i = 0; i < receivedCubes.size(); i++){
            arrX.add((int)(receivedCubes.get(i).x/Extractor.additiveX)*10);
            arrY.add((int)(receivedCubes.get(i).y/Extractor.additiveX)*10);
        }
        for (int i = 0; i < arrX.size(); i++) {
            Tracker t = new Tracker(arrX.get(i),arrY.get(i),0,receivedCubes.get(i).col);
            newCubes.add(t);
        }
        return newCubes;
    }

    public static List<Color> uniqueColors(ArrayList<Color> colors) {
        Set<Color> uniqueSet = new HashSet<>();
        List<Color> result = new ArrayList<>();

        for (Color color : colors) {
            if (!uniqueSet.contains(color)) {
                uniqueSet.add(color);
                result.add(color);
            }
        }
        return result;
    }

    public static String findCorrectMaterial(Color a){
        String mat = "usemtl Material";
        for (int i = 0; i < arrCol.size(); i++) {
            if(a.equals(arrCol.get(i))){
                mat += i;
            }
        }
        return mat;
    }

    public static void printArrayList(ArrayList<Tracker> arr){
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i).batch +"  "+arr.get(i).z);
        }
    }

    public static Color awtToJavaFXColor(java.awt.Color awtColor) {
        double red = awtColor.getRed();
        double green = awtColor.getGreen();
        double blue = awtColor.getBlue();
        double alpha = awtColor.getAlpha() / 255.0; // Normalize alpha value

        return new Color(red/255, green/255, blue/255, alpha);
    }

    public static void getAllCubeWithin(ArrayList <Tracker> theMainExtratorArr,double sX,double sY,double eX,double eY,int [] batchs){
        ArrayList <Tracker> t = new ArrayList<>();
        int num = 0;
        for (int i = 0; i < theMainExtratorArr.size(); i++) {
            double tempSX = sX;  // Temporary variable for modified sX
            double tempEX = eX;
            if(theMainExtratorArr.get(i).batch == batchs[0]){
                if((theMainExtratorArr.get(i).x >= tempSX)&(theMainExtratorArr.get(i).y >= sY)&(theMainExtratorArr.get(i).x <= tempEX)&(theMainExtratorArr.get(i).y <= eY)
                &(theMainExtratorArr.get(i).x != 0)&(theMainExtratorArr.get(i).batch == batchs[0])){
                    num += 1;
                    t.add(theMainExtratorArr.get(i));
                }
            }else if(theMainExtratorArr.get(i).batch == batchs[1]){
                tempSX -= 450.00;
                tempEX -= 450.00;
                if((theMainExtratorArr.get(i).x >= tempSX)&(theMainExtratorArr.get(i).y >= sY)&(theMainExtratorArr.get(i).x <= tempEX)&(theMainExtratorArr.get(i).y <= eY)
                        &(theMainExtratorArr.get(i).x != 0)){
                    num += 1;
                    t.add(theMainExtratorArr.get(i));
                }
            }
        }
        arrLOfGroup.add(t);
    }

    public static Group createGroups(){
        Group mainGroup = new Group();
        Text t = new Text("List of all the groups\n" +
                " inside the array");
        for (int i = 0; i < arrLOfGroup.size(); i++) {
            ArrayList<Tracker> current = arrLOfGroup.get(i);
            Button visualizeBtn = new Button("+");
            ImageView imageView = new ImageView(PositionUtility.class.getResource("eye.png").toExternalForm());
            imageView.setFitWidth(10);
            imageView.setFitHeight(10);
            visualizeBtn.setMaxSize(10, 10);
            visualizeBtn.setGraphic(imageView);

            Group g = new Group();
            Text text = new Text();
            if(allGroupName.get(arrLOfGroup.indexOf(current)).equals("")){
                text.setText("Group number " + i);
            }else{
                text.setText(allGroupName.get(arrLOfGroup.indexOf(current)));
            }
            visualizeBtn.setOnAction(actionEvent -> {

                //----------------------------------
                ArrayList<Circle> arr = new ArrayList<>();
                Group miniGroup = new Group();
                Button cRule = new Button("Create rule");
                Text ruleListed = new Text();
                for (int j = 0; j < Rules.arr.size(); j++) {
                    ruleListed.setText(ruleListed.getText()+" "+Rules.arr.get(j));
                }
                final boolean[] ruleCreation = {false};
                cRule.setOnAction(event -> {
                    ruleCreation[0] = true;
                });
                TextField ruleVal = new TextField();
                Button done = new Button("Done");
                Button rulesList = new Button("rule history");
                done.setLayoutX(120);done.setLayoutY(80);
                rulesList.setLayoutX(180);rulesList.setLayoutY(80);
                ruleListed.setLayoutY(240);ruleListed.setLayoutY(10);
                final Line[] line = {null};
                done.setOnAction(doneEvent ->{
                    miniGroup.getChildren().removeIf(node -> node instanceof Circle || node instanceof Line);
                    arr.clear();
                    Rules.ApplyRule(current,ruleVal.getText(),line[0]);
                });
                rulesList.setOnAction(ruleEvent ->{
                    System.out.println("testing");
                    miniGroup.getChildren().add(ruleListed);
                });
                ruleVal.setLayoutY(40);
                cRule.setLayoutX(120);
                ruleVal.setLayoutX(120);
                miniGroup.getChildren().addAll(done,ruleVal,cRule,rulesList);
                addTrackersAsCubesToGroup(arrLOfGroup, arrLOfGroup.indexOf(current), miniGroup);
                Stage stage = new Stage();
                stage.setTitle("Visualizer window:"+text.getText());
                Scene miniScene = new Scene(miniGroup, 390, 130);

                miniScene.setOnMouseClicked(event -> {

                    if((ruleCreation[0])&&(arr.size()<2)){
                        Circle startCircle = new Circle((int) (Math.round(event.getX() / 5) * 5), (int) (Math.round(event.getY() / 5) * 5), 2, Color.RED);
                        miniGroup.getChildren().add(startCircle);
                        arr.add(startCircle);
                    }
                    if(arr.size() == 2){
                        line[0]= new Line(arr.get(0).getCenterX(), arr.get(0).getCenterY(), arr.get(1).getCenterX(), arr.get(1).getCenterY());
                        miniGroup.getChildren().add(line[0]);
                    }

                });
                stage.setScene(miniScene);
                stage.show();
            });
            Button applyZ = new Button("ApplyZ");
            Button applyX = new Button("ApplyX");
            Button applyChange = new Button("Apply Change");
            TextField t_Input = new TextField();
            g.getChildren().add(text);g.getChildren().add(applyX);
            g.getChildren().add(applyZ);
            g.getChildren().add(applyChange);
            visualizeBtn.setLayoutX(100);
            visualizeBtn.setLayoutY(10);
            applyX.setLayoutY(10);
            applyZ.setLayoutY(40);
            int finalI = i;
            applyX.setOnAction(actionEvent -> {
                setAllX(Integer.parseInt(t_Input.getText()),arrLOfGroup.get(arrLOfGroup.indexOf(current)));
            });
            applyZ.setOnAction(actionEvent -> {
                setAllZ(Integer.parseInt(t_Input.getText()),arrLOfGroup.get(arrLOfGroup.indexOf(current)));
            });
            t_Input.setLayoutY(70);
            applyChange.setLayoutY(90);
            applyChange.setOnAction(actionEvent -> {
                findAndReplace(theMainExtratorArr,arrLOfGroup.get(arrLOfGroup.indexOf(current)),arrLOfGroup.get(arrLOfGroup.indexOf(current)).get(0).axis);
                mainGroup.getChildren().remove(g);
                Tracker.removeSimilarList(arrLOfGroup,current);
            });
            g.getChildren().add(visualizeBtn);
            g.getChildren().add(t_Input);
            g.setLayoutX(870);
            g.setLayoutY(150*(i+1));
            mainGroup.getChildren().add(g);

        }
        return mainGroup;
    }
    public static void addTrackersAsCubesToGroup(ArrayList<ArrayList<Tracker>> arrLOfGroup, int index, Group group) {
        ArrayList<Tracker> trackers = arrLOfGroup.get(index);
        Pane grid = makeGridForMini(10);
        for (Tracker tracker : trackers) {
            Box cube = createCube(tracker.x-5, tracker.y-5, tracker.z, tracker.col);
            grid.getChildren().add(cube);
        }
        group.getChildren().add(grid);
    }

    private static Box createCube(double x, double y, double z, Color color) {
        // Create a cube
        Box cube = new Box(9, 9, 9);

        // Set position
        cube.setTranslateX(x);
        cube.setTranslateY(y);
        cube.setTranslateZ(z);

        // Set color
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(color);
        cube.setMaterial(material);

        return cube;
    }

    public static void setAllX(int x,ArrayList<Tracker> arr){

        for (int i = 0; i < arr.size(); i++) {
            arr.get(i).z = x;
        }

    }

    public static void setAllZ(int z,ArrayList<Tracker> arr){

        for (int i = 0; i < arr.size(); i++) {
            arr.get(i).z = z;
        }

    }

    public static void findAndReplace(ArrayList <Tracker> arr1,ArrayList<Tracker> arr2,String axis){
        for (int i = 0; i < arr1.size(); i++) {
            for (int j = 0; j < arr2.size(); j++) {
                if((arr1.get(i).x == arr2.get(j).x)&(arr1.get(i).y == arr2.get(j).y)&(arr1.get(i).axis.equals(arr2.get(j).axis))&(arr1.get(i).batch == arr2.get(j).batch)){
                    arr1.set(i,arr2.get(j));
                }
            }
        }
    }
    public static void callOn3dCreation(){
        cubesXZ = new int[][]{};
        for (int i = 0; i < theMainExtratorArr.size(); i++) {
            if(theMainExtratorArr.get(i).axis.equals("X")){
                cubesXZ = AddCubesToArray(cubesXZ, (int) theMainExtratorArr.get(i).x/10,(int) theMainExtratorArr.get(i).z ,(int) theMainExtratorArr.get(i).y/10 );
            }else if(theMainExtratorArr.get(i).axis.equals("Z")){
                cubesXZ = AddCubesToArray(cubesXZ, (int) (theMainExtratorArr.get(i).z),(int) (theMainExtratorArr.get(i).x/10) ,(int) (theMainExtratorArr.get(i).y/10) );
            }else if(theMainExtratorArr.get(i).axis.equals("Y")){
                cubesXZ = AddCubesToArray(cubesXZ, (int) theMainExtratorArr.get(i).x/10 ,(int) theMainExtratorArr.get(i).y/10 ,(int) theMainExtratorArr.get(i).z);
            }
            /*
            else if(theMainExtratorArr.get(i).axis.equals("-X")){
                cubesXZ = AddCubesToArray(cubesXZ, (int) theMainExtratorArr.get(i).x/10,5 ,(int) theMainExtratorArr.get(i).y/10  );
            }else if(theMainExtratorArr.get(i).axis.equals("-Z")){
                cubesXZ = AddCubesToArray(cubesXZ, 5,(int) (theMainExtratorArr.get(i).x/10) ,(int) (theMainExtratorArr.get(i).y/10) );
            }else if(theMainExtratorArr.get(i).axis.equals("-XZ")){
                cubesXZ = AddCubesToArray(cubesXZ, (int) theMainExtratorArr.get(i).x/10 ,(int) theMainExtratorArr.get(i).y/10 ,5);
            }
            System.out.println("X: "+(int) theMainExtratorArr.get(i).x/10+" Y: "+(int) (theMainExtratorArr.get(i).y/10)+" Z: "+(int) theMainExtratorArr.get(i).z);*/

        }
    }

    public static void addSquare(Tracker t1,Tracker t2,Pane pane1,Pane pane2,String axis){
        Stop[] stops = new Stop[] {
                new Stop(0, Color.GOLD),
                new Stop(1, Color.GREEN)
        };
        LinearGradient linearG = new LinearGradient(0, 0, 1, 0, true, null, stops);
        if(t1.axis.equals(axis)){
        Rectangle rect1 = new Rectangle(t1.x+2.5, t1.y+2.5, 5, 5);
        rect1.setFill(linearG);
        pane1.getChildren().add(rect1);
        Rectangle rect2 = new Rectangle(t2.x+2.5, t2.y+2.5, 5, 5);
        rect2.setFill(linearG);
        pane2.getChildren().add(rect2);
        }else {
            Rectangle rect1 = new Rectangle(t1.x+2.5, t1.y+2.5, 5, 5);
            rect1.setFill(linearG);
            pane2.getChildren().add(rect1);
            Rectangle rect2 = new Rectangle(t2.x+2.5, t2.y+2.5, 5, 5);
            rect2.setFill(linearG);
            pane1.getChildren().add(rect2);
        }
    }


    public static void printList(List<Circle> list) {
        for (Circle item : list) {
            System.out.println("\n"+item.getCenterX() +" "+ item.getCenterY());
        }
        System.out.println("<----------->");
    }
    public static void printLineList(List<Line> list) {
        for (Line item : list) {
            System.out.println(item.getStartX() +" "+ item.getEndX()+"\n"+
                    item.getStartY() +" "+ item.getEndY());
        }
        System.out.print("<----------->");
    }
}