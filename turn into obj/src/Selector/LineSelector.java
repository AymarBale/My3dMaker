package Selector;

import ColorsPaletteExtraction.Tracker;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Grid.GridPage.*;
import static Grid.PositionUtility.printList;

public class LineSelector {
    public Group myLin = new Group();
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
    public Button ajouter = new Button("ajouter les cubes");
    public LineSelector(){
        Text t = new Text("Click left to place the point\n" +
                " and click right to delete them");
        t.setLayoutY(215);
        t.setLayoutX(860);
        Button checkPos = new Button("checkMyPos");
        checkPos.setLayoutY(250);
        checkPos.setLayoutX(860);
        root = new Pane();
        pointsLinePane = new Pane();

        root.getChildren().addAll(pointsLinePane, checkPos);
        root.setStyle("-fx-background-color: rgba(0, 255, 0, 0.2);");
        root.setPrefSize(850, 400);

        checkPos.setOnAction(e -> {
            checkPosition = !checkPosition;
            CreatePolygon();
        });
        ajouter.setLayoutY(280);
        ajouter.setLayoutX(860);
        myLin.setOnMousePressed(this::handleMousePressed);
        myLin.setOnMouseDragged(this::handleMouseDragged);
        myLin.setOnMouseReleased(this::handleMouseReleased);
        myLin.setOnMouseClicked(this::handleMouseClick);
        myLin.setLayoutX(-870);
        myLin.setLayoutY(-100);
        myLin.getChildren().add(root);
        myLin.getChildren().add(ajouter);

        myLin.getChildren().add(t);
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
        lines.removeAll(linesToRemove);
        root.getChildren().removeAll(linesToRemove);
    }

    private boolean approxEqual(double a, double b) {
        return Math.abs(a - b) < 0.001;
    }

    private void handleMouseClick(MouseEvent event) {
        lastMouseEvent = event;
        double x = event.getX();
        double y = event.getY();

        if (event.getButton() == MouseButton.PRIMARY) {
            if (checkPosition) {
                System.out.println(" if is inside :"+isPointInsidePolygon(x,y));
            } else {
                handleLeftMouseClick(x, y);
            }
        } else if (event.getButton() == MouseButton.SECONDARY) {
            handleRightMouseClick(x, y);
        }
    }

    private void handleLeftMouseClick(double x, double y) {
        if (!checkPosition) {
            Circle clickedCircle = getClickedCircle(x, y);
            if (clickedCircle == null) {
                // Adjust x value based on the desired conditions
                x = Math.round(x / 5) * 5; // Round to the nearest multiple of 5
                y = Math.round(y / 5) * 5;

                // Check if a circle already exists at the adjusted position
                if (!circleExistsAtPosition(x, y)) {
                    Circle circle = new Circle(x, y, 2, Color.BLUE);
                    root.getChildren().add(circle);
                    points.add(circle);
                    connectPointsWithLines();
                }
            } else {
                selectedCircle = clickedCircle;
            }
        }

    }

    private boolean circleExistsAtPosition(double x, double y) {
        for (Circle circle : points) {
            if (approxEqual(circle.getCenterX(), x) && approxEqual(circle.getCenterY(), y)) {
                return true;
            }
        }
        return false;
    }


