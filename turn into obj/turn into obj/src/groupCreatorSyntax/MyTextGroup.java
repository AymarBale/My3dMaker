package groupCreatorSyntax;

public class MyTextGroup {
    String groupName;
    int valAxis;
    String axis;
    String corner;
    int batch;
    String color;

    public MyTextGroup() {

    }

    public MyTextGroup(String groupName, int valAxis, String axis, String corner, int batch, String color) {
        this.groupName = groupName;
        this.valAxis = valAxis * 10;
        this.axis = axis;
        this.corner = corner;
        this.batch = batch;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Group{" +"\n"+
                "GroupName:" + groupName + "\n" +
                "valAxis=" + valAxis +"\n"+
                "axis=" + axis + '\n' +
                "corner={" + corner + "};\n" +/**/
                "batch=" + batch +"\n"+
                "color=" + color + ";\n" +
                '}';/**/
    }
}
