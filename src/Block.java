import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
public class Block {
    private String blockType;
    private String name;
    private int id;
    private int[] position = new int[4];
    private int zOrder;
    HashMap<String, String> attributes;

    Block () {
    }

    Block (String blockType, String name, int id, int[] position, int zOrder, HashMap<String, String> attributes) {
        this.blockType = blockType;
        this.name = name;
        this.id = id;
        this.position = position;
        this.zOrder = zOrder;
        this.attributes = attributes;
    }

    public void print() {
        System.out.println("block type = " + blockType + ", name = " + name + ", ID = " + id + ", position = [" + position[0] + ", " + position[1] + ", " + position[2] + ", " + position[3] + "], zOrder = " + zOrder);
        System.out.println("attributes: " + attributes.toString() + "\n");
    }

    public void draw(Pane mainPane,Stage primaryStage){
        final int WINDOW_WIDTH = 1200;
        final int WINDOW_HEIGHT = 600;

        //Create a rectangle
        double rectangleWidth = position[2]-position[0];
        double rectangleHeight = position[3]-position[1];
        double borderWidth = 1;

        Rectangle rectangle = new Rectangle(rectangleWidth, rectangleHeight);
        rectangle.setFill(Color.BLACK);
        rectangle.setStroke(Color.BLUE);
        rectangle.setStrokeWidth(borderWidth);
        rectangle.setStrokeType(StrokeType.OUTSIDE);
        rectangle.setLayoutX(position[0]-700);
        rectangle.setLayoutY(position[1]);
        rectangle.setWidth(rectangleWidth);
        rectangle.setHeight(rectangleHeight);

        // Load the image for the block background
        Image blockImage;
        try {
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\" + this.blockType + ".png");
        }catch(Exception e){
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\Error.png");
        }

        ImagePattern imagePattern = new ImagePattern(blockImage);
        rectangle.setFill(imagePattern);

        // Create a label for the block name
        Label nameLabel = new Label(this.name);
        nameLabel.setLayoutX(position[0]-700);
        nameLabel.setLayoutY(position[3] + 5);
        nameLabel.setTextFill(Color.BLUE);

        // Create a pane to hold the block image and label
        mainPane.getChildren().addAll(rectangle, nameLabel);
        mainPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainPane.setPadding(new Insets(10));

        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double widthRatio = newWidth.doubleValue() / WINDOW_WIDTH;
            double newX = (position[0]-700) * widthRatio;
            rectangle.setLayoutX(newX);
            nameLabel.setLayoutX(newX);
        });
        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            double heightRatio = newHeight.doubleValue() / WINDOW_HEIGHT;
            double newY = position[1] * heightRatio;
            rectangle.setLayoutY(newY);
            nameLabel.setLayoutY(newY + (position[3]-position[1]) + 5);
        });
    }
}
