package ColorsPaletteExtraction;

import Editor.EditorSettings;
import Grid.GridPage;
import ImageTaker.AdvanceTab;
import ImageTaker.GetMyImageAlongAxe;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static Editor.EditorSettings.tabPane;

public class Extractor extends Application {
    public static int myGridSizeX = 10;
    public static int myGridSizeY = 10;
    public static double [] currentPos = {0,0};
    public static String imagePath ;
    public static Image imag ;
    public Group root = new Group();
    public static Group rectGroup = new Group();
    public static Group remake = new Group();
    public static double additiveX = 13.4;
    public static ArrayList<Tracker> myCubes = new ArrayList<>();
    public static double [][]cubePosArr = new double[][]{};

    private static double startX;
    private static double startY;


    @Override
    public void start(Stage stage) {
        imag = new Image(imagePath);
        AtomicInteger opt = new AtomicInteger();
        Image image = new Image(imagePath);

        // Create an ImageView to display the image
        ImageView imageView = new ImageView(image);
        Slider sliderX = new Slider(0,40,0.1);
        Slider sliderY = new Slider(0,20,0.1);
        Button getAllPos = new Button("Get all the position");
        Button remakeImg = new Button("Remake the image");
        Button openNewStage = new Button("New Scene");
        Button gridSizeX = new Button("SetTheGridSizeX");
        javafx.scene.control.TextField gridSizeTextX = new javafx.scene.control.TextField();
        Button gridSizeY = new Button("SetTheGridSizeY");
        javafx.scene.control.TextField gridSizeTextY = new javafx.scene.control.TextField();

        Label numLabel = new Label();

        remake.setLayoutX(image.getWidth()+150);
        root.getChildren().add(remake);
        root.getChildren().add(sliderX);
        root.getChildren().add(sliderY);
        root.getChildren().add(numLabel);
        root.getChildren().add(getAllPos);
        root.getChildren().add(remakeImg);
        root.getChildren().add(openNewStage);
        root.getChildren().add(gridSizeX);
        root.getChildren().add(gridSizeTextX);
        root.getChildren().add(gridSizeY);
        root.getChildren().add(gridSizeTextY);

        sliderX.setShowTickLabels(true);
        sliderY.setShowTickLabels(true);
        getAllPos.setLayoutX(image.getWidth()+100);
        getAllPos.setLayoutY(100);

        remakeImg.setLayoutX(image.getWidth()+100);
        remakeImg.setLayoutY(150);

        openNewStage.setLayoutX(image.getWidth()+100);
        openNewStage.setLayoutY(250);

        gridSizeX.setLayoutX(image.getWidth()+100);
        gridSizeX.setLayoutY(300);

        gridSizeTextX.setLayoutX(image.getWidth()+100);
        gridSizeTextX.setLayoutY(350);

        gridSizeY.setLayoutX(image.getWidth()+100);
        gridSizeY.setLayoutY(400);

        gridSizeTextY.setLayoutX(image.getWidth()+100);
        gridSizeTextY.setLayoutY(450);
        
        sliderX.setRotate(90);
        sliderX.setLayoutX(image.getWidth());
        sliderX.setLayoutY(100);
        sliderY.setLayoutX(100);
        sliderY.setLayoutY(425);
        numLabel.setLayoutX(image.getWidth()+10);
        numLabel.setLayoutY(200);

        gridSizeX.setOnAction(event -> {
            myGridSizeX = Integer.parseInt(gridSizeTextX.getText());
        });

        gridSizeY.setOnAction(event -> {
            myGridSizeY = Integer.parseInt(gridSizeTextY.getText());
        });
        getAllPos.setOnAction(event -> {
            opt.set(1);
            AddCubesToSender();
        });
        remakeImg.setOnAction(event -> {
            timeToRemake();
        });
        openNewStage.setOnAction(event -> {
            try {
                openSecondStage(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sliderX.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                rectGroup.getChildren().clear();
                numLabel.setText(String.valueOf(newValue));
                additiveX = (double) newValue;
            }
        });
        sliderX.setOnMouseReleased(event -> {
            rectGroup.getChildren().clear();
            numLabel.setText(String.valueOf(additiveX));
            GridL(currentPos[0],currentPos[1]/*10,10*/,myGridSizeX,myGridSizeY);//13.3
        });

        StackPane jo = new StackPane(imageView);
        root.getChildren().add(jo);
        root.getChildren().add(rectGroup);

        Scene scene = new Scene(root, image.getWidth()*3, image.getHeight()*1.5);

        imageView.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();

            // Get the PixelReader from the image
            PixelReader pixelReader = image.getPixelReader();

            // Read the color of the pixel at the clicked coordinates
            javafx.scene.paint.Color color = pixelReader.getColor((int) x, (int) y);

            // Display the color information (for example, in the console)

        });

