package Grid;

import ColorsPaletteExtraction.Extractor;
import ColorsPaletteExtraction.Tracker;
import ImageTaker.GetMyImageAlongAxe;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

import static Grid.PositionUtility.*;
import static Utils3DCreation.com.Utils.Create3DObject;
import static javafx.application.Application.launch;

public class GridPage extends Application{


    public static Color c = Color.BLACK;
    public static ArrayList<myImagePosition> savedArray = new ArrayList<myImagePosition>();
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
        root.getChildren().add(p1);
        root.getChildren().add(p2);
        TextField text = new TextField();
        p2.setLayoutX(650);

        Button sub = new Button("SUBMIT");
        ColorPicker cp = new ColorPicker(Color.BLUE);
        Button sameZ = new Button("Apply same z");
        Button loadAnother = new Button("Load another scene");

        sameZ.setLayoutY(500);
        sameZ.setLayoutX(440);
        loadAnother.setLayoutY(500);
        loadAnother.setLayoutX(740);
        text.setLayoutY(500);
        text.setLayoutX(540);
        loadAnother.setOnAction(event -> {
            try {
                Extractor.ResetVariables();
                Stage.close();
                Stage secondWindow = new Stage();
                GetMyImageAlongAxe e = new GetMyImageAlongAxe();
                e.start(secondWindow);
                secondWindow.setTitle("extractor page");
                secondWindow.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sameZ.setOnAction(value ->  {
            putZForSelect(selectedCubes, Integer.parseInt(text.getText()));
        });
        root.getChildren().add(loadAnother);
        root.getChildren().add(sameZ);
        root.getChildren().add(text);
        root.getChildren().add(con);
        root.getChildren().add(select);
        root.getChildren().add(cp);
        root.getChildren().add(sub);
        sub.setLayoutX(500);
        sub.setLayoutY(200);
        cp.setLayoutX(500);
        con.setLayoutX(500);
        con.setLayoutY(300);
        select.setLayoutX(500);
        select.setLayoutY(350);
        sub.setOnAction(value ->  {
            Create3DObject();
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
        Stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

class myImagePosition{
    public ArrayList<Tracker> myCubes = new ArrayList<>();
    String axis = "";
}
