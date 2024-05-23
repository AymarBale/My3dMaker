package testingFunction;

import ColorsPaletteExtraction.Tracker;
import Grid.GridPage;
import ImageTaker.QuickImport;
import groupCreatorSyntax.SyntaxDetector;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static groupCreatorSyntax.CodingEditor.deepCopyTracker;
import static groupCreatorSyntax.SyntaxDetector.*;
import static javafx.application.Application.launch;

public class TestingFunc {
    public static ArrayList<String> extractGroupNames(String input) {
        ArrayList<String> groupNames = new ArrayList<>();

        // Regular expression to match "GROUPNAME" sections
        Pattern pattern = Pattern.compile("GROUPNAME\\(.*?\\)\\{[^\\}]*\\}");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            groupNames.add(matcher.group());
        }
        //"nathan"
        return groupNames;
    }


    public static void main(String[] args) {
        String input = "GROUPNAME(back){\n" +
                "valAxis: 9;\n" +
                "axis: X;\n" +
                "Corner: {[10,10],[10,10],[10,90],[90,90],[90,10]};\n" +
                "batch: 44;\n" +
                "color: #FFFFFF;\n" +
                "}\n" +
                "GROUPNAME(left){\n" +
                "valAxis: 9;\n" +
                "axis: Z;\n" +
                "Corner: {[10,10],[10,10],[80,10],[80,90],[10,90]};\n" +
                "batch: 55;\n" +
                "color: #FFFFFF;\n" +
                "}\n" +
                "GROUPNAME(Bottom){\n" +
                "valAxis: 8;\n" +
                "axis: Y;\n" +
                "Corner: {[10,10],[10,10],[10,70],[70,70],[70,10]};\n" +
                "batch: 66;\n" +
                "color: #FFFFFF;\n" +
                "}\n" +
                "batchRp(22){\n" +
                "  UAxis:Y;\n" +
                "  X:1;\n" +
                "  Z:0;\n" +
                "}";
        ArrayList<Tracker> copyArrayList = new ArrayList<>();
        for (Tracker tracker : GridPage.theMainExtratorArr) {
            copyArrayList.add(deepCopyTracker(tracker));
        }
        if (input.contains("batchRp(")){
            getAllBatchs(copyArrayList,input);
        }

        //System.out.println();
    }
}

