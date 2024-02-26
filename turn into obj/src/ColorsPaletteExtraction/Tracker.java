package ColorsPaletteExtraction;

import Utils3DCreation.com.Utils;

import java.util.ArrayList;

public class Tracker {
    public double x;
    public double y;
    public double z;
    public String axis;
    public javafx.scene.paint.Color col;
    public int batch;


    public Tracker() {

    }
    public static Tracker findClosestTracker(ArrayList<Tracker> list, double targetX, double targetY,String axis,int batch) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Tracker list is null or empty.");
        }
        int index = 0;
        while (!list.get(index).axis.equals(axis)){
            index ++;
        }
        Tracker closestTracker = list.get(index);

        double minDistance = calculateDistance(targetX, targetY, closestTracker);

        for (int i = 1; i < list.size(); i++) {
            if((list.get(i).axis.equals(axis))&&(list.get(i).x < targetX)&&(list.get(i).y < targetY)&&(list.get(i).batch == batch)){
                Tracker currentTracker = list.get(i);
                double distance = calculateDistance(targetX, targetY, currentTracker);

                if (distance < minDistance) {
                    minDistance = distance;
                    closestTracker = currentTracker;
                }
            }

        }

        return closestTracker;
    }
    private static double calculateDistance(double targetX, double targetY, Tracker tracker) {
        double deltaX = targetX - tracker.x;
        double deltaY = targetY - tracker.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public static void mergeTrackers(ArrayList<Tracker> list, int index1, int index2) {
        Tracker tracker1 = list.get(index1);
        Tracker tracker2 = list.get(index2);
        double newX = 0;double newY = 0;double newZ = 0;
        newX = tracker1.x;
        newY = tracker1.y;
        newZ = tracker2.x/10;


        Tracker mergedTracker = new Tracker(newX, newY, newZ, tracker1.col, tracker1.axis);

        Utils.allColArr.set(index1, mergedTracker.col);
        Utils.allColArr.remove(index2);
        list.set(index1, mergedTracker);
        list.remove(index2);
    }

    public Tracker(double posX, double posY, double posZ, javafx.scene.paint.Color initCol) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.col = initCol;
    }

    public Tracker(double posX, double posY, double posZ, javafx.scene.paint.Color initCol,String axe) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.col = initCol;
        this.axis = axe;
    }
    public Tracker(double posX, double posY, double posZ, javafx.scene.paint.Color initCol,String axe,int id) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.col = initCol;
        this.axis = axe;
        this.batch = id;
    }
    public String printOutString() {
        String s = "";
        if(this.x < 450){
            s = "-----\nX:"+this.x + " Y:" + this.y + "  Z:" + this.z +" |axis: "+axis +" | batch :"+batch/*+ " \nCOL:" + this.col.getRed()*255 +" |"+this.col.getGreen()*255 +" |"+this.col.getBlue()*255 +"\n "+axis*/;
        }else if(this.x > 450){
            s = "-----\nX:"+(this.x-450) + " Y:" + this.y + "\n Z:" + this.z + " \nCOL:" + this.col.getRed()*255 +" |"+this.col.getGreen()*255 +" |"+this.col.getBlue()*255;
        }

        return s;
    }
    public static void removeSimilarList(ArrayList<ArrayList<Tracker>> listOfLists, ArrayList<Tracker> targetList) {
        listOfLists.removeIf(list -> list.equals(targetList));
    }
}