    private void handleRightMouseClick(double x, double y) {
        if (!checkPosition) {
            Circle clickedCircle = getClickedCircle(x, y);
            if (clickedCircle != null) {
                root.getChildren().remove(clickedCircle);
                points.remove(clickedCircle);
                removeLinesConnectedToCircle(clickedCircle);
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
        List<Circle> oriPoints = new ArrayList<>(points);
        points = orderPointsByDistance(points);
        for (Circle circle : points) {
            List<Line> connectedLines = getConnectedLines(circle);
            if (connectedLines.size() > 1) {
                polygon.getPoints().addAll(circle.getCenterX(), circle.getCenterY());
            }
        }
        if (!isValidPolygon()) {
            points = new ArrayList<>(oriPoints);
            System.out.println("The points are not connected in a way that allows a polygon to be made.");
            return;
        }
        polygon.setFill(Color.BLUE);
        polygon.setStroke(Color.GOLD);
        root.getChildren().add(polygon);
    }

    private boolean isValidPolygon() {
        if (points.size() != polygon.getPoints().size() / 2) {
            return false; // Not enough points to form a polygon
        }

        // Check if the last point is connected to the first point
        Circle firstPoint = points.get(0);
        Circle lastPoint = points.get(points.size() - 1);

        List<Line> connectedLines = getConnectedLines(lastPoint);

        for (Line line : connectedLines) {
            double startX = line.getStartX();
            double startY = line.getStartY();
            double endX = line.getEndX();
            double endY = line.getEndY();

            if ((approxEqual(startX, lastPoint.getCenterX()) && approxEqual(startY, lastPoint.getCenterY())) ||
                    (approxEqual(endX, lastPoint.getCenterX()) && approxEqual(endY, lastPoint.getCenterY())) ||
                    (approxEqual(startX, firstPoint.getCenterX()) && approxEqual(startY, firstPoint.getCenterY())) ||
                    (approxEqual(endX, firstPoint.getCenterX()) && approxEqual(endY, firstPoint.getCenterY()))) {
                return true; // The last point is connected to the first point
            }
        }

        return false;
    }

    public void addGroup(Polygon polygon, Pane rectanglePane){
        if (!isValidPolygon()) {
            checkOverLap();
        }else {
            checkOverLapPolygon(polygon,rectanglePane);
        }
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

    public void checkOverLapPolygon(Polygon polygon, Pane rectanglePane) {
        ArrayList <Tracker> t = new ArrayList<>();
        for (int i = 0; i < theMainExtratorArr.size(); i++) {
            if(theMainExtratorArr.get(i).axis.equals(axis1)){
                if(isRectangleInsidePolygon(theMainExtratorArr.get(i).x,
                        theMainExtratorArr.get(i).y,10,polygon)){
                    t.add(theMainExtratorArr.get(i));
                }
            }
            if(theMainExtratorArr.get(i).axis.equals(axis2)){
                if(isRectangleInsidePolygon(theMainExtratorArr.get(i).x+450,
                        theMainExtratorArr.get(i).y,10,polygon)){
                    t.add(theMainExtratorArr.get(i));
                }
            }
        }
        if (!t.isEmpty()) {
            arrLOfGroup.add(t);
        }
    }

    public void checkOverLap(){
        ArrayList <Tracker> t = new ArrayList<>();
        String axe = "";
        int l = 0;
        if(points.get(0).getCenterX() < 450){
            axe = axis1;
        }else {
            l = 450;
            axe = axis2;
        }
        for (int i = 0; i < theMainExtratorArr.size(); i++) {
            if(theMainExtratorArr.get(i).axis.equals(axis1)){
                if (isTrackerBetweenPoints(theMainExtratorArr.get(i),axe,l)) {
                    t.add(theMainExtratorArr.get(i));
                }
            }
            if(theMainExtratorArr.get(i).axis.equals(axis2)){
                if (isTrackerBetweenPoints(theMainExtratorArr.get(i),axe,l)) {
                    t.add(theMainExtratorArr.get(i));
                }
            }
        }
        if (!t.isEmpty()) {
            arrLOfGroup.add(t);
        }
    }
    private boolean isTrackerBetweenPoints(Tracker tracker,String axe,int layout) {
        double trackerX = tracker.x;
        double trackerY = tracker.y;
        double trackerWidth = 10;

        for (int i = 0; i < lines.size(); i++) {
            if(lines.get(i).getEndX()-lines.get(i).getStartX() > lines.get(i).getStartY()-lines.get(i).getEndY()){
                double minX = Math.min(lines.get(i).getStartX()-layout, lines.get(i).getEndX()-layout)-5;
                double maxX = Math.max(lines.get(i).getStartX()-layout, lines.get(i).getEndX()-layout)+5;
                double minY = Math.min(lines.get(i).getStartY(), lines.get(i).getEndY())-10;
                double maxY = Math.max(lines.get(i).getStartY(), lines.get(i).getEndY());

                if (trackerX >= minX && trackerX + trackerWidth <= maxX && trackerY >= minY && trackerY <= maxY && tracker.axis.equals(axe)) {
                    return true;
                }
            }else {
                double minX = Math.min(lines.get(i).getStartX()-layout, lines.get(i).getEndX()-layout)-5;
                double maxX = Math.max(lines.get(i).getStartX()-layout, lines.get(i).getEndX()-layout)+5;
                double minY = Math.min(lines.get(i).getStartY(), lines.get(i).getEndY())-10;
                double maxY = Math.max(lines.get(i).getStartY(), lines.get(i).getEndY());

                if (trackerX >= minX && trackerX + trackerWidth <= maxX && trackerY >= minY && trackerY <= maxY && tracker.axis.equals(axe)) {
                    return true;
                }

            }
        }

        return false;
    }

    private boolean isRectangleInsidePolygon(double x, double y, double width, Polygon polygon) {
        // Check each corner of the rectangle
        double[] corners = {
                x, y,
                x + width, y,
                x, y + width,
                x + width, y + width
        };
        // Check if at least one corner is inside the polygon
        for (int i = 0; i < corners.length; i += 2) {
            double cornerX = corners[i];
            double cornerY = corners[i + 1];

            if (polygon.contains(cornerX, cornerY)) {

                return true;
            }
        }

        return false;
    }
}
