package ColorsPaletteExtraction;

public class Tracker {
    public double x;
    public double y;
    public double z;
    public javafx.scene.paint.Color col;


    public Tracker() {

    }

    public Tracker(double posX, double posY, double posZ, javafx.scene.paint.Color initCol) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.col = initCol;
    }

    public String printOutString() {
        String s = "X:"+this.x + " Y:" + this.y + " Z:" + this.z + " COL:" + this.col;
        return s;
    }
}
