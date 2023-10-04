package Grid;

import ColorsPaletteExtraction.Extractor;
import ColorsPaletteExtraction.Tracker;
import ImageTaker.GetMyImageAlongAxe;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

import static ColorsPaletteExtraction.Extractor.myCubes;
import static Grid.GridPage.c;
import static Utils3DCreation.com.Utils.*;


public class PositionUtility {
    public static int [][] connect = {{},{}};
    public static int [][] selectedCubes = {};
    public static int xorz = 0;
    public static Pane makeGrid(int n, int x_z, CheckBox c,CheckBox s){

        double width = 10;
        Pane pane = new Pane();

        pane.setOnMouseClicked(e -> {
            int x = (int)e.getX()/10;
            int y = (int)e.getY()/10;
            xorz = x_z;
            DrawSquare(pane,(int)e.getX()/10,(int)e.getY()/10,c,s);

        });
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
        Rectangle rect = new Rectangle(10,10);
        rect.setFill(c);
        rect.setX(x*10);
        rect.setY(y*10);
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

        if(xorz == 1){
            cubesX = AddCubesToArray(cubesX,x,y,0);
        }
        if(xorz == 2){
            cubesZ = AddCubesToArray(cubesZ,x,y,0);
        }
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

    public static void addCubeToGrid(Pane p, ArrayList<Tracker> receivedCubes){
        System.out.println(receivedCubes.size());
        int selectedAxis = 0;
        if(GetMyImageAlongAxe.chosenAxe.equals("Z")){
            selectedAxis += 650;
        }
        for (int i = 0; i < receivedCubes.size(); i++){
            c = receivedCubes.get(i).col;

            p.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, transformPosition(myCubes).get(i).x+selectedAxis, transformPosition(myCubes).get(i).y, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
            arrCol.add(c);
            allColArr.add(receivedCubes.get(i).col);

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
            System.out.println(arr.get(i) + " ----------------->"+i);
        }
    }
}