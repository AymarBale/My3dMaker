package Selector;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LineSelector {
    private double xOffset;
    private double yOffset;
    public Group myLin = new Group();
    public Rectangle r_start = new Rectangle(5,5);
    public Rectangle r_finish = new Rectangle(5,5);
    public Rectangle lignes = new Rectangle();
    public Button ajouter = new Button("ajouter les cubes");
    public LineSelector(){
        Text t = new Text("Place the cubes at the beginning\n" +
                " and at the end of the line");
        Group h = new Group();
        h.getChildren().add(r_start);
        h.getChildren().add(r_finish);
        r_start.setFill(Color.BLUE);
        r_finish.setFill(Color.RED);
        r_start.setOnMousePressed(event -> {
            xOffset = event.getSceneX() - r_start.getX();
            yOffset = event.getSceneY() - r_start.getY();
        });
        r_start.setOnMouseDragged(event -> {
            double newX = event.getSceneX() - xOffset;
            double newY = event.getSceneY() - yOffset;
            r_start.setX(newX);
            r_start.setY(newY);
        });
        r_finish.setOnMousePressed(event -> {
            xOffset = event.getSceneX() - r_finish.getX();
            yOffset = event.getSceneY() - r_finish.getY();
        });
        r_finish.setOnMouseDragged(event -> {
            double newX = event.getSceneX() - xOffset;
            double newY = event.getSceneY() - yOffset;
            r_finish.setX(newX);
            r_finish.setY(newY);
        });
        r_start.setLayoutX(430);r_finish.setLayoutX(480);
        h.setLayoutY(20);
        h.setLayoutX(-430);
        ajouter.setLayoutY(50);
        myLin.getChildren().add(ajouter);

        myLin.getChildren().add(h);
        myLin.getChildren().add(t);
        myLin.setLayoutY(120);
    }
    public void placeSquarres(int x,int y){
        
    }
}
