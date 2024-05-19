package ImageTaker;

import ColorsPaletteExtraction.Tracker;

import java.util.*;
import java.util.stream.Collectors;

import static groupCreatorSyntax.SyntaxDetector.getFirstLetterAfterSecondSemicolon;

public class QuickImport {
    public String path;
    public String chosenAxe;
    public double additive;
    public Map<String,Double> val = new HashMap<>();
    public static ArrayList<QuickImport> allQImport = new ArrayList<>();
    public QuickImport(){
        val.put("gridX", (double) 0);
        val.put("gridY", (double) 0);
        val.put("sizeX", (double) 0);
        val.put("sizeY", (double) 0);
    }
    public QuickImport(String path,String chosenAxe,double additive,
                       double gX,double gY,double sX,double sY){
        this.path = path;
        this.chosenAxe = chosenAxe;
        this.additive = additive;
        val.put("gridX", gX);
        val.put("gridY", gY);
        val.put("sizeX", sX);
        val.put("sizeY", sY);
    }
    public static void getAllQI(String input){
        String[] parts = input.split("\\}");

        // Store each part in an ArrayList
        ArrayList<String> partsList = new ArrayList<>();
        for (String part : parts) {
            partsList.add(part.trim()+""); // Add back the "}" character removed by split
        }

        // Print the ArrayList
        for (String part : partsList) {
            //findQI(cTrackers,part);
            if(!part.isEmpty())
            findQI(part);
        }
    }
    public static void findQI(String input){
        if (!input.contains("path:") || !input.contains("chosenAxe:") || !input.contains("additive:") || !input.contains("val:")) {
            // Handle the case where any of the required substrings are missing
            System.out.println("Error:"+input+"finish");
        }
        int pathStartIndex = input.indexOf("path:") + 5; // Start index of batchRp(
        int pathEndIndex = input.indexOf(";", pathStartIndex); // End index of batchRp(
        String path = input.substring(pathStartIndex, pathEndIndex);

        int chosenAxeStartIndex = input.indexOf("chosenAxe:") + 10; // Start index of batchRp(
        int chosenAxeEndIndex = input.indexOf(";", chosenAxeStartIndex); // End index of batchRp(
        String chosenAxe = input.substring(chosenAxeStartIndex, chosenAxeEndIndex);

        int additiveStartIndex = input.indexOf("additive:") + 9; // Start index of batchRp(
        int additiveEndIndex = input.indexOf(";", additiveStartIndex); // End index of batchRp(
        double additive = Double.parseDouble(input.substring(additiveStartIndex, additiveEndIndex));

        int valStartIndex = input.indexOf("val:") + 4; // Start index of batchRp(
        int valEndIndex = input.indexOf(";", valStartIndex); // End index of batchRp(
        String val = input.substring(valStartIndex, valEndIndex);
        ArrayList<Double> arr = convertStringToDoubleList(val);

        QuickImport myQi = new QuickImport(path,chosenAxe,additive,arr.get(0),arr.get(1),arr.get(2),arr.get(3));
        allQImport.add(myQi);
    }

    public static ArrayList<Double> convertStringToDoubleList(String input) {
        // Split the string by comma
        String[] stringArray = input.split(",");

        // Convert each string to a double and add to the list
        ArrayList<Double> doubleList = (ArrayList<Double>) Arrays.stream(stringArray)
                .map(Double::parseDouble)
                .collect(Collectors.toList());

        return doubleList;
    }
    @Override
    public String toString() {
        return "path:"+path+" \n"+
                "chosenAxe:"+chosenAxe+" \n"+
                "additive:"+additive+" \n"+
                "gridX:"+val.get("gridX")+" \n"+
                "gridY:"+val.get("gridY")+" \n"+
                "sizeX:"+val.get("sizeX")+" \n"+
                "sizeY:"+val.get("sizeY")+" \n";
    }
}
