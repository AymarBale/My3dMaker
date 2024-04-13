package Selector;

import ColorsPaletteExtraction.Tracker;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
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

import java.util.*;

import static Grid.GridPage.*;
import static Grid.PositionUtility.printLineList;
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
    ArrayList<Circle> additional = new ArrayList<>();
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
        if(isValidPolygon() != null){
            points = orderPointsByDistance(points);
            ArrayList<Integer> polyPoints = isValidPolygon();
            for (int i = 0; i < points.size(); i++) {
                for (int j = 0; j < polyPoints.size(); j++) {
                    if(polyPoints.get(j) == i){
                        polygon.getPoints().addAll(points.get(i).getCenterX(), points.get(i).getCenterY());
                    }
                }
            }
        }else if (isValidPolygon() == null) {
            points = orderPointsByDistance(points);
            points = new ArrayList<>(oriPoints);
            System.out.println("The points are not connected in a way that allows a polygon to be made.");
            return;
        }
        polygon.setFill(Color.BLUE);
        polygon.setStroke(Color.GOLD);
        root.getChildren().add(polygon);
    }
    //The points are not connected in a way that allows a polygon to be made.
    private ArrayList<Integer> isValidPolygon() {
        ArrayList<Points> arr = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Points myPoint = new Points(i+"");
            Circle currCircle = points.get(i);
            for (int j = 0; j < points.size(); j++) {
                if(points.get(j)!= currCircle){
                    if(areConnected(currCircle,points.get(j),lines)){
                        if(myPoint.connectedTo.equals("")){
                            myPoint.connectedTo += j+"";
                        }else{
                            myPoint.connectedTo += ","+j;
                        }

                    };
                }
            }
            arr.add(myPoint);
        }
        ArrayList<Integer> polygon = findPolygon(arr);
        if (polygon != null) {
            return polygon;
        } else {
            return null;
        }

    }

    public void addGroup(Polygon polygon, Pane rectanglePane,int [] batchs){
        if (isValidPolygon() == null) {
            checkOverLap(batchs);
        }else {
            checkOverLapPolygon(polygon,rectanglePane,batchs);
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
        Circle finalCircle = points.get(points.size()-1);
        for (int i = 1; i < Psize; i++) {
            Circle currentPoint = orderedPoints.get(i - 1);
            Circle closestPoint = findClosestCircle(currentPoint, points,lines);

            if ((closestPoint != null)) {
                orderedPoints.add(closestPoint);
                points.remove(closestPoint);
            }else {
                orderedPoints.add(points.get(0));
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

    public void checkOverLapPolygon(Polygon polygon, Pane rectanglePane,int [] batchs) {
        ArrayList <Tracker> t = new ArrayList<>();
        int numberOfPoints = polygon.getPoints().size() / 2;

        List<Line> additionaLines = new ArrayList<>();
        if(points.size() > numberOfPoints){
            additional = (ArrayList<Circle>) getAdditionalCircle();
            for (int i = 0; i < additional.size(); i++) {
                for (int j = 0; j < getConnectedLines(additional.get(i)).size(); j++) {
                    additionaLines.add(getConnectedLines(additional.get(i)).get(j));
                }
            }
        }
        for (int i = 0; i < theMainExtratorArr.size(); i++) {
            if(theMainExtratorArr.get(i).batch == batchs[0]){
                if(isRectangleInsidePolygon(theMainExtratorArr.get(i).x,
                        theMainExtratorArr.get(i).y,10,polygon)){
                    t.add(theMainExtratorArr.get(i));
                }
                if(additional.size()>0){
                    for (int j = 0; j < additional.size(); j++) {
                        if(isTrackerBetween2Point(theMainExtratorArr.get(i),theMainExtratorArr.get(i).axis,
                        0,additionaLines)){
                            t.add(theMainExtratorArr.get(i));
                        };
                    }
                }
            }
            if(theMainExtratorArr.get(i).batch == batchs[1]){
                if(isRectangleInsidePolygon(theMainExtratorArr.get(i).x+450,
                        theMainExtratorArr.get(i).y,10,polygon)){
                    t.add(theMainExtratorArr.get(i));
                }
                if(additional.size()>0){
                    for (int j = 0; j < additional.size(); j++) {
                        if(isTrackerBetween2Point(theMainExtratorArr.get(i),theMainExtratorArr.get(i).axis,
                                450,additionaLines)){
                            t.add(theMainExtratorArr.get(i));
                        };
                    }
                }
            }
        }
        if (!t.isEmpty()) {
            arrLOfGroup.add(t);
        }
    }

    public List<Circle> getAdditionalCircle( ) {
        List<Circle> additionalCircles = new ArrayList<>();
        List<Point2D> polygonPoints = new ArrayList<>();
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            double x = polygon.getPoints().get(i);
            double y = polygon.getPoints().get(i + 1);
            polygonPoints.add(new Point2D(x, y));
        }
        // Iterate over each circle in the points list
        for (int i = 0; i < points.size(); i++) {

            for (int j = 0; j < polygonPoints.size();j++) {

                if ((points.get(i).getCenterX() != polygonPoints.get(j).getX())&&
                        (points.get(i).getCenterY() != polygonPoints.get(j).getY())) {
                    additionalCircles.add(points.get(i));
                    //break;
                }
            }
        }
        System.out.println(additionalCircles.get(0).getCenterX()+" "+additionalCircles.get(0).getCenterY()+"\n"+additionalCircles.get(1).getCenterX()+" "+additionalCircles.get(1).getCenterY());
        return additionalCircles;
    }



    public void checkOverLap(int [] batchs){
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
            if(theMainExtratorArr.get(i).batch == batchs[0]){
                if (isTrackerBetweenPoints(theMainExtratorArr.get(i),axe,l)) {
                    t.add(theMainExtratorArr.get(i));
                }
            }
            if(theMainExtratorArr.get(i).batch == batchs[1]){
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
    private boolean isTrackerBetween2Point(Tracker tracker,String axe,int layout,List<Line> lines) {
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

    static ArrayList<Integer> findPolygon(ArrayList<Points> pointsList) {
        HashMap<String, HashSet<String>> graph = new HashMap<>();

        // Build the graph from the given points list
        for (Points point : pointsList) {
            String[] connectedPoints = point.connectedTo.split(",");
            HashSet<String> connections = new HashSet<>();
            for (String connectedPoint : connectedPoints) {
                connections.add(connectedPoint);
            }
            graph.put(point.nom, connections);
        }

        // Perform depth-first search (DFS) to find a cycle
        HashSet<String> visited = new HashSet<>();
        for (Points point : pointsList) {
            if (!visited.contains(point.nom)) {
                ArrayList<String> cycle = new ArrayList<>();
                if (hasCycle(graph, point.nom, null, visited, cycle)) {
                    ArrayList<Integer> polyVal = new ArrayList<>();
                    for (int i = 0; i < cycle.size(); i++) {
                        polyVal.add(Integer.parseInt(cycle.get(i)));
                    }
                    return polyVal;
                }
            }
        }

        return null;
    }

    static boolean hasCycle(HashMap<String, HashSet<String>> graph, String current, String parent, HashSet<String> visited, ArrayList<String> cycle) {
        visited.add(current);
        cycle.add(current);
        HashSet<String> neighbors = graph.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!neighbor.equals(parent)) {
                    if (visited.contains(neighbor)) {
                        // Found a cycle
                        cycle.add(neighbor);
                        return true;
                    } else {
                        if (hasCycle(graph, neighbor, current, visited, cycle)) {
                            return true;
                        }
                    }
                }
            }
        }
        cycle.remove(current);
        return false;
    }
}

class Points {
    String nom;
    String connectedTo = "";

    public Points() {

    }
    public Points(String nom) {
        this.nom = nom;
    }
    public Points(String nom, String connect) {
        this.nom = nom;
        this.connectedTo = connect;
    }
    public void print(){
        System.out.println(nom +" "+ connectedTo);
    }
}