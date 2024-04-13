package testingFunction;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static Grid.PositionUtility.printList;

public class TestingFunc extends Application{

    public static List<Circle> points = new ArrayList<>();
    public static List<Line> lines = new ArrayList<>();
    public static ArrayList<Text> textArray = new ArrayList<>();
    public static int circleCount = 0;

    @Override
    public void start(Stage primaryStage) {
        // Create a Pane to hold the shapes
        Pane pane = new Pane();
        //TestingFunc a = new TestingFunc();
        // Add circles with specified positions to the list
        points.add(createCircle(25.0, 25.0));
        points.add(createCircle(55.0, 25.0));
        points.add(createCircle(55.0, 55.0));
        points.add(createCircle(85.0, 55.0));
        points.add(createCircle(85.0, 85.0));
        points.add(createCircle(25.0, 85.0));
        points.add(createCircle(95.0, 25.0));
        points.add(createCircle(95.0, 35.0));

        lines.add(createLine(25.0, 25.0, 25.0, 85.0));
        lines.add(createLine(25.0, 85.0, 85.0, 85.0));
        lines.add(createLine(85.0, 85.0, 85.0, 55.0));
        lines.add(createLine(85.0, 55.0, 55.0, 55.0));

        lines.add(createLine(55.0, 55.0, 55.0, 25.0));
        lines.add(createLine(55.0, 25.0, 25.0, 25.0));
        lines.add(createLine(55.0, 25.0, 95.0, 25.0));
        lines.add(createLine(95.0, 25.0, 95.0, 35.0));
        /*addCircleAndLines();
        circles = (ArrayList<Circle>) orderPointsByDistance(circles);
        addCircleAndLines();*/
        // Add circles and lines to the pane

        pane.getChildren().addAll(points);
        pane.getChildren().addAll(lines);
        pane.getChildren().addAll(textArray);

        // Create a scene and add the pane to it
        Scene scene = new Scene(pane, 200, 200);
        System.out.println(isValidPolygon());
        // Set the stage title and scene, then show the stage
        primaryStage.setTitle("JavaFX Shapes");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
    public static String isValidPolygon() {
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
        ArrayList<String> polygon = findPolygon(arr);
        if (polygon != null) {
            return polygon.toString();
        } else {
            return "";
        }

    }
    private static Circle createCircle(double centerX, double centerY) {
        Circle circle = new Circle(centerX, centerY, 5, Color.BLACK);
        Text text = new Text(""+circleCount);
        text.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        text.setLayoutX(centerX);
        text.setLayoutY(centerY);
        textArray.add(text);
        circleCount++;
        return circle;
    }
    private static Line createLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.BLACK);
        return line;
    }
    static ArrayList<String> findPolygon(ArrayList<Points> pointsList) {
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
                    return cycle;
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

    private static boolean areConnected(Circle circle1, Circle circle2, List<Line> lines) {
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