package Utils3DCreation.com;
import ColorsPaletteExtraction.Extractor;
import ColorsPaletteExtraction.Tracker;
import Grid.PositionUtility;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Utils {
        public static int faceCount = 0;
        public static int x_ori = 0;
        public static int y_ori = 0;
        public static int z_ori = 0;
        public static int [][] cubesX = {};
        public static int [][] cubesZ = {};
        public static int [][] cubesXZ = {}; //array of position where to place cubes
        public static ArrayList<Color> allColArr = new ArrayList<>(); //toute couleur
        public static ArrayList<Color> arrCol  = new ArrayList<>();// seulement couleur principale
        public static int colorCount = 0;
        public static ArrayList<String> simpleArr = new ArrayList<>();
        public static String previousSubmit = "";
        public static void Create3DObject(){// function to create cubes
            faceCount = 0;x_ori = 0;y_ori = 0;z_ori = 0;colorCount = 0;

            removeStringFromFile("MyName.obj",previousSubmit);
            CreateFile("MyName.obj");
            //CreateFile("UColors.mtl");
            WriteFile("UColors.mtl",addMaterialShaders());
            int [][][] arrays = new int[][][]{ {{0+x_ori,0+y_ori,0+z_ori},{0+x_ori,1+y_ori,0+z_ori},{1+x_ori,1+y_ori,0+z_ori},{1+x_ori,0+y_ori,0+z_ori}} ,{{1+x_ori,0+y_ori,0+z_ori},
                    {1+x_ori,0+y_ori,1+z_ori},{1+x_ori,1+y_ori,1+z_ori},{1+x_ori,1+y_ori,0+z_ori}},{{0+x_ori,0+y_ori,0+z_ori},{0+x_ori,0+y_ori,1+z_ori},{0+x_ori,1+y_ori,1+z_ori},
                    {0+x_ori,1+y_ori,0+z_ori}} ,{{0+x_ori,0+y_ori,1+z_ori},{0+x_ori,1+y_ori,1+z_ori},{1+x_ori,1+y_ori,1+z_ori},{1+x_ori,0+y_ori,1+z_ori}},{{0+x_ori,1+y_ori,0+z_ori},
                    {0+x_ori,1+y_ori,1+z_ori},{1+x_ori,1+y_ori,1+z_ori},{1+x_ori,1+y_ori,0+z_ori}}, {{0+x_ori,0+y_ori,0+z_ori},{1+x_ori,0+y_ori,0+z_ori},{1+x_ori,0+y_ori,1+z_ori},
                    {0+x_ori,0+y_ori,1+z_ori}}};

            WriteFile("MyName.obj",Preenbule("Nate","bale")+AddVertices(cubesXZ)+CreateVerticesNormals()+MakeMultiply(cubesXZ));
            previousSubmit = AddVertices(cubesXZ)+CreateVerticesNormals()+MakeMultiply(cubesXZ);
        }

        private static String mergeFaces(String face1, String face2) {
            // Extract the vertex strings from each face
            String[] vertices1 = face1.substring(2).split("\\s+"); // Remove the 'f ' prefix
            String[] vertices2 = face2.substring(2).split("\\s+"); // Remove the 'f ' prefix

            // Preserve the order of appearance using LinkedHashSet
            Set<String> uniqueVertices = new LinkedHashSet<>(Arrays.asList(vertices1));
            uniqueVertices.addAll(Arrays.asList(vertices2));

            // Join the unique vertices into a single line with 'f ' prefix
            String mergedFace = "f " + String.join(" ", uniqueVertices);

            return mergedFace;
        }
        private static String reorderVertices(String obj) {
            String[] objLines = obj.split("\n");
            for (int i = 0; i < objLines.length; i++) {
                simpleArr.add(objLines[i]);
            }
            return ordonateArray();
        }
        public static String ordonateArray(){
            String allFaces = "";
            while(simpleArr.size() > 0){
                allFaces += mergeFaces(simpleArr.get(0),simpleArr.get(1))+"\n";
                simpleArr.remove(0);
                simpleArr.remove(0);


            };
            return allFaces;
        }

        public static void CreateFile (String x){
            try {
                File myObj = new File(x);
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        public static void WriteFile(String name,String x) {
            try {
                FileWriter myWriter = new FileWriter(name);
                myWriter.write(x);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        public static String Preenbule(String nom ,String t){
            String pre ="# "+nom+".obj\n" +
                    "#\n" +
                    " \n" +"mtllib uColors.mtl"+"\n"+
                    "g "+t+"\n" ;
            return pre;
        }

        public static String CreateVerticesNormals(){
            String normals = "vn  0.0  0.0  1.0\n" +
                    "vn  0.0  0.0 -1.0\n" +
                    "vn  0.0  1.0  0.0\n" +
                    "vn  0.0 -1.0  0.0\n" +
                    "vn  1.0  0.0  0.0\n" +
                    "vn -1.0  0.0  0.0\n" +
                    " \n" ;
            return normals;
        }

        public static String AddVertices(int data[][]){
        String vertices = " \n";
        for (int d = 0; d < data.length; d++) {
            x_ori = data[d][0];
            y_ori = data[d][1];
            z_ori = data[d][2];

            int[][][] arrays = new int[][][]{{{0 + x_ori, 0 + y_ori, 0 + z_ori}, {0 + x_ori, 1 + y_ori, 0 + z_ori}, {1 + x_ori, 1 + y_ori, 0 + z_ori}, {1 + x_ori, 0 + y_ori, 0 + z_ori}}, {{1 + x_ori, 0 + y_ori, 0 + z_ori},
                    {1 + x_ori, 0 + y_ori, 1 + z_ori}, {1 + x_ori, 1 + y_ori, 1 + z_ori}, {1 + x_ori, 1 + y_ori, 0 + z_ori}}, {{0 + x_ori, 0 + y_ori, 0 + z_ori}, {0 + x_ori, 0 + y_ori, 1 + z_ori}, {0 + x_ori, 1 + y_ori, 1 + z_ori},
                    {0 + x_ori, 1 + y_ori, 0 + z_ori}}, {{0 + x_ori, 0 + y_ori, 1 + z_ori}, {0 + x_ori, 1 + y_ori, 1 + z_ori}, {1 + x_ori, 1 + y_ori, 1 + z_ori}, {1 + x_ori, 0 + y_ori, 1 + z_ori}}, {{0 + x_ori, 1 + y_ori, 0 + z_ori},
                    {0 + x_ori, 1 + y_ori, 1 + z_ori}, {1 + x_ori, 1 + y_ori, 1 + z_ori}, {1 + x_ori, 1 + y_ori, 0 + z_ori}}, {{0 + x_ori, 0 + y_ori, 0 + z_ori}, {1 + x_ori, 0 + y_ori, 0 + z_ori}, {1 + x_ori, 0 + y_ori, 1 + z_ori},
                    {0 + x_ori, 0 + y_ori, 1 + z_ori}}};

            int count = 1;
            for (int i = 0; i < arrays.length; i++) {
                int[][] t = arrays[i];
                for (int j = 0; j < t.length; j++) {
                    int[] c = t[j];
                    vertices += "v  " + c[0] + "  " + c[1] + "  " + c[2] + ("  #" + count) + "\n";
                    count++;
                }

            }
        }
        vertices += " \n";
        return vertices;
    }
        public static String MakeACube(int arr[][][]){

        String face = "";

        String vn = "2";
        for (int i = 0; i < arr.length; i++) {

            if(faceCount == 0){
                vn = "2";
            }if(faceCount == 4){
                vn = "5";
            }if(faceCount == 8){
                vn = "6";
            }if(faceCount == 12){
                vn = "1";
            }if(faceCount == 16){
                vn = "3";
            }if(faceCount == 20){
                vn = "4";
            }
            face+="f  "+(1+faceCount)+"//"+vn+"  "+(2+faceCount)+"//"+vn+"  "+(3+faceCount)+"//"+vn+"\n";
            face+="f  "+(1+faceCount)+"//"+vn+"  "+(4+faceCount)+"//"+vn+"  "+(3+faceCount)+"//"+vn+"\n";
            faceCount+=4;

        }
        face = reorderVertices(face);
        if (colorCount < allColArr.size()){
                face = PositionUtility.findCorrectMaterial(allColArr.get(colorCount)) + "\ns off\n"+face;
                colorCount += 1;
        }
        return face;

    }

        public static String MakeMultiply(int data[][]){
        String all_faces = "";
        for (int i = 0; i < data.length; i++) {
                x_ori = data[i][0];
                y_ori = data[i][1];
                z_ori = data[i][2];
                int [][][] arrays = new int[][][]{ {{0+x_ori,0+y_ori,0+z_ori},{0+x_ori,1+y_ori,0+z_ori},{1+x_ori,1+y_ori,0+z_ori},{1+x_ori,0+y_ori,0+z_ori}} ,{{1+x_ori,0+y_ori,0+z_ori},
                        {1+x_ori,0+y_ori,1+z_ori},{1+x_ori,1+y_ori,1+z_ori},{1+x_ori,1+y_ori,0+z_ori}},{{0+x_ori,0+y_ori,0+z_ori},{0+x_ori,0+y_ori,1+z_ori},{0+x_ori,1+y_ori,1+z_ori},
                        {0+x_ori,1+y_ori,0+z_ori}} ,{{0+x_ori,0+y_ori,1+z_ori},{0+x_ori,1+y_ori,1+z_ori},{1+x_ori,1+y_ori,1+z_ori},{1+x_ori,0+y_ori,1+z_ori}},{{0+x_ori,1+y_ori,0+z_ori},
                        {0+x_ori,1+y_ori,1+z_ori},{1+x_ori,1+y_ori,1+z_ori},{1+x_ori,1+y_ori,0+z_ori}}, {{0+x_ori,0+y_ori,0+z_ori},{1+x_ori,0+y_ori,0+z_ori},{1+x_ori,0+y_ori,1+z_ori},
                        {0+x_ori,0+y_ori,1+z_ori}}};
                all_faces += MakeACube(arrays);

        }
        return all_faces;
    }

        public static String addMaterialShaders(){
            String materials = "";
            for (int i = 0; i < arrCol.size(); i++) {
                materials += "newmtl Material"+i+"\n"+
                "Ns 225.000000"+"\n"+
                "Ka 1.000000 1.000000 1.000000"+"\n"+
                "Kd "+arrCol.get(i).getRed()+" "+arrCol.get(i).getGreen()+" "+arrCol.get(i).getBlue()+"\n"+
                "Ks 0.500000 0.500000 0.500000"+"\n"+
                "Ke 0.000000 0.000000 0.000000"+"\n"+
                "Ni 1.450000"+"\n"+
                "d 1.000000"+"\n"+
                "illum 2"+"\n"+"\n";
            }
            return materials;
        }

    private static void removeStringFromFile(String filePath, String stringToRemove) {
        if(stringToRemove.trim().length() != 0){
            File file = new File(filePath);
            StringBuilder contentBuilder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.replace(stringToRemove, "");
                    contentBuilder.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(contentBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
