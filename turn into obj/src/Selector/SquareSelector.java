package Selector;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class SquareSelector {

    public static double startX;
    public static double startY;
    public static double endX = 0;
    public static double endY = 0;
    private static Rectangle selectionRectangle;
    public static Button done = new Button("Done");


    public static Pane test(Pane root,int x,int y,int l) {
        root.setPrefWidth(x);
        root.setPrefHeight(y);
        root.setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();

            selectionRectangle = new Rectangle(startX, startY, 0, 0);
            selectionRectangle.setFill(Color.GOLD.deriveColor(0, 1, 1, 0.8));
            root.getChildren().add(selectionRectangle);
        });

        root.setOnMouseDragged(e -> {
            selectionRectangle.setWidth(0);
            selectionRectangle.setHeight(0);
            endX = e.getX();
            endY = e.getY();

            double width = endX - startX;
            double height = endY - startY;

            if (width < 0) {
                selectionRectangle.setTranslateX(width);
                width = -width;
            }
            if (height < 0) {
                selectionRectangle.setTranslateY(height);
                height = -height;
            }
            selectionRectangle.setWidth(width);
            selectionRectangle.setHeight(height);
        });
        /**/
        root.setOnMouseReleased(e -> {
            root.getChildren().remove(selectionRectangle);
            if (l==0){
                startX -= 10;
                startY -= 10;
            }else {
                startX = startX+l;
                endX = endX+l;
            }
        });
        return root;
    }
    public Group intiateLine(){
        Group g = new Group();
        Text t = new Text("Drag your mouse to create a square \n " +
                "that will represent your group");
        done.setLayoutY(30);
        g.setLayoutY(125);
        g.getChildren().add(done);
        g.getChildren().add(t);
        return g;
    }

}



