package testingFunction;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class TestingFunc extends Application {
    private double anchrX;
    private double anchrY;
    private double anchr_angX = 0;
    private double anchr_angY = 0;
    private final DoubleProperty anglX = new SimpleDoubleProperty();
    private final DoubleProperty anglY = new SimpleDoubleProperty();
    @Override
    public void start(Stage stage) throws Exception{
        Box box = new Box(20,50,20);
        Box box0 = new Box( 50,20,20);
        box0.setTranslateX(50);
        Group root = new Group();
        root.getChildren().addAll(box,box0);
        root.translateXProperty().set(250);
        root.translateYProperty().set(250);

        Scene scene = new Scene(root,500,500,true);
        scene.setFill(Color.AQUA);
        initmouse(root,scene);
        stage.setScene(scene);
        stage.show();
    }

    private void initmouse(Group root,Scene scene){
        Rotate xrotate;
        Rotate yrotate;
        root.getTransforms().addAll(xrotate=new Rotate(0,Rotate.X_AXIS),yrotate=new Rotate(0,Rotate.Y_AXIS));
        xrotate.angleProperty().bind(anglX);
        yrotate.angleProperty().bind(anglY);
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
    }
    public static void main(String[] args) {
        launch(args);
    }
}
