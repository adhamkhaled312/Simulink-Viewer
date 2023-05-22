package userInterfacePackage;

import javafx.scene.control.*;

public class CustomMenuBar extends MenuBar {
    private Menu[] menuList;

    public CustomMenuBar() {}
    public CustomMenuBar(String[] names) {
        super();
        menuList = new Menu[names.length];
        for (int i = 0; i < names.length; i++) {
            menuList[i] = new Menu(names[i]);
            menuList[i].getItems().add(new MenuItem());
            this.getMenus().add(menuList[i]); 
        }
        for (int i = 0; i < names.length; i++) {
            final int ii = i;
            menuList[i].onShownProperty().setValue( (e)->{
                menuList[ii].hide();
                for (int j = 0; j < names.length; j++) {
                    menuList[j].setId("");
                }
                menuList[ii].setId("current-menu");
            });
        }
        menuList[0].setId("current-menu");
    }

    public Menu getMenu(int i) {
        return menuList[i];
    }
}
