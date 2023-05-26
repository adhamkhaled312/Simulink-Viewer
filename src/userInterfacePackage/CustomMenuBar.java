package userInterfacePackage;

import javafx.scene.control.*;

public class CustomMenuBar extends MenuBar {
    
    public CustomMenuBar() {}
    public CustomMenuBar(String[] names) {
        super();
        for (int i = 0; i < names.length; i++) {
            Menu menu = new Menu(names[i]);
            menu.getItems().add(new MenuItem());
            this.getMenus().add(menu); 
        }
        for (int i = 0; i < names.length; i++) {
            Menu menu = this.getMenus().get(i);
            menu.onShownProperty().setValue( (e)->{
                menu.hide();
                for (int j = 0; j < names.length; j++) {
                    this.getMenus().get(j).setId("");
                }
                menu.setId("current-menu");
            });
        }
        this.getMenus().get(0).setId("current-menu");
    }

    public Menu getMenu(int i) {
        return getMenus().get(i);
    }

    public Menu addMenu(String name) {
        Menu newMenu = new Menu(name);
        newMenu.getItems().add(new MenuItem());
        this.getMenus().add(newMenu);
        for (int i = 0; i < size(); i++) {
            Menu menu = this.getMenus().get(i);
            menu.onShownProperty().setValue( (e)->{
                menu.hide();
                for (int j = 0; j < size(); j++) {
                    this.getMenus().get(j).setId("");
                }
                menu.setId("current-menu");
            });
        }
        return newMenu;
    }
    public void removeMenu(int index) {
        getMenus().remove(index);
    }

    public int size() {
        return getMenus().size();
    }
}
