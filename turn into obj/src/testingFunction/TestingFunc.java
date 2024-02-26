package testingFunction;
import ImageTaker.AdvanceTab;
import ImageTaker.GetMyImageAlongAxe;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static Editor.EditorSettings.extractAxis;

public class TestingFunc extends Application {

    private List<Circle> points = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private Pane pointsLinePane;
    private Pane root;
    private Circle selectedCircle;
    private Line tempLine;
    private boolean isDragging = false;
    private boolean checkPosition = false;
    public Polygon polygon ;

    private MouseEvent lastMouseEvent;
    public static void main(String[] args) {
        launch(args);
    }

    private void removeLinesConnectedToCircle(Circle circle) {
        List<Line> linesToRemove = new ArrayList<>();

        for (Line line : lines) {
            double startX = line.getStartX();
            double startY = line.getStartY();
            double endX = line.getEndX();
            double endY = line.getEndY();

            if (approxEqual(startX, circle.getCenterX()) && approxEqual(startY, circle.getCenterY()) ||
                    approxEqual(endX, circle.getCenterX()) && approxEqual(endY, circle.getCenterY())) {
                linesToRemove.add(line);
            }
        }

        // Remove the lines connected to the deleted circle
        lines.removeAll(linesToRemove);
        root.getChildren().removeAll(linesToRemove);
    }

    private boolean approxEqual(double a, double b) {
        return Math.abs(a - b) < 0.001; // Adjust the threshold as needed
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Click Points with Lines");
        Button checkPos = new Button("checkMyPos");
        Button removePoly = new Button("Remove polygon");
        removePoly.setLayoutX(490);
        root = new Pane();
        Circle c = new Circle(10);
        c.setFill(Color.GOLD);
        c.setCenterX(400);
        c.setCenterY(300);
        pointsLinePane = new Pane();
        root.getChildren().addAll(pointsLinePane, checkPos,removePoly,c);
        Scene scene = new Scene(root, 600, 400);

        checkPos.setOnAction(e -> {
            checkPosition = true;
            CreatePolygon();
            System.out.println(checkOverlap(polygon,c));
        });

        removePoly.setOnAction(e ->{
            checkPosition = false;
            polygon = null;
        });

        scene.setOnMousePressed(this::handleMousePressed);
        scene.setOnMouseDragged(this::handleMouseDragged);
        scene.setOnMouseReleased(this::handleMouseReleased);
        scene.setOnMouseClicked(this::handleMouseClick);

        primaryStage.setScene(scene);
        primaryStage.show();
    }





    private void handleMouseClick(MouseEvent event) {
        lastMouseEvent = event; // Update the last MouseEvent
        double x = event.getX();
        double y = event.getY();

        if (event.getButton() == MouseButton.PRIMARY) {
            // Left mouse button clicked
            if (checkPosition) {
                System.out.println(" if is inside :"+isPointInsidePolygon(x,y));
            } else {
                handleLeftMouseClick(x, y);
            }
        } else if (event.getButton() == MouseButton.SECONDARY) {
            // Right mouse button clicked
            handleRightMouseClick(x, y);
        }
    }

    private void handleLeftMouseClick(double x, double y) {
        if (!checkPosition) {
            // Check if the click is on an existing circle
            Circle clickedCircle = getClickedCircle(x, y);
            if (clickedCircle == null) {
                // Create a new circle at the clicked position
                Circle circle = new Circle(x, y, 5, Color.BLUE);
                root.getChildren().add(circle);
                points.add(circle);
                connectPointsWithLines();
            } else {
                selectedCircle = clickedCircle;
            }
        }
    }

    private void handleRightMouseClick(double x, double y) {
        if (!checkPosition) {
            // Handle right mouse click logic here
            // For example, remove a circle or perform a specific action
            Circle clickedCircle = getClickedCircle(x, y);
            if (clickedCircle != null) {
                // Remove the clicked circle
                root.getChildren().remove(clickedCircle);
                points.remove(clickedCircle);

                // Remove lines connected to the deleted circle
                removeLinesConnectedToCircle(clickedCircle);

                // Redraw remaining points and lines
                connectPointsWithLines();
            }
        }
    }

