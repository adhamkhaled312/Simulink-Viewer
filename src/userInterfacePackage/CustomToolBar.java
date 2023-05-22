package userInterfacePackage;

import javafx.scene.control.*;

public class CustomToolBar extends ToolBar {
    ButtonAndLabel[] toolList;

    public CustomToolBar() {}
    public CustomToolBar(String[] names) {
        super();
        toolList = new ButtonAndLabel[names.length];
        for (int i = 0; i < names.length; i++) {
            toolList[i] = new ButtonAndLabel(names[i], "");
            this.getItems().add(toolList[i]);
            toolList[i].setId("tool");
        }
        this.setId("tool-bar");
    }

    public ButtonAndLabel getButtonAndLabel(int i) {
        return toolList[i];
    }
}
