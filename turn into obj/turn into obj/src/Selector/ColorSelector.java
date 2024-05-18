package Selector;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ColorSelector {
    public Group myCol = new Group();
    public static Rectangle r = new Rectangle(20,20);
    public Button addAllCubes = new Button("Add all cubes");
    public ColorSelector(){
        Text t = new Text("Click on the color of \n your choice inside the grid");
        r.setLayoutY(30);r.setLayoutX(10);
        addAllCubes.setLayoutY(55);
        myCol.getChildren().add(addAllCubes);
        myCol.getChildren().add(t);
        myCol.getChildren().add(r);
        myCol.setLayoutY(120);

    }
    public Paint addCubes(){
        return r.getFill();
    };
    public static void setColor(Color c){
        r.setFill(c);
    }

}