    private void handleMousePressed(MouseEvent event) {
        lastMouseEvent = event; // Update the last MouseEvent
        double x = event.getX();
        double y = event.getY();

        if (event.getButton() == MouseButton.PRIMARY) {
            // Check if the left mouse button is pressed
            selectedCircle = getClickedCircle(x, y);
            if (selectedCircle != null) {
                tempLine = new Line(selectedCircle.getCenterX(), selectedCircle.getCenterY(), x, y);
                root.getChildren().add(tempLine);
            }
            isDragging = false;
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (isLeftMouseButtonPressed()) { // Check if the left mouse button is pressed
            if (selectedCircle != null && tempLine != null) {
                double x = event.getX();
                double y = event.getY();

                tempLine.setEndX(x);
                tempLine.setEndY(y);
                isDragging = true; // Set the drag flag
            }
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (isLeftMouseButtonPressed()) { // Check if the left mouse button is pressed
            double x = event.getX();
            double y = event.getY();

            Circle targetCircle = getClickedCircle(x, y);
            if (selectedCircle != null && tempLine != null) {
                root.getChildren().remove(tempLine);
                tempLine = null;

                if (targetCircle != null && targetCircle != selectedCircle) {
                    drawLineBetweenCircles(selectedCircle, targetCircle);
                } else {
                    // Create a new circle at the release position
                    Circle newCircle = new Circle(x, y, 5, Color.BLUE);
                    root.getChildren().add(newCircle);
                    points.add(newCircle);
                    connectPointsWithLines();
                }
            }
        }
    }

    private boolean isLeftMouseButtonPressed() {
        return lastMouseEvent != null && lastMouseEvent.getButton() == MouseButton.PRIMARY;
    }

    private Circle getClickedCircle(double x, double y) {
        for (Circle circle : points) {
            if (circle.contains(x, y)) {
                return circle;
            }
        }
        return null;
    }

    private void drawLineBetweenCircles(Circle start, Circle end) {
        Line line = new Line(start.getCenterX(), start.getCenterY(), end.getCenterX(), end.getCenterY());
        lines.add(line);
        root.getChildren().add(line);
    }

    private void connectPointsWithLines() {
        root.getChildren().remove(pointsLinePane);
        pointsLinePane.getChildren().clear(); // Clear previous points and lines

        // Add circles for each point
        pointsLinePane.getChildren().addAll(points);

        // Draw lines between points
        for (Line line : lines) {
            pointsLinePane.getChildren().add(line);
        }

        // Add pointsLinePane to root
        root.getChildren().add(0, pointsLinePane);
    }

    private void CreatePolygon() {
        polygon = new Polygon();
        if (points.size() < 3) {
            System.out.println("not enough points");
            return;
        }

        points = orderPointsByDistance(points);

        for (Circle circle : points) {
            List<Line> connectedLines = getConnectedLines(circle);
            if (connectedLines.size() > 1) {
                polygon.getPoints().addAll(circle.getCenterX(), circle.getCenterY());
            }
        }
        polygon.setFill(Color.BLUE);
        polygon.setStroke(Color.GOLD);
        root.getChildren().add(polygon);
    }

    private List<Circle> orderPointsByDistance(List<Circle> points) {
        List<Circle> orderedPoints = new ArrayList<>();

        if (points.isEmpty()) {
            return orderedPoints; // Return an empty list if there are no points
        }
        int Psize = points.size();

        orderedPoints.add(points.get(0)); // Add the first point to the ordered list
        points.remove(points.get(0));
        for (int i = 1; i < Psize; i++) {
            Circle currentPoint = orderedPoints.get(i - 1);
            Circle closestPoint = findClosestCircle(currentPoint, points,lines);

            if (closestPoint != null) {
                orderedPoints.add(closestPoint);

                points.remove(closestPoint);
            }else {
                orderedPoints.add(currentPoint);
            }
        }

        return orderedPoints;
    }

    private Circle findClosestCircle(Circle targetCircle, List<Circle> circles, List<Line> lines) {
        if (circles.isEmpty() || lines.isEmpty()) {
            return null; // Return null if the list of circles or lines is empty
        }

        Circle closestCircle = null;
        double minDistance = Double.MAX_VALUE;

        for (Circle circle : circles) {
            if (areConnected(targetCircle, circle, lines)) {
                double distance = distanceBetweenCircles(targetCircle, circle);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCircle = circle;
                }
            }
        }

        return closestCircle;
    }

    private boolean areConnected(Circle circle1, Circle circle2, List<Line> lines) {
        for (Line line : lines) {
            if ((line.getStartX() == circle1.getCenterX() && line.getStartY() == circle1.getCenterY() &&
                    line.getEndX() == circle2.getCenterX() && line.getEndY() == circle2.getCenterY()) ||
                    (line.getStartX() == circle2.getCenterX() && line.getStartY() == circle2.getCenterY() &&
                            line.getEndX() == circle1.getCenterX() && line.getEndY() == circle1.getCenterY())) {
                return true;
            }
        }
        return false;
    }

    private double distanceBetweenCircles(Circle circle1, Circle circle2) {
        double deltaX = circle2.getCenterX() - circle1.getCenterX();
        double deltaY = circle2.getCenterY() - circle1.getCenterY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    private boolean isPointInsidePolygon(double px, double py) {
        return polygon.contains(px, py);
    }

    private List<Line> getConnectedLines(Circle circle) {
        List<Line> connectedLines = new ArrayList<>();
        for (Line line : lines) {
            if (line.getStartX() == circle.getCenterX() && line.getStartY() == circle.getCenterY() ||
                    line.getEndX() == circle.getCenterX() && line.getEndY() == circle.getCenterY()) {
                connectedLines.add(line);
            }
        }
        return connectedLines;
    }

    private boolean checkOverlap(Polygon polygon, Circle circle) {
        // Check if any part of the polygon intersects with the circle
        if (polygon.getBoundsInParent().intersects(circle.getBoundsInParent())) {
            return true;
        }

        // Check if any vertex of the polygon is inside the circle
        List<Double> points = polygon.getPoints();
        int size = points.size();

        for (int i = 0; i < size; i += 2) {
            double x = points.get(i);
            double y = points.get(i + 1);

            if (circle.contains(x, y)) {
                return true;
            }
        }

        return false;
    }

}