        root.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        if(opt.get() == 0){
                        rectGroup.getChildren().clear();
                            GridL(e.getX(),e.getY(),myGridSizeX,myGridSizeY);
                            currentPos[0] = e.getX();
                            currentPos[1] = e.getY();
                        }else if(opt.get() == 1){


                        }

                };
        });
        stage.setOnCloseRequest(event -> {
            if (!tabPane.getTabs().isEmpty()) {
                tabPane.getTabs().remove(tabPane.getTabs().size() - 1);
                if(EditorSettings.i >= 0)
                EditorSettings.i -= 1;
            }
        });

        // Set the scene in the primary stage
        stage.setScene(scene);
        stage.setTitle("Pixel Color Viewer with Rectangle");
        stage.show();
    }

    public static void GridL(double x,double y,int xGrid,int yGrid){
        cubePosArr = new double[][]{};
        double initX = x;

        double initY = y;
        for (int i = 0; i < yGrid; i++) {
            MakeCube(x,initY);

            initX = x;
            for(int j = 0;j < xGrid;j++){
                MakeCube(initX,initY);
                getPosition(initX,initY);
                initX+=additiveX;
            }
            initY+= additiveX;
        }
    }

    public static void MakeCube(double x,double y){
        Circle r = new Circle(2);
        r.setFill(javafx.scene.paint.Color.DARKRED);
        r.setCenterX(x);
        r.setCenterY(y);
        rectGroup.getChildren().add(r);

    }
    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void GetColors(double x, double y ){

        return ;
    }

    public static void getPosition(double x,double y){
        double []miniArr = new double[]{x,y,0};
        cubePosArr = addArrayToIntArrays(cubePosArr,miniArr);
    }

    public static double[][] addArrayToIntArrays(double[][] arrayOfArrays, double[] arrayToAdd) {
        double newArray[][] = new double[arrayOfArrays.length + 1][];
        for (int i = 0; i <arrayOfArrays.length;  i++) {
            newArray[i] = arrayOfArrays[i];
        }
        newArray[arrayOfArrays.length] = arrayToAdd;
        return newArray;
    }

    public static javafx.scene.paint.Color turnIntoRgb(Image image, double x, double y){
        PixelReader pixelReader = image.getPixelReader();

        javafx.scene.paint.Color color = pixelReader.getColor((int) x, (int) y);

        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        return color;
    }

    public static void AddCubesToSender(){
        for (int i = 0; i < cubePosArr.length; i++) {
                if((!turnIntoRgb(imag,cubePosArr[i][0],cubePosArr[i][1]).equals(javafx.scene.paint.Color.WHITE))&((turnIntoRgb(imag,cubePosArr[i][0],cubePosArr[i][1])).getOpacity() == 1)) {
                    myCubes.add(new Tracker(cubePosArr[i][0], cubePosArr[i][1], 0, turnIntoRgb(imag, cubePosArr[i][0], cubePosArr[i][1])));
                }
        }
    }

    public static void timeToRemake(){
        for (int i = 0; i < myCubes.size(); i++){
            Rectangle r = new Rectangle(additiveX,additiveX);
            r.setX(myCubes.get(i).x);
            r.setY(myCubes.get(i).y);
            r.setFill(myCubes.get(i).col);
            remake.getChildren().add(r);
        }
    }

    public static void QuickShort(Stage s){
        if(GetMyImageAlongAxe.chosenAxe.equals("X")){
            additiveX = 10.270;
            GridL(12.4,12.4,9,9);
            AddCubesToSender();
            timeToRemake();
        }else if(GetMyImageAlongAxe.chosenAxe.equals("Z")){
            additiveX = 10.270;
            GridL(12.4,12.2,8,9);
            AddCubesToSender();
            timeToRemake();
        }

        /**/try {
            if(GetMyImageAlongAxe.count == 0){}
                openSecondStage(s);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openSecondStage(Stage innerStage) throws Exception {
        innerStage.close();
        AdvanceTab myTab = (AdvanceTab) tabPane.getTabs().get(tabPane.getTabs().size()-1);
        myTab.setText(myTab.getText()+" AXIS:"+GetMyImageAlongAxe.chosenAxe);
        myTab.batch = EditorSettings.batchCount;
        myTab.setContent(GridPage.exportPane(GetMyImageAlongAxe.chosenAxe,myTab.batch));
        EditorSettings.batchCount += 11;
        ResetVariables();
    }

    public static void ResetVariables(){
        rectGroup = new Group();
        remake = new Group();
        additiveX = 13.4;
        myCubes = new ArrayList<>();
        cubePosArr = new double[][]{};
        myGridSizeX = 10;
        myGridSizeY = 10;
        currentPos = new double[]{0, 0};
        imag = null;
    }

}

