package Utils3DCreation.com;

import ColorsPaletteExtraction.Tracker;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Rules {
    public static ArrayList<String> arr = new ArrayList<>();
    public String direction;
    public String rule;
    public int initX;
    public int initY;
    public static void ApplyRule(ArrayList<Tracker> trackers , String rule, Line line){
        String[] parts = rule.split(" "); // Split the rule string by space
        if (parts.length != 3) {
            System.out.println("Invalid input format: " + rule);
            return;
        }
        String valueString = parts[0];
        String sign = parts[1];
        String allOrOnly = parts[2];


        String dir = (line.getStartX() == line.getEndX()) ? "vert" : "hori";
        System.out.println(valueString+" "+sign+" "+allOrOnly+" "+dir);

        int val = Integer.parseInt(valueString);
        for (int i = 0; i < trackers.size(); i++) {
            if(allOrOnly.equals("all")){
                if(dir.equals("vert")){
                    if((trackers.get(i).y >= line.getStartY()-5)&&(trackers.get(i).y <= line.getEndY()+5)){
                        if(sign.equals("++")){
                            trackers.get(i).z = trackers.get(i).z + val;
                            val += Integer.parseInt(valueString);
                        }else if(sign.equals("+")){
                            trackers.get(i).z += val;
                        }else if(sign.equals("--")){
                            val = (val>0) ? -val : val;
                            trackers.get(i).z += val;
                            val -= Integer.parseInt(valueString);
                        }else if(sign.equals("-")){
                            trackers.get(i).z -= val;
                        }

                    }
                }else if(dir.equals("hori")){
                    if((trackers.get(i).x >= line.getStartX()-5)&&(trackers.get(i).x <= line.getEndX()+5)){
                        if(sign.equals("++")){
                            trackers.get(i).z += val ;
                            val += Integer.parseInt(valueString);
                        }else if(sign.equals("+")){
                            trackers.get(i).z += Integer.parseInt(valueString);
                        }else if(sign.equals("--")){
                            val = (val>0) ? -val : val;
                            trackers.get(i).z += val ;
                            val -= Integer.parseInt(valueString);
                        }else if(sign.equals("-")){
                            trackers.get(i).z -= Integer.parseInt(valueString);
                        }/**/
                    }
                }
            }else if(allOrOnly.equals("only")){
                if(dir.equals("vert")){
                    if((trackers.get(i).y >= line.getStartY()-5)&&(trackers.get(i).y <= line.getEndY()+5)&&
                            (trackers.get(i).x == line.getStartX() - 5)){
                        if(sign.equals("++")){
                            System.out.println("test"+val+Integer.parseInt(valueString));
                            trackers.get(i).z = val+trackers.get(i).z;
                            val += Integer.parseInt(valueString);
                        }else if(sign.equals("+")){
                            trackers.get(i).z += val;
                        }else if(sign.equals("--")){
                            val = (val>0) ? -val : val;
                            trackers.get(i).z = val;
                            val -= Integer.parseInt(valueString);
                        }else if(sign.equals("-")){
                            trackers.get(i).z -= val;
                        }
                    }
                }else if(dir.equals("hori")){
                    /*System.out.println(line.getStartX()-5+" <-> "+trackers.get(i).x +" | "+(line.getStartY()-5)+" <-> "+trackers.get(i).y+"\n"+
                            (line.getEndX()-5)+" <-> "+trackers.get(i).x +" | "+(line.getEndY()-5)+" <-> "+trackers.get(i).y);*/
                    if((trackers.get(i).x >= line.getStartX()-5)&&(trackers.get(i).x <= line.getEndX()+5)&&
                            (trackers.get(i).y == line.getStartY() - 5)){

                        if(sign.equals("++")){
                            trackers.get(i).z += val;
                            val += Integer.parseInt(valueString);
                        }else if(sign.equals("+")){
                            trackers.get(i).z += Integer.parseInt(valueString);
                        }else if(sign.equals("--")){
                            val = (val>0) ? -val : val;
                            trackers.get(i).z = val ;
                            val -= Integer.parseInt(valueString);
                        }else if(sign.equals("-")){
                            trackers.get(i).z -= Integer.parseInt(valueString);
                        }
                    }
                    /**/
                }
            }
        }

        String info = "Starting point:"+line.getStartX()+" "+ line.getStartY()+"\n Ending point:"+line.getEndX()+" "+ line.getEndY()+"\n "
                +rule;
        arr.add(info);
    }

    public static void ApplyRuleForText(ArrayList<Tracker> trackers , String rule, Line line){
        String[] parts = rule.split(" "); // Split the rule string by space
        if (parts.length != 3) {
            System.out.println("Invalid input format: " + rule);
            return;
        }
        String valueString = parts[0];
        String sign = parts[1];
        String allOrOnly = parts[2];


        String dir = (line.getStartX() == line.getEndX()) ? "vert" : "hori";
        System.out.println(valueString+" "+sign+" "+allOrOnly+" "+dir);

        int val = Integer.parseInt(valueString)*10;
        for (int i = 0; i < trackers.size(); i++) {
            if(allOrOnly.equals("all")){
                if(dir.equals("vert")){
                    if((trackers.get(i).y >= line.getStartY()-5)&&(trackers.get(i).y <= line.getEndY()+5)){
                        if(sign.equals("++")){
                            trackers.get(i).z = trackers.get(i).z + val;
                            val += Integer.parseInt(valueString);
                        }else if(sign.equals("+")){
                            trackers.get(i).z += val;
                        }else if(sign.equals("--")){
                            val = (val>0) ? -val : val;
                            trackers.get(i).z += val;
                            val -= Integer.parseInt(valueString);
                        }else if(sign.equals("-")){
                            trackers.get(i).z -= val;
                        }

                    }
                }else if(dir.equals("hori")){
                    if((trackers.get(i).x >= line.getStartX()-5)&&(trackers.get(i).x <= line.getEndX()+5)){
                        if(sign.equals("++")){
                            trackers.get(i).z += val ;
                            val += Integer.parseInt(valueString);
                        }else if(sign.equals("+")){
                            trackers.get(i).z += Integer.parseInt(valueString);
                        }else if(sign.equals("--")){
                            val = (val>0) ? -val : val;
                            trackers.get(i).z += val ;
                            val -= Integer.parseInt(valueString);
                        }else if(sign.equals("-")){
                            trackers.get(i).z -= Integer.parseInt(valueString);
                        }/**/
                    }
                }
            }else if(allOrOnly.equals("only")){
                if(dir.equals("vert")){
                    if((trackers.get(i).y >= line.getStartY()-5)&&(trackers.get(i).y <= line.getEndY()+5)&&
                            (trackers.get(i).x == line.getStartX() - 5)){
                        if(sign.equals("++")){
                            System.out.println("test"+val+Integer.parseInt(valueString));
                            trackers.get(i).z = val+trackers.get(i).z;
                            val += Integer.parseInt(valueString);
                        }else if(sign.equals("+")){
                            trackers.get(i).z += val;
                        }else if(sign.equals("--")){
                            val = (val>0) ? -val : val;
                            trackers.get(i).z = val;
                            val -= Integer.parseInt(valueString);
                        }else if(sign.equals("-")){
                            trackers.get(i).z -= val;
                        }
                    }
                }else if(dir.equals("hori")){
                    /*System.out.println(line.getStartX()-5+" <-> "+trackers.get(i).x +" | "+(line.getStartY()-5)+" <-> "+trackers.get(i).y+"\n"+
                            (line.getEndX()-5)+" <-> "+trackers.get(i).x +" | "+(line.getEndY()-5)+" <-> "+trackers.get(i).y);*/
                    if((trackers.get(i).x >= line.getStartX()-5)&&(trackers.get(i).x <= line.getEndX()+5)&&
                            (trackers.get(i).y == line.getStartY() - 5)){

                        if(sign.equals("++")){
                            System.out.println(trackers.get(i).z);
                            trackers.get(i).z += val;
                            System.out.println(trackers.get(i).z);
                            val += Integer.parseInt(valueString)*10;
                        }else if(sign.equals("+")){
                            trackers.get(i).z += Integer.parseInt(valueString);
                        }else if(sign.equals("--")){
                            val = (val>0) ? -val : val;
                            trackers.get(i).z = val ;
                            val -= Integer.parseInt(valueString);
                        }else if(sign.equals("-")){
                            trackers.get(i).z -= Integer.parseInt(valueString);
                        }
                    }
                    /**/
                }
            }
        }

        String info = "Starting point:"+line.getStartX()+" "+ line.getStartY()+"\n Ending point:"+line.getEndX()+" "+ line.getEndY()+"\n "
                +rule;
        arr.add(info);
    }
}
