package Selector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SquareSelector extends Application {
    private double startX, startY;
    private Rectangle selectionRectangle;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 400, 400);

        selectionRectangle = new Rectangle(0, 0, 0, 0);
        selectionRectangle.setFill(Color.TRANSPARENT);
        selectionRectangle.setStroke(Color.GOLD);
        selectionRectangle.setStrokeWidth(1);

        root.getChildren().add(selectionRectangle);

        scene.setOnMousePressed(event -> {
            startX = event.getX();
            startY = event.getY();
            selectionRectangle.setX(startX);
            selectionRectangle.setY(startY);
            selectionRectangle.setWidth(0);
            selectionRectangle.setHeight(0);
        });

        scene.setOnMouseDragged(event -> {
            selectionRectangle.setStroke(Color.GOLD);
            double currentX = event.getX();
            double currentY = event.getY();
            double width = currentX - startX;
            double height = currentY - startY;

            selectionRectangle.setX(width >= 0 ? startX : currentX);
            selectionRectangle.setY(height >= 0 ? startY : currentY);
            selectionRectangle.setWidth(Math.abs(width));
            selectionRectangle.setHeight(Math.abs(height));
        });

        scene.setOnMouseReleased(event -> {
            selectionRectangle.setStroke(Color.WHITE);
            selectionRectangle.setWidth(0);
            selectionRectangle.setHeight(0);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
