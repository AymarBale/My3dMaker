package ImageTaker;

import javafx.scene.control.Tab;

public class AdvanceTab extends Tab {
    public int batch = 0;
    public int [] mergeBatch = {0,0};

    public AdvanceTab(String s) {
        super();
        setText(s);
    }

    public AdvanceTab(){

    }
}
