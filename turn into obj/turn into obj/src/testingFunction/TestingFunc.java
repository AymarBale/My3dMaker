package testingFunction;

import ImageTaker.QuickImport;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;

public class TestingFunc {
    private double anchrX;
    private double anchrY;
    private double anchr_angX = 0;
    private double anchr_angY = 0;
    private final DoubleProperty anglX = new SimpleDoubleProperty();
    private final DoubleProperty anglY = new SimpleDoubleProperty();
    public static void main(String[] args) {
        double test = 3.14;
        int test_int = ((Number) test).intValue();
        Tab t = new Tab();

        System.out.println(test_int);
    }
}
