package userInterfacePackage;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.ImagePattern;

public class ButtonAndLabel extends VBox{
    private Rectangle picture;
    private Label label;

    public ButtonAndLabel() {}
    public ButtonAndLabel(String name, String path) {
        super();
        picture = new Rectangle(30, 30);
        label = new Label(name);
        label.setId("button-label");
        ImagePattern imagePattern = new ImagePattern(new Image("Images/" + name + "Symbol.png"));
        picture.setFill(imagePattern);
        this.setMinWidth(90);
        this.setMinHeight(100);
        this.getChildren().addAll(picture, label);
        this.setAlignment(Pos.CENTER);
    }

    public Rectangle getPicture() {
        return picture;
    }
    public Label getLabel() {
        return label;
    }
}
