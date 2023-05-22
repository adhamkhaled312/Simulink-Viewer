package userInterfacePackage;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class ButtonAndLabel extends VBox{
    private Button button = new Button();
    private Label label;

    public ButtonAndLabel() {}
    public ButtonAndLabel(String name, String path) {
        super();
        button.setPrefSize(60, 60);
        label = new Label(name);
        label.setId("button-label");
        ImageView photo= new ImageView(new Image("Images/" + name + "Symbol.png"));
        photo.setFitHeight(30);
        photo.setFitWidth(30);
        button.setGraphic(photo);
        button.setContentDisplay(ContentDisplay.TOP);
        this.getChildren().addAll(button, label);
        this.setAlignment(Pos.CENTER);
    }

    public Button getButton() {
        return button;
    }
    public Label getLabel() {
        return label;
    }
}
